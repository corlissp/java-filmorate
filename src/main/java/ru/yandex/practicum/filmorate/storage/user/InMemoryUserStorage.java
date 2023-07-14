package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.UserService.checkValidationUser;

/**
 * @author Min Danil 12.07.2023
 */

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUserStorage(User user) {
        checkValidationUser(user);
        int freeId = IdGenerator.getFreeId();
        user.setId(freeId);
        users.put(user.getId(), user);
        log.info("INFO: User is saved.");
        return user;
    }

    @Override
    public User updateUserStorage(User user) {
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

    @Override
    public List<User> getAllUsersStorage() {
        List<User> usersList = new ArrayList<>();
        for (Integer key : users.keySet())
            usersList.add(users.get(key));
        log.info("INFO: Все пользователи получены.");
        return usersList;
    }

    @Override
    public User getUserByIdStorage(int id) {
        if (!users.containsKey(id))
            throw new NotFoundException("Not Found");
        return users.get(id);
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
