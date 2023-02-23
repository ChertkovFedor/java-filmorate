package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private long rate;
    private List<Genre> genres;
    private Mpa mpa;

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, long rate, List<Genre> genres, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genres = genres;
        this.mpa = mpa;
    }
}
