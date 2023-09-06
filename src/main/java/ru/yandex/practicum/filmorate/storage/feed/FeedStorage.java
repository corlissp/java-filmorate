package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.models.feed.Event;

import java.util.List;

public interface FeedStorage {

    void addEvent(Event event);

    List<Event> findFeed(int userId);
}
