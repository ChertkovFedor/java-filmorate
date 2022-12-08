package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

public class UserValidator {

    public static void userValid(User user) {
        if (user == null)
            throw new ValidationException("request body is missing");
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@"))
            throw new ValidationException("the email cannot be empty and must contain the character @");
        if (user.getLogin().isEmpty() || user.getLogin().contains(" "))
            throw new ValidationException("the login cannot be empty and contain spaces");
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("the date of birth cannot be in the future");
    }

    public static void recurringUser(Long firstId, Long secondId) {
        if (firstId.equals(secondId))
            throw new ValidationException("an operation is performed with the same user");
    }

    public static void alreadyExistIdUser(Long id, Map<Long, User> users) {
        if (users.containsKey(id))
            throw new AlreadyExistException("user with this id already exists");
    }

    public static void notFoundIdUser(Long id, Map<Long, User> users) {
        if (!users.containsKey(id))
            throw new UserNotFoundException("there is no user with this id");
    }

}
