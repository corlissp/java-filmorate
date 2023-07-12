package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Min Danil 12.07.2023
 */

@Slf4j
@Service
public class UserService {
    private static final Map<Integer, User> users = new HashMap<>();

    public static User createUserService(User user) {
        checkValidationUser(user);
        int freeId = IdGenerator.getFreeId();
        user.setId(freeId);
        users.put(user.getId(), user);
        log.info("INFO: User is saved.");
        return user;
    }

    public static User updateUserService(User user) {
        int id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
            log.info("INFO: User is update.");
        } else {
            log.error("ERROR: User с данным id не найден.");
            throw new ValidationException("User с данным id не найден.");
        }
        return user;
    }

    public static List<User> getAllUsersService() {
        List<User> usersList = new ArrayList<>();
        for (Integer key : users.keySet())
            usersList.add(users.get(key));
        log.info("INFO: Все пользователи получены.");
        return usersList;
    }

    private static void checkValidationUser(User user) {
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

    public static class IdGenerator {
        private static int id = 1;

        private static int getFreeId() {
            while (users.containsKey(id))
                id++;
            return id;
        }
    }
}