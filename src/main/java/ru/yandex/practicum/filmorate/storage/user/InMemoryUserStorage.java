package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Logger;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.traceCleaner.UserTraceCleanerService;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long newId = 1L;

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User find(Long id) {
        UserValidator.notFoundIdUser(id, users);
        return users.get(id);
    }

    public User create(User user) {
        addUser(user);
        return user;
    }

    public User put(Long id, User user) {
        updateUser(id, user);
        return user;
    }

    public User putIdInBody(User user) {
        Long id = user.getId();
        updateUser(id, user);
        return user;
    }

    public void delete(Long id) {
        UserValidator.notFoundIdUser(id, users);
        UserTraceCleanerService.deleteUserTraces(id, users);
        users.remove(id);
        Logger.queryResultLog("user id=" + id + " deleted");
    }

    public void clear() {
        UserTraceCleanerService.clearAllUsersTraces();
        users.clear();
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    private void addUser(User user) {
        UserValidator.userValid(user);
        if (user.getId() == null) {
            user.setId(newId++);
        } else {
            UserValidator.alreadyExistIdUser(user.getId(), users);
            if (newId <= user.getId())
                newId = user.getId() + 1;
        }
        users.put(user.getId(), user);
        Logger.queryResultLog("user id=" + user.getId() + " added");
    }

    private void updateUser(Long id, User user) {
        UserValidator.userValid(user);
        UserValidator.notFoundIdUser(id, users);
        users.get(id).setUser(user);
        Logger.queryResultLog("user id=" + user.getId() + " updated");
    }

}
