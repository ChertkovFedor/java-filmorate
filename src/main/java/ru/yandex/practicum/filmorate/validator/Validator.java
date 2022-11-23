package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {

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

    public static void userValid(User user) {
        if (user == null)
            throw new ValidationException("request body is missing");
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@"))
            throw new ValidationException("the email cannot be empty and must contain the character @");
        if (user.getLogin().isEmpty() || user.getLogin().contains(" "))
            throw new ValidationException("the login cannot be empty and contain spaces");
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("the date of birth cannot be in the future");
    }

}
