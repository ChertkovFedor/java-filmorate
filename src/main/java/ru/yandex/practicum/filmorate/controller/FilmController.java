package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    protected final Map<Integer, Film> films = new HashMap<>();
    private int newId = 1;

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @GetMapping(value = "/films/{id}")
    public Film find(@PathVariable("id") int id) {
        if (films.containsKey(id)) {
            return films.get(id);
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
        if (films.containsKey(id)) {
            films.remove(id);
            log.info("Фильм id="+id+" удален");
            return;
        }
        Validator.notFoundId();
    }

    public void addFilm(Film film) {
        Validator.filmValid(film);
        if (film.getId() == null) {
            film.setId(newId++);
        } else if (newId <= film.getId()) {
            newId = film.getId() + 1;
        }
        films.put(film.getId(), film);
        log.info("Фильм id="+film.getId()+" добавлен");
    }

    public void updateFilm(int id, Film film) {
        Validator.filmValid(film);
        if (films.containsKey(id)) {
            films.get(id).setFilm(film);
            log.info("Фильм id="+film.getId()+" обновлен");
            return;
        }
        Validator.notFoundId();
    }

}
