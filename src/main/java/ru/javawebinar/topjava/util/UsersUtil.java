package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User("user", "e@ma.il", "password", Role.USER),
            new User("User", "e1@ma.il", "password", Role.USER),
            new User("an user", "e2@ma.il", "password", Role.USER),
            new User("user an", "e3@ma.il", "password", Role.USER),
            new User("super user", "e4@ma.il", "password", Role.USER),
            new User("God", "e5@ma.il", "password", Role.USER, Role.ADMIN),
            new User("user1", "e6@ma.il", "password", Role.USER),
            new User("user2", "e7@ma.il", "password", Role.USER),
            new User("user2", "e9@ma.il", "password", Role.USER),
            new User("user2", "e8@ma.il", "password", Role.USER)
    );
}
