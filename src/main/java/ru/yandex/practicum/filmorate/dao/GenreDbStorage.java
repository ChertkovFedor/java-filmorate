package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDbStorage {

    List<Genre> getGenres();

    Genre find(Long id);

    void addGenresByFilm(Long filmId, Film film);

    List<Genre> getGenresByFilmId(Long filmId);

    void checkNotFoundIdGenre(Long id);
}
