package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Map;

public class FilmValidator {

    public static void filmValid(Film film) {
        if (film == null)
            throw new ValidationException("request body is missing");
        if (film.getName() == null || film.getName().isEmpty())
            throw new ValidationException("the name cannot be empty");
        if (film.getDescription().length() > 200)
            throw new ValidationException("the maximum length of the description is 200 characters");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("release date - no earlier than December 28, 1895");
        if (film.getDuration() <= 0)
            throw new ValidationException("the duration of the film should be positive");
    }

    public static void alreadyExistIdFilm(Long id, Map<Long, Film> films) {
        if (films.containsKey(id))
            throw new AlreadyExistException("film with this id already exists");
    }

    public static void notFoundIdFilm(Long id, Map<Long, Film> films) {
        if (!films.containsKey(id))
            throw new FilmNotFoundException("there is no film with this id");
    }

}
