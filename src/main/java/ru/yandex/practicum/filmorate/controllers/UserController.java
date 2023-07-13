package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

/**
 * @author Min Danil 06.07.2023
 */

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUserService(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUserService(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsersService();
    }
}
