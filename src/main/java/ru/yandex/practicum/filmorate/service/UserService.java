package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Logger;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id, Long idFriend) {
        checkingInputValues(id, idFriend);

        User user = userStorage.find(id);
        user.getFriends().add(idFriend);

        User userFriend = userStorage.find(idFriend);
        userFriend.getFriends().add(id);

        Logger.queryResultLog("friend id=" + idFriend + " added to user id=" + id);
    }

    public void deleteFriend(Long id, Long idFriend) {
        checkingInputValues(id, idFriend);

        User user = userStorage.find(id);
        user.getFriends().remove(idFriend);

        User userFriend = userStorage.find(idFriend);
        userFriend.getFriends().remove(id);

        Logger.queryResultLog("friend id=" + idFriend + " deleted from the user id=" + id);
    }

    public List<User> getFriends(Long id) {
        UserValidator.notFoundIdUser(id, userStorage.getUsers());
        User user = userStorage.find(id);
        List<User> friends = new ArrayList<>();
        for (Long idFriend : user.getFriends()) {
            friends.add(userStorage.find(idFriend));
        }
        return friends;
    }

    public List<User> getMutualFriends(Long idFirstUser, Long idSecondUser) {
        checkingInputValues(idFirstUser, idSecondUser);

        User firstUser = userStorage.find(idFirstUser);
        User secondUser = userStorage.find(idSecondUser);

        List<User> mutualFriends = new ArrayList<>();
        for (Long idFriendFirstUser : firstUser.getFriends()) {
            if (secondUser.getFriends().contains(idFriendFirstUser))
                mutualFriends.add(userStorage.find(idFriendFirstUser));
        }
        return mutualFriends;
    }

    private void checkingInputValues(Long firstId, Long secondId) {
        UserValidator.recurringUser(firstId, secondId);
        UserValidator.notFoundIdUser(firstId, userStorage.getUsers());
        UserValidator.notFoundIdUser(secondId, userStorage.getUsers());
    }

}
