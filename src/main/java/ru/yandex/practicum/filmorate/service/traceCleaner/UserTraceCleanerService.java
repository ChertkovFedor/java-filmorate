package ru.yandex.practicum.filmorate.service.traceCleaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Map;

@Service
public class UserTraceCleanerService {

    private static FilmStorage filmStorage;

    @Autowired
    public UserTraceCleanerService(FilmStorage filmStorage) {
        UserTraceCleanerService.filmStorage = filmStorage;
    }

    public static void clearAllUsersTraces() {
        for (Map.Entry<Long, Film> films : filmStorage.getFilms().entrySet()) {
            Film film = films.getValue();
            film.getLikes().clear();
        }
    }

    public static void deleteUserTraces(Long id, Map<Long, User> users) {
        User user = users.get(id);
        for (Long idFriend : user.getFriends()) {
            User friend = users.get(idFriend);
            friend.getFriends().remove(id);
        }
        for (Long idFilm : user.getLikeFilms()) {
            Film film = filmStorage.find(idFilm);
            film.getLikes().remove(id);
        }
    }

}
