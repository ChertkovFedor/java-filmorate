package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {

    public static void validatorLog(String s) {
        log.info("Validation failed: " + s);
    }

    public static void queryResultLog(String s) {
        log.info(s);
    }

}
