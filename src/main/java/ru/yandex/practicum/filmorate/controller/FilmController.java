package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.Map;

@RestController
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(value = "/films")
    public List<Film> findAll() {
        return filmService.getFilmStorage().findAll();
    }

    @GetMapping(value = "/films/{id}")
    public Film find(@PathVariable("id") Long id) {
        return filmService.getFilmStorage().find(id);
    }

    @PostMapping(value = "/films", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film create(@RequestBody Film film) {
        return filmService.getFilmStorage().create(film);
    }

    @PutMapping(value = "/films/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film put(@PathVariable("id") Long id, @RequestBody Film film) {
        return filmService.getFilmStorage().put(id, film);
    }

    @PutMapping(value = "/films", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Film putIdInBody(@RequestBody Film film) {
        return filmService.getFilmStorage().putIdInBody(film);
    }

    @DeleteMapping(value = "/films/{id}")
    public void delete(@PathVariable("id") Long id) {
        filmService.getFilmStorage().delete(id);
    }

    @DeleteMapping(value = "/films/all")
    public void clear() {
        filmService.getFilmStorage().clear();
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

}
