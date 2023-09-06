package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

/**
 * @author Min Danil 06.09.2023
 */

@Component
public class LikeDBStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long film, long user) {
        String sqlQuery = "insert into LIKES (filmid, userid) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                film,
                user);
    }

    @Override
    public void deleteLike(long film, long user) {
        String sqlQuery = "delete FROM LIKES where filmid = ? and userid = ?";
        jdbcTemplate.update(sqlQuery, film, user);
    }

}
