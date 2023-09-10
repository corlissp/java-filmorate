package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {

    List<Review> getAllReviews();

    Optional<Review> create(Review review);

    Optional<Review> findReviewById(int id);

    Optional<Review> addLike(Integer reviewId, Integer userId);

    Optional<Review> addDislike(Integer reviewId, Integer userId);

    Optional<Review> removeLike(Integer reviewId, Integer userId);

    Optional<Review> update(Review review);

    Optional<Review> delete(Review review);

    List<Review> getReviewsByFilmId(int filmId, int count);


}
