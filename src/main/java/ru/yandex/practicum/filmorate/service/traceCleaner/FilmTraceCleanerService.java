package ru.yandex.practicum.filmorate.service.traceCleaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Map;

@Service
public class FilmTraceCleanerService {

    private static UserStorage userStorage;

    @Autowired
    public FilmTraceCleanerService(UserStorage userStorage) {
        FilmTraceCleanerService.userStorage = userStorage;
    }

    public static void clearAllFilmsTraces() {
        for (Map.Entry<Long, User> users : userStorage.getUsers().entrySet()) {
            User user = users.getValue();
            user.getLikeFilms().clear();
        }
    }

    public static void deleteFilmTraces(Film film) {
        for (Long idUser : film.getLikes()) {
            User user = userStorage.find(idUser);
            user.getLikeFilms().remove(film.getId());
        }
    }

}
