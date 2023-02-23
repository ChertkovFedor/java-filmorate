package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.List;

@Service
@Getter
public class UserService {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public List<User> findAll() {
        return userDbStorage.getUsers();
    }

    public User find(Long id) {
        userDbStorage.checkNotFoundIdUser(id);
        return userDbStorage.find(id);
    }

    public User create(User user) {
        UserValidator.userValid(user);
        userDbStorage.checkAlreadyExistIdUser(user.getId());
        return userDbStorage.create(user);
    }

    public User put(Long id, User user) {
        UserValidator.userValid(user);
        userDbStorage.checkNotFoundIdUser(id);
        return userDbStorage.update(id, user);
    }

    public User putIdInBody(User user) {
        Long id = user.getId();
        UserValidator.userValid(user);
        userDbStorage.checkNotFoundIdUser(id);
        return userDbStorage.update(id, user);
    }

    public void delete(Long id) {
        userDbStorage.checkNotFoundIdUser(id);
        userDbStorage.delete(id);
    }

    public void clear() {
        userDbStorage.clear();
    }

    public void addFriend(Long id, Long idFriend) {
        checkingInputValues(id, idFriend);
        userDbStorage.addFriend(id, idFriend);
    }

    public void deleteFriend(Long id, Long idFriend) {
        checkingInputValues(id, idFriend);
        userDbStorage.deleteFriend(id, idFriend);
    }

    public List<User> getFriends(Long id) {
        userDbStorage.checkNotFoundIdUser(id);
        return userDbStorage.getFriends(id);
    }

    public List<User> getMutualFriends(Long idFirstUser, Long idSecondUser) {
        checkingInputValues(idFirstUser, idSecondUser);
        return userDbStorage.getMutualFriends(idFirstUser, idSecondUser);
    }

    private void checkingInputValues(Long firstId, Long secondId) {
        UserValidator.recurringUser(firstId, secondId);
        userDbStorage.checkNotFoundIdUser(firstId);
        userDbStorage.checkNotFoundIdUser(secondId);
    }

}
