package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    List<User> findAll();

    Map<Long, User> getUsers();

    User find(Long id);

    User create(User user);

    User put(Long id, User user);

    User putIdInBody(User user);

    void delete(Long id);

    void clear();

}
