package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Min Danil 11.07.2023
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmControllerTest {

    @Order(1)
    @Test
    public void addFilmTest() throws ValidationException {
        Film film = new Film();
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(120);
        film.setDescription("Our planet in future. People have not got food on the Earth.");
        assertEquals(film, new FilmController().addFilm(film));
    }

    @Order(2)
    @Test
    public void addNoNameFilmTest() {
        boolean thrown = false;
        Film film = new Film();
        film.setId(1);
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(120);
        film.setDescription("Our planet in future. People have not got food on the Earth.");
        try {
            new FilmController().addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(3)
    @Test
    public void addNegativeDurationFilmTest() {
        boolean thrown = false;
        Film film = new Film();
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(-20);
        film.setDescription("Our planet in the future. People have not got food on the Earth.");
        try {
            new FilmController().addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(4)
    @Test
    public void addLongDescriptionFilmTest() {
        boolean thrown = false;
        Film film = new Film();
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(120);
        film.setDescription("Our planet in the future. People have not got food on the Earth. " +
                "bdhjvbejhbjhebvjhbehjvbhevhbehvbhebvhebvhbehvbbejfbjefbjebfjbejfbejbfjbefbe" +
                "efbjefjbejbfjebfjebfjbejfbejbfjebfjejfbejfbefejfbejfbjebfjejfjefbejbfjebfjbejfb" +
                "efbehbfebfhbehfehfbbbbehfbehbfhebfhbehfbehfhebfhbehfbhebfhebfhbehbfhebfhehfehbfbf");
        try {
            new FilmController().addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(5)
    @Test
    public void addBlankDescriptionFilmTest() {
        boolean thrown = false;
        Film film = new Film();
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(120);
        film.setDescription("    ");
        try {
            new FilmController().addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(6)
    @Test
    public void addWrongDateFilmTest() {
        boolean thrown = false;
        Film film = new Film();
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("1812-09-02"));
        film.setDuration(120);
        film.setDescription("Our planet in the future. People have not got food on the Earth.");
        try {
            new FilmController().addFilm(film);
        } catch (ValidationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Order(7)
    @Test
    public void updateFilmTest() throws ValidationException {
        Film film = new Film();
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(120);
        film.setDescription("Our planet in the future. People have not got food on the Earth.");
        assertEquals(film, new FilmController().addFilm(film));

        film.setName("Interstellar Update");
        assertEquals(film, new FilmController().updateFilm(film));
    }

    @Order(8)
    @Test
    public void updateFailIdFilmTest() throws ValidationException {
        boolean thrown = false;
        Film film = new Film();
        film.setId(1);
        film.setName("Interstellar");
        film.setReleaseDate(LocalDate.parse("2014-10-29"));
        film.setDuration(120);
        film.setDescription("Our planet in the future. People have not got food on the Earth.");
        assertEquals(film, new FilmController().addFilm(film));

        film.setName("Interstellar Update");
        film.setId(999);
        try {
            new FilmController().updateFilm(film);
        } catch (ValidationException exception) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
