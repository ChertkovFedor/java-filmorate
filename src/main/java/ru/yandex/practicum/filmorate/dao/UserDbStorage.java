package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDbStorage {

    List<User> getUsers();

    User find(Long id);

    User create(User user);

    User update(Long id, User user);

    void delete(Long id);

    void clear();

    void addFriend(Long id, Long idFriend);

    void deleteFriend(Long id, Long idFriend);

    List<User> getFriends(Long id);

    List<User> getMutualFriends(Long idFirstUser, Long idSecondUser);

    void checkAlreadyExistIdUser(Long id);

    void checkNotFoundIdUser(Long id);
}
