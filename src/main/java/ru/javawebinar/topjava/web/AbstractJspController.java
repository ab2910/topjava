package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.service.UserService;

public class AbstractJspController {

    @Autowired
    protected UserService userService;
}