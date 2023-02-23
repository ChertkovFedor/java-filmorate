package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.Logger;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String m) {
        super(m);
        Logger.validatorLog(m);
    }
}
