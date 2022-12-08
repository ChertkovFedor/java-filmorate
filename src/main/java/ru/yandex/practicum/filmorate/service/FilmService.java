package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.Logger;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long id, Long userId) {
        checkingInputValues(id, userId);

        Film film = filmStorage.find(id);
        film.getLikes().add(userId);

        User user = userStorage.find(userId);
        user.getLikeFilms().add(id);

        Logger.queryResultLog("like to the film id=" + id + " added");

    }

    public void deleteLike(Long id, Long userId) {
        checkingInputValues(id, userId);

        Film film = filmStorage.find(id);
        film.getLikes().remove(userId);

        User user = userStorage.find(userId);
        user.getLikeFilms().remove(id);

        Logger.queryResultLog("like from the film id=" + id + " deleted");
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count == null)
            count = 10;
        Collection<Film> filmsValues = filmStorage.getFilms().values();
        return filmsValues.stream()
                .sorted((p0, p1) -> compare(p0, p1, Constants.DESCENDING_ORDER))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1, String sort) {
        int result = p0.getLikes().size() - p1.getLikes().size();
        if (sort.equals(Constants.DESCENDING_ORDER)) {
            result = -1 * result;
        }
        return result;
    }

    private void checkingInputValues(Long FilmId, Long userId) {
        FilmValidator.notFoundIdFilm(FilmId, filmStorage.getFilms());
        UserValidator.notFoundIdUser(userId, userStorage.getUsers());
    }

}
