package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@RestController
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();
    int newId = 1;

    @GetMapping("/films")
    public Map<Integer, Film> findAll() {
        return films;
    }

    @PostMapping(value = "/films", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film create(@RequestBody Film film) {
        addFilm(film);
        return film;
    }

    @PutMapping(value = "/films/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film put(@PathVariable("id") int id, @RequestBody Film film) {
        updateFilm(id, film);
        return film;
    }

    @PutMapping(value = "/films", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film putIdInBody(@RequestBody Film film) {
        int id = film.getId();
        updateFilm(id, film);
        return film;
    }

    @DeleteMapping(value = "/films/{id}")
    public void delete(@PathVariable("id") int id) {
        films.remove(id);
        log.info("Фильм удален");
    }

    public void addFilm(Film film) {
        Validator.filmValid(film);
        if (film.getId() == 0) {
            film.setId(newId++);
        } else if (newId < film.getId()) {
            newId = film.getId() + 1;
        }
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
    }

    public void updateFilm(int id, Film film) {
        Validator.filmValid(film);
        films.get(id).setFilm(film);
        log.info("Фильм обновлен");
    }

}
