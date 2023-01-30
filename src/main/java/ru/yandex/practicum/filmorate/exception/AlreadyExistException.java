package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.Logger;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String m) {
        super(m);
        Logger.validatorLog(m);
    }
}