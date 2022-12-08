package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Film {
    private Long id;
    private String  name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likes = new HashSet<>();

    public void setFilm(Film film) {
        this.setId(film.getId());
        this.setName(film.getName());
        this.setDescription(film.getDescription());
        this.setReleaseDate(film.getReleaseDate());
        this.setDuration(film.getDuration());
    }
}
