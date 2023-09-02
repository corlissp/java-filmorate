package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

/**
 * @author Min Danil 13.07.2023
 */
public interface UserStorage {
    User createUserStorage(User user);

    User updateUserStorage(User user);

    void deleteUserStorage(int id);

    List<User> getAllUsersStorage();

    User getUserByIdStorage(int id);

    boolean addFriend(int userId, int friendId);

    boolean deleteFriend(int userId, int friendId);
}
