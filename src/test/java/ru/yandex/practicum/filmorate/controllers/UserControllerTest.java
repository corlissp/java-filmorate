package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Min Danil 11.07.2023
 */
public class UserControllerTest {
    User user = User.builder().build();

    @BeforeEach
    public void beforeEach() {
        user.setId(1);
        user.setLogin("f1unexx");
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
    }

    @Test
    public void addUserTest() {
        Assertions.assertEquals(user, new UserController().createUser(user));
    }

    @Test
    public void addEmptyAndBlankLoginUserTest() {
        boolean thrown = false;
        user.setLogin("");
        try {
            new UserController().createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
        user.setLogin("   ");
        try {
            new UserController().createUser(user);
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
            new UserController().createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addNoValidationEmailUserTest() {
        boolean thrown = false;
        user.setEmail("danilwottwin.yandex.ru");
        try {
            new UserController().createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addEmptyNameUserTest() {
        user.setName("");
        user = new UserController().createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void updateUserTest() {
        assertEquals(user, new UserController().createUser(user));
        user.setLogin("corlissp");
        assertEquals(user, new UserController().updateUser(user));
    }

    @Test
    public void updateFailIdUserTest() {
        boolean thrown = false;
        assertEquals(user, new UserController().createUser(user));
        user.setLogin("corlissp");
        user.setId(999);
        try {
            new UserController().updateUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
