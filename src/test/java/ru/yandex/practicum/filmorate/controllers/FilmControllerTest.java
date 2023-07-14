package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Min Danil 11.07.2023
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmControllerTest {
    private final Film film = Film.builder().build();
    private final FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));

    @BeforeEach
    public void beforeEach() {
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(120);
        film.setDescription("Our planet in future. People have not got food on the Earth.");
    }

    @Test
    public void addFilmTest() {
        assertEquals(film, filmController.addFilm(film));
    }

    @Test
    public void addNoNameFilmTest() {
        boolean thrown = false;
        film.setName("");
        try {
            filmController.addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addNegativeDurationFilmTest() {
        boolean thrown = false;
        film.setDuration(-20);
        try {
            filmController.addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addLongDescriptionFilmTest() {
        boolean thrown = false;
        film.setDescription("Our planet in the future. People have not got food on the Earth. " +
                "bdhjvbejhbjhebvjhbehjvbhevhbehvbhebvhebvhbehvbbejfbjefbjebfjbejfbejbfjbefbe" +
                "efbjefjbejbfjebfjebfjbejfbejbfjebfjejfbejfbefejfbejfbjebfjejfjefbejbfjebfjbejfb" +
                "efbehbfebfhbehfehfbbbbehfbehbfhebfhbehfbehfhebfhbehfbhebfhebfhbehbfhebfhehfehbfbf");
        try {
            filmController.addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addBlankDescriptionFilmTest() {
        boolean thrown = false;
        film.setDescription("    ");
        try {
            filmController.addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void addWrongDateFilmTest() {
        boolean thrown = false;
        film.setReleaseDate(LocalDate.parse("1812-09-02"));
        try {
            filmController.addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void updateFilmTest() {
        assertEquals(film, filmController.addFilm(film));

        film.setName("Interstellar Update");
        assertEquals(film, filmController.updateFilm(film));
    }

    @Test
    public void updateFailIdFilmTest() {
        boolean thrown = false;
        assertEquals(film, filmController.addFilm(film));

        film.setName("Interstellar Update");
        film.setId(999);
        try {
            filmController.updateFilm(film);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(1)
    @Test
    public void getAllFilmsTest() {
        List<Film> films = new ArrayList<>();
        films.add(film);
        filmController.addFilm(film);
        assertEquals(films, filmController.getAllFilms());
    }
}
