package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Review;
import ru.yandex.practicum.filmorate.models.feed.EventOperation;
import ru.yandex.practicum.filmorate.models.feed.EventType;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final EventService eventService;

    public ReviewService(ReviewStorage reviewStorage, EventService eventService) {
        this.reviewStorage = reviewStorage;
        this.eventService = eventService;
    }

    public Optional<Review> create(Review review) {
        Optional<Review> reviewOpt = reviewStorage.create(review);
        reviewOpt.ifPresent(r -> eventService.createEvent(r.getUserId(), EventType.REVIEW, EventOperation.ADD, r.getReviewId()));
        return reviewOpt;
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
        Optional<Review> updateReviewOpt = reviewStorage.update(review);
        updateReviewOpt.ifPresent(r -> eventService.createEvent(r.getUserId(), EventType.REVIEW, EventOperation.UPDATE, r.getReviewId()));
        return updateReviewOpt;
    }

    public Optional<Review> deleteReview(int reviewId) {
        Review review = reviewStorage.findReviewById(reviewId).get();

        Optional<Review> remove = reviewStorage.delete(review);
        if (remove.isPresent()) {
            eventService.createEvent(review.getUserId(), EventType.REVIEW, EventOperation.REMOVE, reviewId);
        }
        return remove;
    }
}
