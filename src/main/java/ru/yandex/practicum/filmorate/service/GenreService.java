package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Getter
public class GenreService {

    private final GenreDbStorage genreDbStorage;

    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> findAll() {
        return genreDbStorage.getGenres();
    }

    public Genre find(Long id) {
        return genreDbStorage.find(id);
    }

}
