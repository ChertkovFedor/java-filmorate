package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int newId = 1;

    @GetMapping("/users")
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @GetMapping(value = "/users/{id}")
    public User find(@PathVariable("id") int id) {
        if (users.containsKey(id)) {
            return users.get(id);
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
            if (users.containsKey(id)) {
                users.remove(id);
                log.info("Пользователь id="+id+" удален");
                return;
            }
        Validator.notFoundId();
    }

    public void addUser(User user) {
        Validator.userValid(user);
        if (user.getId() == null) {
            user.setId(newId++);
        } else if (newId <= user.getId()) {
            newId = user.getId() + 1;
        }
        users.put(user.getId(), user);
        log.info("Пользователь id="+user.getId()+" добавлен");
    }

    public void updateUser(int id, User user) {
        Validator.userValid(user);
            if (users.containsKey(id)) {
                users.get(id).setUser(user);
                log.info("Пользователь id="+user.getId()+" обновлен");
                return;
            }
        Validator.notFoundId();
    }

}
