package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.models.Review;
import ru.yandex.practicum.filmorate.models.ReviewLike;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.reviewLikes.ReviewLikesDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReviewDBStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewLikesDao reviewLikesDao;
    private final UserDBStorage userDBStorage;
    private final FilmDBStorage filmDBStorage;

    @Autowired
    public ReviewDBStorage(JdbcTemplate jdbcTemplate, ReviewLikesDao reviewLikesDao, UserDBStorage userDBStorage, FilmDBStorage filmDBStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.reviewLikesDao = reviewLikesDao;
        this.userDBStorage = userDBStorage;
        this.filmDBStorage = filmDBStorage;
    }

    @Override
    public Optional<Review> findReviewById(int id) {
        String sql = "select * from REVIEWS where REVIEW_ID = ?";
        Optional<Review> review = jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), id).stream()
                .findAny();
        if (review.isEmpty())
            throw new NotFoundException(String.format("Review с идентификатором %s не найден", id));
        return review;
    }

    @Override
    public List<Review> getReviewsByFilmId(int filmId, int count) {
        String sql = "select * from REVIEWS " +
                "where FilmID = ? " +
                "ORDER BY USEFUL DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), filmId, count).stream().collect(Collectors.toList());
    }

    @Override
    public List<Review> getAllReviews() {
        String sql = "select * from REVIEWS " +
                "ORDER BY USEFUL DESC ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs)).stream().collect(Collectors.toList());
    }

    public Optional<Review> addLike(Integer reviewId, Integer userId) {
        ReviewLike reviewLike = ReviewLike.builder()
                .reviewId(reviewId)
                .userId(userId)
                .isPositive(true)
                .build();
        findReviewById(reviewId);
        userDBStorage.getUserByIdStorage(userId);
        Optional<ReviewLike> reviewLikeLocal = reviewLikesDao.findLikeByUserIdandReviewId(userId, reviewId);
        if (reviewLikeLocal.isPresent()) {
            throw new NotFoundException(String.format("reviewId = \"%s\" userId = %s", reviewId, userId));
        }
        Optional<ReviewLike> reviewLikeCreated = reviewLikesDao.create(reviewLike);
        if (reviewLikeCreated.isPresent()) {
            return updateUseful(reviewLikeCreated.get().getReviewId(), reviewLikeCreated.get().isPositive());
        }
        return Optional.empty();
    }

    public Optional<Review> addDislike(Integer reviewId, Integer userId) {
        ReviewLike reviewLike = ReviewLike.builder()
                .reviewId(reviewId)
                .userId(userId)
                .isPositive(false)
                .build();
        findReviewById(reviewId);
        userDBStorage.getUserByIdStorage(userId);
        Optional<ReviewLike> reviewLikeLocal = reviewLikesDao.findLikeByUserIdandReviewId(userId, reviewId);
        if (reviewLikeLocal.isEmpty()) {
            Optional<ReviewLike> reviewLikeCreated = reviewLikesDao.create(reviewLike);
            if (reviewLikeCreated.isPresent()) {
                return updateUseful(reviewLikeCreated.get().getReviewId(), reviewLikeCreated.get().isPositive());
            }
            return Optional.empty();
        } else {
            throw new NotFoundException(String.format("попытка повторно поставить dislike, reviewId = \"%s\" userId = %s", reviewId, userId));
        }
    }

    public Optional<Review> removeLike(Integer reviewId, Integer userId) {
        findReviewById(reviewId);
        userDBStorage.getUserByIdStorage(userId);
        Optional<ReviewLike> reviewLikeLocal = reviewLikesDao.findLikeByUserIdandReviewId(userId, reviewId);
        if (reviewLikeLocal.isEmpty()) {
            throw new NotFoundException(String.format("Review like не существует reviewId = \"%s\", userId = %s", reviewId, userId));
        }
        Optional<ReviewLike> reviewLikeDeleted = reviewLikesDao.deleteLike(reviewLikeLocal.get());
        if (reviewLikeDeleted.isPresent()) {
            return updateUseful(reviewLikeDeleted.get().getUserId(), !reviewLikeDeleted.get().isPositive());
        }
        return Optional.empty();
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("REVIEW_ID"))
                .content(rs.getString("CONTENT"))
                .userId(rs.getInt("UserID"))
                .filmId(rs.getInt("FilmID"))
                .useful(rs.getInt("USEFUL"))
                .isPositive(rs.getBoolean("IS_POSITIVE"))
                .build();
    }

    @Override
    public Optional<Review> create(Review review) {
        filmDBStorage.getFilmByIdStorage(review.getFilmId());
        userDBStorage.getUserByIdStorage(review.getUserId());

        // Проверяем, существует ли запись с такими UserID и FilmID
        String checkSql = "SELECT COUNT(*) FROM REVIEWS WHERE UserID = ? AND FilmID = ?";
        int existingRecords = jdbcTemplate.queryForObject(checkSql, Integer.class, review.getUserId(), review.getFilmId());

        if (existingRecords > 0) {
            // Запись с такими UserID и FilmID уже существует, выбрасываем исключение или обрабатываем ошибку
            throw new RuntimeException("Запись с такими UserID и FilmID уже существует");
        }

        // Если такой записи нет, выполняем вставку
        String insertSql = "INSERT INTO REVIEWS (CONTENT, UserID, FilmID, USEFUL, IS_POSITIVE) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, review.getContent());
            preparedStatement.setInt(2, review.getUserId());
            preparedStatement.setInt(3, review.getFilmId());
            preparedStatement.setInt(4, 0); // Здесь можно указать начальное значение для USEFUL
            preparedStatement.setBoolean(5, review.getIsPositive());
            return preparedStatement;
        }, keyHolder);

        int reviewId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findReviewById(reviewId);
    }

    private Map<String, Object> toMap(Review review) {
        Map<String, Object> values = new HashMap<>();
        values.put("CONTENT", review.getContent());
        values.put("UserID", review.getUserId());
        values.put("FilmID", review.getFilmId());
        values.put("USEFUL", 0);
        values.put("IS_POSITIVE", review.getIsPositive());
        return values;
    }

    public Optional<Review> updateUseful(int reviewId, boolean positive) {
        Optional<Review> review = findReviewById(reviewId);
        int useful = review.get().getUseful();
        int usefulUpdated = positive ? useful + 1 : useful - 1;
        String sql = "update REVIEWS set " +
                "USEFUL = ? " +
                "where REVIEW_ID = ?";
        int amountUpdateRows = jdbcTemplate.update(
                sql,
                usefulUpdated,
                reviewId
        );
        if (amountUpdateRows > 0) {
            return findReviewById(reviewId);
        }
        return Optional.empty();
    }

    public Optional<Review> update(Review review) {
        findReviewById(review.getReviewId());
        String sql = "update REVIEWS set " +
                "CONTENT = ?, " +
                "IS_POSITIVE = ? " +
                "where REVIEW_ID = ?";
        int amountUpdateRows = jdbcTemplate.update(
                sql,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        if (amountUpdateRows > 0) {
            return findReviewById(review.getReviewId());
        }
        return Optional.empty();
    }

    public Optional<Review> delete(Review review) {
        String sql = "delete from REVIEWS " +
                "where REVIEW_ID = ?";
        int amountUpdateRows = jdbcTemplate.update(
                sql,
                review.getReviewId()
        );
        if (amountUpdateRows > 0) {
            return Optional.of(review);
        }
        return Optional.empty();
    }
}
