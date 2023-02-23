package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDbStorage {

    void addLike(Long id, Long userId);
    void deleteLike(Long id, Long userId);
    List<Film> getPopularFilms(Integer count);
    List<Film> getFilms();

    void checkNotFoundIdFilm(Long id);
    void checkAlreadyExistIdFilm(Long id);

    Film create(Film film);

    Film update(Long id, Film film);

    Film find(Long id);

    void delete(Long id);

    void clear();
}
