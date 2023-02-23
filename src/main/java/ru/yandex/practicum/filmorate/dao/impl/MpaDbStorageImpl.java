package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorageImpl implements MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa find(Long id) {
        checkNotFoundIdMpa(id);
        String sql = """
                SELECT *
                FROM MPA
                WHERE MPA_ID = ?
                """;
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sql, id);
        if (mpaRows.next()) {
            return makeMpaSqlRowSet(mpaRows);
        } else {
            return null;
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = """
                SELECT *
                FROM MPA
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpaResultSet(rs));
    }

    @Override
    public void checkNotFoundIdMpa(Long id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE MPA_ID = ? LIMIT 1", id);
        if (!mpaRows.next())
            throw new MpaNotFoundException("there is no mpa with this id");
    }

    private Mpa makeMpaResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("MPA_ID");
        String name = rs.getString("MPA_NAME");
        return new Mpa(id, name);
    }

    private Mpa makeMpaSqlRowSet(SqlRowSet rs) {
        Long id = rs.getLong("MPA_ID");
        String name = rs.getString("MPA_NAME");
        return new Mpa(id, name);
    }

}
