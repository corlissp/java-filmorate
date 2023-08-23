package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Min Danil 11.07.2023
 */
public class UserControllerTest {
    private User user;
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        user = User.builder().build();
        user.setId(1);
        user.setLogin("f1unexx");
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    public void addUserTest() {
        Assertions.assertEquals(user, userController.createUser(user));
    }

    @Test
    public void addEmptyAndBlankLoginUserTest() {
        boolean thrown = false;
        user.setLogin("");
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
        user.setLogin("   ");
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addFutureBirthdayUserTest() {
        boolean thrown = false;
        user.setBirthday(LocalDate.parse("3000-07-23"));
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addNoValidationEmailUserTest() {
        boolean thrown = false;
        user.setEmail("mail.ru");
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addEmptyNameUserTest() {
        user.setName("");
        user = userController.createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void updateUserTest() {
        assertEquals(user, userController.createUser(user));
        user.setLogin("corlissp");
        assertEquals(user, userController.updateUser(user));
    }

    @Test
    public void updateFailIdUserTest() {
        boolean thrown = false;
        assertEquals(user, userController.createUser(user));
        user.setLogin("corlissp");
        user.setId(999);
        try {
            userController.updateUser(user);
        } catch (NotFoundException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
