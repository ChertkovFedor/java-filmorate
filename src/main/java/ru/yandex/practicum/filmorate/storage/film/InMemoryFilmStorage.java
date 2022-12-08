package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Logger;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.traceCleaner.FilmTraceCleanerService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected final Map<Long, Film> films = new HashMap<>();
    private Long newId = 1L;

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public Film find(Long id) {
        FilmValidator.notFoundIdFilm(id, films);
        return films.get(id);
    }

    public Film create(Film film) {
        addFilm(film);
        return film;
    }

    public Film put(Long id, Film film) {
        updateFilm(id, film);
        return film;
    }

    public Film putIdInBody(Film film) {
        Long id = film.getId();
        updateFilm(id, film);
        return film;
    }

    public void delete(Long id) {
        FilmValidator.notFoundIdFilm(id, films);
        FilmTraceCleanerService.deleteFilmTraces(films.get(id));
        films.remove(id);
        Logger.queryResultLog("film id=" + id + " deleted");
    }

    public void clear() {
        FilmTraceCleanerService.clearAllFilmsTraces();
        films.clear();
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    private void addFilm(Film film) {
        FilmValidator.filmValid(film);
        if (film.getId() == null) {
            film.setId(newId++);
        } else {
            FilmValidator.alreadyExistIdFilm(film.getId(), films);
            if (newId <= film.getId())
                newId = film.getId() + 1;
        }
        films.put(film.getId(), film);
        Logger.queryResultLog("film id=" + film.getId() + " added");
    }

    private void updateFilm(Long id, Film film) {
        FilmValidator.filmValid(film);
        FilmValidator.notFoundIdFilm(id, films);
        films.get(id).setFilm(film);
        Logger.queryResultLog("film id=" + film.getId() + " updated");
    }

}
