package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void setUser(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setLogin(user.getLogin());
        this.setName(user.getName());
        this.setBirthday(user.getBirthday());
    }
}
