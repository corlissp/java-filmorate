package ru.yandex.practicum.filmorate.storage.like;

/**
 * @author Min Danil 06.09.2023
 */
public interface LikeStorage {

    void addLike(long film, long user);

    void deleteLike(long film, long user);
}
