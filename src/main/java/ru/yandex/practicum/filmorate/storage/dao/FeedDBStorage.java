package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.feed.Event;
import ru.yandex.practicum.filmorate.models.feed.EventOperation;
import ru.yandex.practicum.filmorate.models.feed.EventType;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class FeedDBStorage implements FeedStorage {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addEvent(Event event) {
        String sql = "INSERT INTO FEED(USERID, EVENT_TIME, EVENT_TYPE, EVENT_OPERATION, ENTITY_ID) VALUES (:userId, :eventTime, :eventType, :eventOperation, :entityId)";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", event.getUserId());
        map.addValue("eventTime", event.getTimestamp());
        map.addValue("eventType", String.valueOf(event.getEventType()));
        map.addValue("eventOperation", String.valueOf(event.getOperation()));
        map.addValue("entityId", event.getEntityId());
        jdbcOperations.update(sql, map);
    }

    @Override
    public List<Event> findFeed(int userId) {
        String sql = "SELECT * FROM FEED WHERE USERID = :userId";
        return jdbcOperations.query(sql, new MapSqlParameterSource(Map.of("userId", userId)), ((rs, rowNum) -> Event.builder()
                .eventId(rs.getInt("EVENT_ID"))
                .userId(rs.getInt("USERID"))
                .timestamp(rs.getLong("EVENT_TIME"))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .operation(EventOperation.valueOf(rs.getString("EVENT_OPERATION")))
                .entityId(rs.getInt("ENTITY_ID"))
                .build()));
    }
}
