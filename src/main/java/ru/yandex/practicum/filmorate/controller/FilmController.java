package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private List<Film> films = new ArrayList<>();
    int newId = 1;

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        return films;
    }

    @GetMapping(value = "/films/{id}")
    public Film find(@PathVariable("id") int id) {
        for (Film film:films) {
            if (film.getId() == id) {
                return film;
            }
        }
        Validator.notFoundId();
        return null;
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
        for (Film film:films) {
            if (film.getId() == id) {
                films.remove(film);
                log.info("Фильм удален");
                return;
            }
        }
        Validator.notFoundId();
    }

    public void addFilm(Film film) {
        Validator.filmValid(film);
        if (film.getId() == 0) {
            film.setId(newId++);
        } else if (newId <= film.getId()) {
            newId = film.getId() + 1;
        }
        films.add(film);
        log.info("Фильм добавлен");
    }

    public void updateFilm(int id, Film film) {
        Validator.filmValid(film);
        for (Film oldFilm:films) {
            if (oldFilm.getId() == id) {
                oldFilm.setFilm(film);
                log.info("Фильм обновлен");
                return;
            }
        }
        Validator.notFoundId();
    }

}
