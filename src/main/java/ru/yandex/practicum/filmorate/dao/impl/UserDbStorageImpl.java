package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Logger;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class UserDbStorageImpl implements UserDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User find(Long id) {
        String sql = """
                SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY
                FROM USERS
                WHERE USER_ID = ?
                """;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            return makeUserSqlRowSet(userRows);
        } else {
            return null;
        }
    }

    @Override
    public User create(User user) {
        Long id;
        if (user.getId() == null) {
            String sql = """
                    INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY)
                    VALUES (?, ?, ?, ?)
                    """;
            jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                    user.getName(), user.getBirthday());
            id = jdbcTemplate.queryForObject("SELECT MAX(USER_ID) FROM USERS", Long.class);
        } else {
            String sql = """
                    INSERT INTO USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY)
                    VALUES (?, ?, ?, ?, ?)
                    """;
            jdbcTemplate.update(sql, user.getId(), user.getEmail(), user.getLogin(),
                    user.getName(), user.getBirthday());
            id = user.getId();

        }

        Logger.queryResultLog("user id=" + id + " added");

        return find(id);
    }

    @Override
    public User update(Long id, User user) {
        String sql = """
                UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?
                WHERE USER_ID = ?
                """;
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), id);

        Logger.queryResultLog("user id=" + id + " updated");

        return find(id);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);

        Logger.queryResultLog("user id=" + id + " deleted");
    }

    @Override
    public void clear() {
        String sql = """
        DELETE FROM USERS;
        ALTER TABLE USERS DROP COLUMN USER_ID;
        ALTER TABLE USERS ADD COLUMN USER_ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;
        """;
        jdbcTemplate.update(sql);
    }

    @Override
    public List<User> getUsers() {
        String sql = """
                SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY
                FROM USERS
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUserResultSet(rs));
    }

    @Override
    public void addFriend(Long id, Long idFriend) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ? LIMIT 1", id, idFriend);
        if (!friendRows.next()) {
            SqlRowSet friendRequestRows = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ? LIMIT 1", idFriend, id);
            if (!friendRequestRows.next()) {
                jdbcTemplate.update("""
                        INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS)
                        VALUES (?, ?, ?)
                        """, id, idFriend, false);
                Logger.queryResultLog("friend id=" + idFriend + " added to user id=" + id);
            } else {
                boolean status = friendRequestRows.getBoolean("STATUS");
                if (!status) {
                    jdbcTemplate.update("UPDATE FRIENDS SET STATUS = ?" +
                            "WHERE USER_ID = ? AND FRIEND_ID = ?", true, idFriend, id);
                    Logger.queryResultLog("friend id=" + idFriend + " added to user id=" + id);
                }
            }
        }
    }

    @Override
    public void deleteFriend(Long id, Long idFriend) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ? LIMIT 1", id, idFriend);
        if (friendRows.next()) {
            jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", id, idFriend);
            Logger.queryResultLog("friend id=" + idFriend + " deleted from the user id=" + id);
        } else {
            SqlRowSet friendRequestRows = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ? LIMIT 1", idFriend, id);
            if (friendRequestRows.next()) {
                jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", idFriend, id);
                Logger.queryResultLog("friend id=" + idFriend + " deleted from the user id=" + id);
            }
        }
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = """
                SELECT u.USER_ID, u.EMAIL, u.LOGIN, u.USER_NAME, u.BIRTHDAY
                FROM FRIENDS AS f
                JOIN USERS AS u ON f.FRIEND_ID = u.USER_ID
                WHERE f.USER_ID = ?
                UNION
                SELECT u.USER_ID, u.EMAIL, u.LOGIN, u.USER_NAME, u.BIRTHDAY
                FROM FRIENDS AS f
                JOIN USERS AS u ON f.USER_ID = u.USER_ID
                WHERE f.FRIEND_ID = ? AND f.STATUS = true
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUserResultSet(rs), id, id);
    }

    @Override
    public List<User> getMutualFriends(Long idFirstUser, Long idSecondUser) {
        String sql = """
                SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY
                FROM USERS
                WHERE USER_ID IN (SELECT USER_ID
                                  FROM FRIENDS
                                  WHERE USER_ID IN (SELECT FRIEND_ID
                                                    FROM FRIENDS
                                                    WHERE (USER_ID = ?) AND FRIEND_ID != ?
                                                    UNION
                                                    SELECT USER_ID
                                                    FROM FRIENDS
                                                    WHERE (FRIEND_ID = ? AND STATUS = true) AND USER_ID != ?)
                                  AND STATUS = true AND FRIEND_ID = ?
                                  UNION
                                  SELECT FRIEND_ID
                                  FROM FRIENDS
                                  WHERE FRIEND_ID IN (SELECT FRIEND_ID
                                                      FROM FRIENDS
                                                      WHERE (USER_ID = ?) AND FRIEND_ID != ?
                                                      UNION
                                                      SELECT USER_ID
                                                      FROM FRIENDS
                                                      WHERE (FRIEND_ID = ? AND STATUS = true) AND USER_ID != ?)
                                  AND USER_ID = ?)
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUserResultSet(rs), idFirstUser, idSecondUser, idFirstUser, idSecondUser, idSecondUser, idFirstUser, idSecondUser, idFirstUser, idSecondUser, idSecondUser);
    }

    @Override
    public void checkAlreadyExistIdUser(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ? LIMIT 1", id);
        if (userRows.next())
            throw new AlreadyExistException("user with this id already exists");
    }

    @Override
    public void checkNotFoundIdUser(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ? LIMIT 1", id);
        if (!userRows.next())
            throw new UserNotFoundException("there is no user with this id");
    }

    private User makeUserResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("USER_NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

    private User makeUserSqlRowSet(SqlRowSet rs) {
        Long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("USER_NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

}
