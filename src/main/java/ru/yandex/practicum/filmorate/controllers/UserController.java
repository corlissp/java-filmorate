package ru.yandex.practicum.filmorate.controllers;

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

    @PostMapping
    public User createUser(@RequestBody User user) {
        return UserService.createUserService(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return UserService.updateUserService(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return UserService.getAllUsersService();
    }
}
