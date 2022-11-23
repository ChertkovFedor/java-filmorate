package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private List<User> users = new ArrayList<>();
    int newId = 1;

    @GetMapping("/users")
    public List<User> findAll() {
        return users;
    }

    @GetMapping(value = "/users/{id}")
    public User find(@PathVariable("id") int id) {
        for (User user:users) {
            if (user.getId() == id) {
                return user;
            }
        }
        Validator.notFoundId();
        return null;
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
        for (User user:users) {
            if (user.getId() == id) {
                users.remove(user);
                log.info("Пользователь удален");
                return;
            }
        }
        Validator.notFoundId();
    }

    public void addUser(User user) {
        Validator.userValid(user);
        if (user.getId() == 0) {
            user.setId(newId++);
        } else if (newId <= user.getId()) {
            newId = user.getId() + 1;
        }
        users.add(user);
        log.info("Пользователь добавлен");
    }

    public void updateUser(int id, User user) {
        Validator.userValid(user);
        for (User oldUser:users) {
            if (oldUser.getId() == id) {
                oldUser.setUser(user);
                log.info("Пользователь обновлен");
                return;
            }
        }
        Validator.notFoundId();
    }

}
