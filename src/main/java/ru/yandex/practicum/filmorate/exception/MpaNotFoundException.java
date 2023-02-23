package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.Logger;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(String m) {
        super(m);
        Logger.validatorLog(m);
    }
}
