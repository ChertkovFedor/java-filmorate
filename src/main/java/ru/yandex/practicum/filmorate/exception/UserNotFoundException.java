package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.Logger;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String m) {
        super(m);
        Logger.validatorLog(m);
    }
}
