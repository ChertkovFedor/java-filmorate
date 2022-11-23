package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public void setUser(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setLogin(user.getLogin());
        this.setName(user.getName());
        this.setBirthday(user.getBirthday());
    }
}
