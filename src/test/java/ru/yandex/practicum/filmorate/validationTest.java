package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class validationTest {

    FilmController filmController;
    UserController userController;

    @BeforeEach
    public void newControllers() {
        filmController = new FilmController();
        userController = new UserController();
    }

    @Test
    public void validationFilmTest() {
        Film film1 = getNewFilm(1, "", "Description", LocalDate.of(2020, 10, 10), 300);
        Film film2 = getNewFilm(1, "Film",
                "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789",
                LocalDate.of(2020, 10, 10), 300);
        Film film3 = getNewFilm(1, "Film", "Description", LocalDate.of(1800, 10, 10), 300);
        Film film4 = getNewFilm(1, "Film", "Description", LocalDate.of(2020, 10, 10), -300);
        Film film5 = null;

        ValidationException e1 = assertThrows(ValidationException.class,
                () -> filmController.addFilm(film1));
        ValidationException e2 = assertThrows(ValidationException.class,
                () -> filmController.addFilm(film2));
        ValidationException e3 = assertThrows(ValidationException.class,
                () -> filmController.addFilm(film3));
        ValidationException e4 = assertThrows(ValidationException.class,
                () -> filmController.addFilm(film4));
        ValidationException e5 = assertThrows(ValidationException.class,
                () -> filmController.addFilm(film5));

        assertEquals("the name cannot be empty", e1.getMessage());
        assertEquals("the maximum length of the description is 200 characters", e2.getMessage());
        assertEquals("release date - no earlier than December 28, 1895", e3.getMessage());
        assertEquals("the duration of the film should be positive", e4.getMessage());
        assertEquals("request body is missing", e5.getMessage());
    }

    @Test
    public void validationUserTest() {
        User user1 = getNewUser(1, "", "User", "Username", LocalDate.of(2000, 10, 10));
        User user2 = getNewUser(1, "usertest", "User", "Username", LocalDate.of(2000, 10, 10));
        User user3 = getNewUser(1, "user@test", "", "Username", LocalDate.of(2000, 10, 10));
        User user4 = getNewUser(1, "user@test", "Us er", "Username", LocalDate.of(2000, 10, 10));
        User user5 = getNewUser(1, "user@test", "User", "Username", LocalDate.of(2200, 10, 10));
        User user6 = null;

        ValidationException e1 = assertThrows(ValidationException.class,
                () -> userController.addUser(user1));
        ValidationException e2 = assertThrows(ValidationException.class,
                () -> userController.addUser(user2));
        ValidationException e3 = assertThrows(ValidationException.class,
                () -> userController.addUser(user3));
        ValidationException e4 = assertThrows(ValidationException.class,
                () -> userController.addUser(user4));
        ValidationException e5 = assertThrows(ValidationException.class,
                () -> userController.addUser(user5));
        ValidationException e6 = assertThrows(ValidationException.class,
                () -> userController.addUser(user6));

        assertEquals("the email cannot be empty and must contain the character @", e1.getMessage());
        assertEquals("the email cannot be empty and must contain the character @", e2.getMessage());
        assertEquals("the login cannot be empty and contain spaces", e3.getMessage());
        assertEquals("the login cannot be empty and contain spaces", e4.getMessage());
        assertEquals("the date of birth cannot be in the future", e5.getMessage());
        assertEquals("request body is missing", e6.getMessage());
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
