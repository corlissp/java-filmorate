package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;

    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public Optional<Review> create(Review review) {
        return reviewStorage.create(review);
    }

    public Optional<Review> findReviewById(int reviewId) {
        return reviewStorage.findReviewById(reviewId);
    }

    public List<Review> getAllReviews() {
        return reviewStorage.getAllReviews();
    }

    public List<Review> getReviewsByFilmId(int filmId, int count) {
        return reviewStorage.getReviewsByFilmId(filmId, count);
    }

    public Optional<Review> addLike(Integer reviewId, Integer userId) {
        return reviewStorage.addLike(reviewId, userId);
    }

    public Optional<Review> addDislike(Integer reviewId, Integer userId) {
        return reviewStorage.addDislike(reviewId, userId);
    }

    public Optional<Review> removeLike(Integer reviewId, Integer userId) {
        return reviewStorage.removeLike(reviewId, userId);
    }

    public Optional<Review> updateReview(Review review) {
        return reviewStorage.update(review);
    }

    public Optional<Review> deleteReview(int reviewId) {
        return reviewStorage.delete(reviewId);
    }
}
