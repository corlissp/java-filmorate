package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Collection<Mpa> getAllMpa();
    Mpa getMpaById(int mpaId);
}
