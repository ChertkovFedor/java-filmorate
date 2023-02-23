package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Logger;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDbStorageImpl implements FilmDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Film find(Long id) {
        String sql = """
                SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATE, m.MPA_ID
                FROM FILMS AS f
                JOIN MPA AS m ON f.MPA_ID = m.MPA_ID
                WHERE f.FILM_ID = ?
                """;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (filmRows.next()) {
            return makeFilmSqlRowSet(filmRows);
        } else {
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);

        Logger.queryResultLog("film id=" + id + " deleted");
    }

    @Override
    public void clear() {
        String sql = """
        DELETE FROM FILMS;
        ALTER TABLE FILMS DROP COLUMN FILM_ID;
        ALTER TABLE FILMS ADD COLUMN FILM_ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;
        """;

        jdbcTemplate.update(sql);
    }

    @Override
    public Film create(Film film) {
        Long mpaId = film.getMpa().getId();
        Long id;
        if (film.getId() == null) {
            String sql = """
                    INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA_ID)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;
            jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getRate(), mpaId);
            id = jdbcTemplate.queryForObject("SELECT MAX(FILM_ID) FROM FILMS", Long.class);
        } else {
            String sql = """
                    INSERT INTO FILMS (FILM_ID, FILM_NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA_ID)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;
            jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getRate(), mpaId);
            id = film.getId();
        }
        genreDbStorage.addGenresByFilm(id, film);

        Logger.queryResultLog("film id=" + id + " added");

        return find(id);
    }

    @Override
    public Film update(Long id, Film film) {
        Long mpaId = film.getMpa().getId();

        String sql = "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ?, MPA_ID = ?" +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRate(), mpaId, id);

        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", id);
        genreDbStorage.addGenresByFilm(id, film);

        Logger.queryResultLog("film id=" + id + " updated");

        return find(id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM LIKES WHERE FILM_ID = ? AND USER_ID = ? LIMIT 1", filmId, userId);
        if (!likeRows.next()) {
            String sql = """
                    INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?);
                    UPDATE FILMS SET RATE = RATE + 1 WHERE FILM_ID = ?
                    """;
            jdbcTemplate.update(sql, filmId, userId, filmId);


            Logger.queryResultLog("like to the film filmId=" + filmId + " added");
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT * FROM LIKES WHERE FILM_ID = ? AND USER_ID = ? LIMIT 1", filmId, userId);
        if (likeRows.next()) {
            String sql = """
                    DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?;
                    UPDATE FILMS SET RATE = RATE - 1 WHERE FILM_ID = ?
                    """;
            jdbcTemplate.update(sql, filmId, userId, filmId);

            Logger.queryResultLog("like from the film filmId=" + filmId + " deleted");
        }

    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = """
                SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATE, m.MPA_ID
                FROM FILMS AS f
                JOIN MPA AS m ON f.MPA_ID = m.MPA_ID
                ORDER BY f.RATE DESC
                LIMIT ?""";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmResultSet(rs), count);
    }

    @Override
    public List<Film> getFilms() {
        String sql = """
                SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATE, m.MPA_ID
                FROM FILMS AS f
                JOIN MPA AS m ON f.MPA_ID = m.MPA_ID
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmResultSet(rs));
    }

    @Override
    public void checkNotFoundIdFilm(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ? LIMIT 1", id);
        if (!filmRows.next())
            throw new FilmNotFoundException("there is no film with this id");
    }

    @Override
    public void checkAlreadyExistIdFilm(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ? LIMIT 1", id);
        if (filmRows.next())
            throw new AlreadyExistException("film with this id already exists");
    }

    private Film makeFilmResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASEDATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        long rate = rs.getLong("RATE");
        List<Genre> genres = genreDbStorage.getGenresByFilmId(id);
        Mpa mpa = mpaDbStorage.find((long) rs.getInt("MPA_ID"));

        return new Film(id, name, description, releaseDate, duration, rate, genres, mpa);
    }

    private Film makeFilmSqlRowSet(SqlRowSet rs) {
        Long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASEDATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        long rate = rs.getLong("RATE");
        List<Genre> genres = genreDbStorage.getGenresByFilmId(id);
        Mpa mpa = mpaDbStorage.find((long) rs.getInt("MPA_ID"));

        return new Film(id, name, description, releaseDate, duration, rate, genres, mpa);
    }
}