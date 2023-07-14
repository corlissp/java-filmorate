package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Min Danil 13.07.2023
 */

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUserService(User user) {
        return userStorage.createUserStorage(user);
    }

    public User updateUserService(User user) {
        return userStorage.updateUserStorage(user);
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
        User user = getUserById(id);
        User friendUser = getUserById(friendId);
        if (user.getFriends() == null)
            user.setFriends(new HashSet<>());
        user.getFriends().add(friendId);
        if (friendUser.getFriends() == null)
            friendUser.setFriends(new HashSet<>());
        friendUser.getFriends().add(id);
    }

    public void deleteFriend(int id, int friendId) {
        User user = getUserById(id);
        User friendUser = getUserById(friendId);
        if (user.getFriends() != null)
            user.getFriends().remove(friendId);
        if (friendUser.getFriends() != null)
            friendUser.getFriends().remove(id);
    }

    public List<User> getUserFriends(int id) {
        if (userStorage.getUserByIdStorage(id).getFriends() == null)
            userStorage.getUserByIdStorage(id).setFriends(new HashSet<>());
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
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
