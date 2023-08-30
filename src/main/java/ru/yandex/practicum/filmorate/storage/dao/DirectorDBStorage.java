package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.models.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author Min Danil 29.08.2023
 */
@Component
@Slf4j
public class DirectorDBStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT * FROM DIRECTOR;";
        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    @Override
    public Director getDirectorById(int id) {
        try {
            String sql = "SELECT * FROM DIRECTOR WHERE DIRECTOR_ID = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToDirector, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режисер с идентификатором " +
                    id + " не зарегистрирован!");
        }
    }

    @Override
    public Director addDirector(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO DIRECTOR (DIRECTOR_NAME) VALUES (?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"DIRECTOR_ID"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId((Objects.requireNonNull(keyHolder.getKey()).intValue()));
        return getDirectorById(director.getId());
    }

    @Override
    public Director updateDirector(Director director) {
        try {
            String sql = "UPDATE DIRECTOR SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
            jdbcTemplate.update(sql, director.getName(), director.getId());
            return director;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режисер с идентификатором " +
                    director.getId() + " не зарегистрирован!");
        }
    }

    @Override
    public void deleteDirector(int id) {
        try {
            String sql = "DELETE DIRECTOR WHERE DIRECTOR_ID = ?";
            jdbcTemplate.update(sql, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режисер с идентификатором " +
                    id + " не зарегистрирован!");
        }
    }

    @Override
    public List<Director> getDirectorsByFilm(int filmId) {
        String sql = "SELECT d.DIRECTOR_ID, d.DIRECTOR_NAME " +
                "FROM FILM_DIRECTOR AS fd JOIN DIRECTOR AS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
                "WHERE fd.FILM_ID = ?";
        return jdbcTemplate.query(sql, this::mapRowToDirector, filmId);
    }

    private Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getInt("DIRECTOR_ID"), rs.getString("DIRECTOR_NAME"));
    }
}
