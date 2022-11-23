package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@RestController
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    int newId = 1;

    @GetMapping("/users")
    public Map<Integer, User> findAll() {
        return users;
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody User user) {
        addUser(user);
        return user;
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User put(@PathVariable("id") int id, @RequestBody User user) {
        updateUser(id, user);
        return user;
    }

    @PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User putIdInBody(@RequestBody User user) {
        int id = user.getId();
        updateUser(id, user);
        return user;
    }

    @DeleteMapping(value = "/users/{id}")
    public void delete(@PathVariable("id") int id) {
        users.remove(id);
        log.info("Пользователь удален");
    }

    public void addUser(User user) {
        Validator.userValid(user);
        if (user.getId() == 0) {
            user.setId(newId++);
        } else if (newId < user.getId()) {
            newId = user.getId() + 1;
        }
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
    }

    public void updateUser(int id, User user) {
        Validator.userValid(user);
        users.get(id).setUser(user);
        log.info("Пользователь обновлен");
    }

}
