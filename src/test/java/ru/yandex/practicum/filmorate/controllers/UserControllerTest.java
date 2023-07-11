package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.annotation.Order;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Min Danil 11.07.2023
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Order(1)
    @Test
    public void addUserTest() throws ValidationException {
        User user = new User();
        user.setId(1);
        user.setLogin("f1unexx");
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
        assertEquals(user, new UserController().createUser(user));
    }

    @Order(2)
    @Test
    public void addEmptyAndBlankLoginUserTest() {
        boolean thrown = false;
        User user = new User();
        user.setId(1);
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
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

    @Order(3)
    @Test
    public void addFutureBirthdayUserTest() {
        boolean thrown = false;
        User user = new User();
        user.setId(1);
        user.setLogin("f1unexx");
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("3000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
        try {
            new UserController().createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(4)
    @Test
    public void addNoValidationEmailUserTest() {
        boolean thrown = false;
        User user = new User();
        user.setId(1);
        user.setLogin("f1unexx");
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin.yandex.ru");
        try {
            new UserController().createUser(user);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(5)
    @Test
    public void addEmptyNameUserTest() throws ValidationException {
        User user = new User();
        user.setId(1);
        user.setLogin("f1unexx");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
        user = new UserController().createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Order(6)
    @Test
    public void updateUserTest() throws ValidationException {
        User user = new User();
        user.setId(1);
        user.setLogin("f1unexx");
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
        assertEquals(user, new UserController().createUser(user));

        user.setLogin("corlissp");
        assertEquals(user, new UserController().updateUser(user));
    }

    @Order(7)
    @Test
    public void updateFailIdUserTest() throws ValidationException {
        boolean thrown = false;
        User user = new User();
        user.setId(1);
        user.setLogin("f1unexx");
        user.setName("Danil");
        user.setBirthday(LocalDate.parse("2000-07-23"));
        user.setEmail("danilwottwin@yandex.ru");
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
