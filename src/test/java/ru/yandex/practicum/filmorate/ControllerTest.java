package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class ControllerTest {

    private FilmController filmController;
    private UserController userController;

    @BeforeEach
    public void newControllers() {
        filmController = new FilmController();
        userController = new UserController();
    }

    @Test
    public void addFilmsTest() {
        Film film1 = getNewFilm(1, "Film", "Description", LocalDate.of(2020, 10, 10), 300);
        Film film2 = getNewFilm(2, "Film", "Description", LocalDate.of(2020, 10, 10), 300);
        filmController.create(film1);
        filmController.create(film2);

        assertTrue(filmController.findAll().contains(film1));
        assertTrue(filmController.findAll().contains(film2));
        assertEquals(filmController.find(1), film1);
        assertEquals(filmController.find(2), film2);
    }

    @Test
    public void addUsersTest() {
        User user1 = getNewUser(1, "user@test", "User", "Username", LocalDate.of(2000, 10, 10));
        User user2 = getNewUser(2, "user2@test", "User2", "Username2", LocalDate.of(2000, 10, 10));
        userController.create(user1);
        userController.create(user2);

        assertTrue(userController.findAll().contains(user1));
        assertTrue(userController.findAll().contains(user2));
        assertEquals(userController.find(1), user1);
        assertEquals(userController.find(2), user2);
    }

    @Test
    public void updateFilmTest() {
        Film film = getNewFilm(1, "Film", "Description", LocalDate.of(2020, 10, 10), 300);
        filmController.create(film);

        Film newFilm = getNewFilm(1, "newFilm", "Description", LocalDate.of(2020, 10, 10), 300);
        filmController.put(1, newFilm);

        assertEquals(filmController.find(1).getName(), "newFilm");
    }

    @Test
    public void updateUserTest() {
        User user = getNewUser(1, "user@test", "User", "Username", LocalDate.of(2000, 10, 10));
        userController.create(user);

        User newUser = getNewUser(1, "user@test", "newUser", "Username", LocalDate.of(2000, 10, 10));
        userController.put(1, newUser);

        assertEquals(userController.find(1).getLogin(), "newUser");
    }

    @Test
    public void deleteFilmTest() {
        Film film = getNewFilm(1, "Film", "Description", LocalDate.of(2020, 10, 10), 300);
        filmController.create(film);
        filmController.delete(1);

        assertEquals(filmController.findAll().size(), 0);
    }

    @Test
    public void deleteUserTest() {
        User user = getNewUser(1, "user@test", "User", "Username", LocalDate.of(2000, 10, 10));
        userController.create(user);
        userController.delete(1);

        assertEquals(userController.findAll().size(), 0);
    }

    private Film getNewFilm(int id, String name, String description, LocalDate releaseDate, int duration) {
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        return film;
    }

    private User getNewUser(int id, String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }

}
