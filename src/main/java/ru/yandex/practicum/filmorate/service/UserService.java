package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.models.feed.EventOperation;
import ru.yandex.practicum.filmorate.models.feed.EventType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Min Danil 13.07.2023
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("UserDBStorage")
    private final UserStorage userStorage;
    private final EventService eventService;
    private final FilmStorage filmStorage;

    public User createUserService(User user) {
        checkValidationUser(user);
        return userStorage.createUserStorage(user);
    }

    public User updateUserService(User user) {
        checkValidationUser(user);
        return userStorage.updateUserStorage(user);
    }

    public void deleteUserService(int id) {
        userStorage.deleteUserStorage(id);
    }

    public List<User> getAllUsersService() {
        return userStorage.getAllUsersStorage();
    }

    public static void checkValidationUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("WARN: Неверное имя.");
            user.setName(user.getLogin());
            log.info("INFO: name = login.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || isNoSpace(user.getLogin())) {
            log.error("ERROR: Неверный формат login.");
            throw new ValidationException("Неверный формат login.");
        }
        if (user.getEmail().isBlank() || !isEmail(user.getEmail())) {
            log.error("ERROR: Неверный формат email.");
            throw new ValidationException("Неверный формат email.");
        }
        if (isDateFuture(user.getBirthday())) {
            log.error("ERROR: Birthday не может быть в будущем.");
            throw new ValidationException("Birthday не может быть в будущем.");
        }
    }

    public void addFriend(int id, int friendId) {
        if (id < 0 || friendId < 0)
            throw new NotFoundException("Неверный формат id");
        userStorage.addFriend(id, friendId);
        eventService.createEvent(id, EventType.FRIEND, EventOperation.ADD, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        if (id < 0 || friendId < 0)
            throw new NotFoundException("Неверный формат id");
        userStorage.deleteFriend(id, friendId);
        eventService.createEvent(id, EventType.FRIEND, EventOperation.REMOVE, friendId);
    }

    public List<User> getUserFriends(int id) {
        if (userStorage.getUserByIdStorage(id).getFriends() == null)
            userStorage.getUserByIdStorage(id).setFriends(new ArrayList<>());
        return userStorage.getUserByIdStorage(id).getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User getUserById(int id) {
        return userStorage.getUserByIdStorage(id);
    }

    public Set<User> getUserCommonFriends(int id, int otherId) {
        return userStorage.getUserByIdStorage(id).getFriends()
                .stream()
                .filter(getUserById(otherId).getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    private static boolean isNoSpace(String string) {
        return string.matches("\\S");
    }

    private static boolean isDateFuture(LocalDate inputDate) {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        return inputDate.isAfter(localDate);
    }

    private static boolean isEmail(String emailAddress) {
        String regexPattern = "^(.+)@(\\S+)\\.\\w+$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public List<Film> getRecommendations(int userId) {
        return filmStorage.getRecommendations(userId);
    }
}
