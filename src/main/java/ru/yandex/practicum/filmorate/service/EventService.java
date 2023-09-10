package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.feed.Event;
import ru.yandex.practicum.filmorate.models.feed.EventOperation;
import ru.yandex.practicum.filmorate.models.feed.EventType;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final FeedStorage feedStorage;
    private final UserStorage userStorage;


    public List<Event> getFeed(int userId) {
        userStorage.getUserByIdStorage(userId);
        return feedStorage.findFeed(userId);
    }

    public void createEvent(int userId, EventType type, EventOperation operation, int entityId) {
        Event event = Event.builder()
                .userId(userId)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(type)
                .operation(operation)
                .entityId(entityId)
                .build();
        feedStorage.addEvent(event);
    }
}
