package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Logger;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorageImpl implements GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre find(Long id) {
        checkNotFoundIdGenre(id);
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, id);
        if (genreRows.next()) {
            return makeGenreSqlRowSet(genreRows);
        } else {
            return null;
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenreResultSet(rs));
    }

    @Override
    public void addGenresByFilm(Long filmId, Film film) {
        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres();
            for (Genre genre : genres) {
                Long genreId = genre.getId();
                checkNotFoundIdGenre(genreId);
                SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILM_GENRES WHERE FILM_ID = ? AND GENRE_ID = ? LIMIT 1", filmId, genreId);
                if (!genreRows.next()) {
                    jdbcTemplate.update("INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) " +
                            "VALUES (?, ?)", filmId, genreId);
                }
            }
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        String sql = "SELECT g.GENRE_ID, g.GENRE_NAME " +
                "FROM FILM_GENRES AS fg " +
                "JOIN GENRES AS g ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE fg.FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenreResultSet(rs), filmId);
    }

    @Override
    public void checkNotFoundIdGenre(Long id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ? LIMIT 1", id);
        if (!genreRows.next())
            throw new MpaNotFoundException("there is no genre with this id");
    }

    private Genre makeGenreResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("GENRE_ID");
        String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }

    private Genre makeGenreSqlRowSet(SqlRowSet rs) {
        Long id = rs.getLong("GENRE_ID");
        String name = rs.getString("GENRE_NAME");
        return new Genre(id, name);
    }

}
