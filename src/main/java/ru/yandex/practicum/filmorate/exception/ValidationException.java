package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.Logger;

public class ValidationException extends RuntimeException {
    public ValidationException(final String m) {
        super(m);
        Logger.validatorLog(m);
    }

}
