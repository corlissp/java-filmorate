package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.models.Director;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component("FilmDBStorage")
public class FilmDBStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDBStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;
    private final DirectorStorage directorStorage;

    public FilmDBStorage(JdbcTemplate jdbcTemplate, GenreService genreService, DirectorStorage directorStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
        this.directorStorage = directorStorage;
    }

    @Override
    public Film getFilmByIdStorage(int filmId) {

        String sqlFilm = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.RATINGID = R.RATINGID " +
                "where FILMID = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilm, this::makeFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован!");
        }
        log.info("Найден фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilmsStorage() {
        String sql = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.RATINGID = R.RATINGID ";
        return jdbcTemplate.query(sql, this::makeFilm);
    }

    @Override
    public Film addFilmStorage(Film film) {
        String sqlQuery = "insert into FILM " +
                "(NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, RATINGID) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            preparedStatement.setInt(5, film.getRate());
            preparedStatement.setInt(6, Math.toIntExact(film.getMpa().getId()));
            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (film.getGenres() != null) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        String queryForFilmDirector = "INSERT into FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(queryForFilmDirector, film.getId(), director.getId());
            }
        }
        return getFilmByIdStorage(film.getId());
    }

    @Override
    public Film updateFilmStorage(Film film) {
        String sqlQuery = "update FILM " +
                "set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ? ,RATINGID = ? " +
                "where FILMID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        genreService.deleteFilmGenres(film.getId());
        if (film.getGenres() != null) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }

        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        String queryForDeleteDirectors = "DELETE FROM FILM_DIRECTOR WHERE FILM_ID = ?";
        jdbcTemplate.update(queryForDeleteDirectors, film.getId());
        String queryForFilmDirector = "INSERT into FILM_DIRECTOR (film_id, DIRECTOR_ID) VALUES (?, ?)";
        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(queryForFilmDirector, film.getId(), director.getId());
            }
        }
        return getFilmByIdStorage(film.getId());
    }

    public boolean deleteFilm(Film film) {
        String sqlQuery = "delete from FILM where FILMID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return true;
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortedByYearOrLikes(int id, String sortBy) {
        try {
            directorStorage.getDirectorById(id);
            String sql;
            if (sortBy.equals("year")) {
                sql = "SELECT f.FILMID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATINGID,\n" +
                        "r.NAME\n FROM FILM f \n" +
                        "JOIN RATINGMPA r ON f.RATINGID = r.RATINGID \n" +
                        "JOIN FILM_DIRECTOR fd ON f.FILMID = fd.FILM_ID \n" +
                        "WHERE fd.DIRECTOR_ID = ? " +
                        "ORDER BY RELEASEDATE ";
                return ((jdbcTemplate.query(sql, this::makeFilm, id)));
            }
            if (sortBy.equals("likes")) {
                sql = "SELECT f.FILMID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATINGID, \n" +
                        "r.NAME, count(fl.USER_ID) AS likes_quantity FROM FILM f \n" +
                        "JOIN RATINGMPA r ON f.RATINGID = r.RATINGID \n" +
                        "JOIN FILM_DIRECTOR fd ON f.FILMID = fd.FILM_ID \n" +
                        "LEFT JOIN FILM_LIKE fl ON f.FILMID = fl.FILM_ID \n" +
                        "where fd.DIRECTOR_ID = ? " +
                        "GROUP BY f.FILMID \n" +
                        "ORDER BY likes_quantity desc";
                return ((jdbcTemplate.query(sql, this::makeFilm, id)));
            }
            return new ArrayList<>();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режисер не найден.");
        }
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        String sql = "select * from LIKES where USERID = ? and FILMID = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!existLike.next()) {
            String setLike = "insert into LIKES (USERID, FILMID) values (?, ?) ";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        log.info(String.valueOf(sqlRowSet.next()));
        return sqlRowSet.next();
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        String deleteLike = "delete from LIKES where FILMID = ? and USERID = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
        return true;
    }

    public Collection<Film> getMostPopularFilms(int count) {
        String sqlMostPopular = "select count(L.LIKEID) as likeRate" +
                ",FILM.FILMID" +
                ",FILM.NAME ,FILM.DESCRIPTION ,RELEASEDATE ,DURATION ,RATE ,R.RATINGID, R.NAME, R.DESCRIPTION from FILM " +
                "left join LIKES L on L.FILMID = FILM.FILMID " +
                "inner join RATINGMPA R on R.RATINGID = FILM.RATINGID " +
                "group by FILM.FILMID " +
                "ORDER BY likeRate desc " +
                "limit ?";
        return jdbcTemplate.query(sqlMostPopular, this::makeFilm, count);
    }

    private Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int filmId = resultSet.getInt("FilmID");
        Film film = Film.builder()
                .id(resultSet.getInt("FilmID"))
                .name(resultSet.getString("Name"))
                .description(resultSet.getString("Description"))
                .releaseDate(resultSet.getDate("ReleaseDate").toLocalDate())
                .duration(resultSet.getInt("Duration"))
                .mpa(new Mpa(resultSet.getInt("RatingID"),
                        resultSet.getString("RatingMPA.Name"),
                        resultSet.getString("Description")))
                .build();
        List<Genre> genres = (List<Genre>) genreService.getFilmGenres(filmId);
        List<Integer> likes = getFilmLikes(filmId);
        List<Director> directors = directorStorage.getDirectorsByFilm(filmId);
        if (genres != null) {
            for (Genre genre : genres) {
                film.getGenres().add(genre);
            }
        }
        if (likes != null) {
            for (Integer like : likes) {
                film.getLikes().add(like);
            }
        }
        if (directors != null) {
            for (Director director : directors) {
                film.getDirectors().add(director);
            }
        }
        return film;
    }

    private List<Integer> getFilmLikes(int filmId) {
        String sqlGetLikes = "select USERID from LIKES where FILMID = ?";
        return jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
    }

}
