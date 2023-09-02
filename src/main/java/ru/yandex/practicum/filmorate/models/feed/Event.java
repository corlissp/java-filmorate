package ru.yandex.practicum.filmorate.models.feed;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Event {
    private int userId;
    private int eventId;
    private long timestamp;
    private EventType eventType;
    private EventOperation operation;
    private int entityId;
}
