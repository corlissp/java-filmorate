package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.models.feed.Event;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author Min Danil 06.07.2023
 */

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public UserController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUserService(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUserService(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUserService(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsersService();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}")
    public User getUser(@Valid @PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getUserCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getUserCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getFeed(@Valid @PathVariable int id) {
        return eventService.getFeed(id);
    }

    @GetMapping("/{userId}/recommendations")
    public List<Film> getRecommendations(@Valid @PathVariable int userId) {
        return userService.getRecommendations(userId);
    }
}