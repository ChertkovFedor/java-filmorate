package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Service
@Getter
public class FilmService {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public FilmService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage, MpaDbStorage mpaDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Film> findAll() {
        return filmDbStorage.getFilms();
    }

    public Film find(Long id) {
        filmDbStorage.checkNotFoundIdFilm(id);
        return filmDbStorage.find(id);
    }

    public void delete(Long id) {
        filmDbStorage.checkNotFoundIdFilm(id);
        filmDbStorage.delete(id);
    }

    public void clear() {
        filmDbStorage.clear();
    }

    public Film create(Film film) {
        FilmValidator.filmValid(film);
        filmDbStorage.checkAlreadyExistIdFilm(film.getId());
        //mpaDbStorage.checkNotFoundIdMpa(film.getMpa().getId());
        return filmDbStorage.create(film);
    }

    public Film put(Long id, Film film) {
        FilmValidator.filmValid(film);
        filmDbStorage.checkNotFoundIdFilm(id);
        //mpaDbStorage.checkNotFoundIdMpa(film.getMpa().getId());
        return filmDbStorage.update(id, film);
    }

    public Film putIdInBody(Film film) {
        FilmValidator.filmValid(film);
        Long id = film.getId();
        filmDbStorage.checkNotFoundIdFilm(id);
        //mpaDbStorage.checkNotFoundIdMpa(film.getMpa().getId());
        return filmDbStorage.update(id, film);
    }

    public void addLike(Long id, Long userId) {
        checkingInputValues(id, userId);
        filmDbStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        checkingInputValues(id, userId);
        filmDbStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count == null)
            count = 10;
        return filmDbStorage.getPopularFilms(count);
    }

    private void checkingInputValues(Long filmId, Long userId) {
        filmDbStorage.checkNotFoundIdFilm(filmId);
        userDbStorage.checkNotFoundIdUser(userId);
    }

}
