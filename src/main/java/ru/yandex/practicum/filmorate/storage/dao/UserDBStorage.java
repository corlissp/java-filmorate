package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.util.List;
import java.util.Objects;

@Component("UserDBStorage")
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUserStorage(User user) {
        String sqlQuery = "insert into USERS " +
                "(EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));

            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (user.getFriends() != null) {
            for (Integer friendId : user.getFriends()) {
                addFriend(user.getId(), friendId);
            }
        }
        return getUserByIdStorage(id);
    }

    @Override
    public User updateUserStorage(User user) {
        String sqlUser = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where USERID = ?";
        jdbcTemplate.update(sqlUser,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        return getUserByIdStorage(user.getId());
    }

    @Override
    public void deleteUserStorage(int id) {
        String sqlQuery = "delete from USERS where UserId = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<User> getAllUsersStorage() {
        String sqlAllUsers = "select * from USERS";
        return jdbcTemplate.query(sqlAllUsers, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User getUserByIdStorage(int id) {
        String sqlUser = "select * from USERS where USERID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    id + " не зарегистрирован!");
        }
        return user;
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("UserID");
        return new User(
                userId,
                resultSet.getString("Email"),
                resultSet.getString("Login"),
                resultSet.getString("Name"),
                Objects.requireNonNull(resultSet.getDate("BirthDay")).toLocalDate(),
                getUserFriends(userId));
    }

    private List<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "select FRIENDID from FRIENDSHIP where USERID = ?";
        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        boolean friendAccepted;
        String sqlGetReversFriend = "select * from FRIENDSHIP " +
                "where USERID = ? and FRIENDID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);
        friendAccepted = sqlRowSet.next();
        String sqlSetFriend = "insert into FRIENDSHIP (USERID, FRIENDID, STATUS) " +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);
        if (friendAccepted) {
            String sqlSetStatus = "update FRIENDSHIP set STATUS = true " +
                    "where USERID = ? and FRIENDID = ?";
            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
        return true;
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        String sqlDeleteFriend = "delete from FRIENDSHIP where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlDeleteFriend, userId, friendId);
        String sqlSetStatus = "update FRIENDSHIP set STATUS = false " +
                "where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlSetStatus, friendId, userId);
        return true;
    }
}
