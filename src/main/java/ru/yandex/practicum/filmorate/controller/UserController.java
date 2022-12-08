package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    private final UserService userService;

    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @GetMapping(value = "/users/{id}")
    public User find(@PathVariable("id") Long id) {
        return inMemoryUserStorage.find(id);
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User put(@PathVariable("id") Long id, @RequestBody User user) {
        return inMemoryUserStorage.put(id, user);
    }

    @PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User putIdInBody(@RequestBody User user) {
        return inMemoryUserStorage.putIdInBody(user);
    }

    @DeleteMapping(value = "/users/{id}")
    public void delete(@PathVariable("id") Long id) {
        inMemoryUserStorage.delete(id);
    }

    @DeleteMapping(value = "/users/all")
    public void clear() {
        inMemoryUserStorage.clear();
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long id) {
        return userService.getFriends(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addLike(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
