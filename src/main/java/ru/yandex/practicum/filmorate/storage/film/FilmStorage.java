package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    List<Film> findAll();

    Map<Long, Film> getFilms();
    Film find(Long id);

    Film create(Film film);

    Film put(Long id,Film film);

    Film putIdInBody(Film film);

    void delete(Long id);

    void clear();
}
