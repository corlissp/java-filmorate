package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

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
        log.info("INFO: Пользователь с id = {} сохранён.", freeId);
        return user;
    }

    @Override
    public User updateUserStorage(User user) {
        int id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
            log.info("INFO: Пользователь с id = {} обновлён.", id);
        } else {
            log.error("ERROR: Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
        return user;
    }

    @Override
    public void deleteUserStorage(int id) {
        if (users.containsKey(id)) {
            users.remove(id);
            log.info("INFO: Пользователь с id = {} удалён.", id);
        } else {
            log.error("ERROR: Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
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
        if (!users.containsKey(id)) {
            log.error("ERROR: Пользователь с id = {} не найден.", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден.");
        }
        return users.get(id);
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        updateUserStorage(user);
        updateUserStorage(friend);
        return true;
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        return false;
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
