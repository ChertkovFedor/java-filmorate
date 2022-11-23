package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String  name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public void setFilm(Film film) {
        this.setId(film.getId());
        this.setName(film.getName());
        this.setDescription(film.getDescription());
        this.setReleaseDate(film.getReleaseDate());
        this.setDuration(film.getDuration());
    }
}
