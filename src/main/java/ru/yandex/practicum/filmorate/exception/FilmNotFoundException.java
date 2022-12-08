package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.Logger;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String m) {
        super(m);
        Logger.validatorLog(m);
    }
}
