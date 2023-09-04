package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.models.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public Optional<Review> findReviewById(@PathVariable(value = "id") Integer reviewId) {
        return reviewService.findReviewById(reviewId);
    }
    @PostMapping
    public Optional<Review> create(@Validated @RequestBody Review review) {
        log.info("Получен POST запрос  /reviews, {}", review);
        return reviewService.create(review);
    }
    @PutMapping
    public Optional<Review> update(@Validated @RequestBody Review review) {
        log.info("Получен PUT запрос  /reviews, {}", review);
        return reviewService.updateReview(review);
    }
    @DeleteMapping("/{id}")
    public Optional<Review> delete(@PathVariable(value = "id") Integer reviewId) {
        return reviewService.deleteReview(reviewId);
    }
    @PutMapping("/{id}/like/{userId}")
    public Optional<Review> addLike(@PathVariable(value = "id") Integer reviewId, @PathVariable Integer userId) {
        return reviewService.addLike(reviewId, userId);
    }
    @PutMapping("/{id}/dislike/{userId}")
    public Optional<Review> addDislike(@PathVariable(value = "id") Integer reviewId, @PathVariable Integer userId) {
        return reviewService.addDislike(reviewId, userId);
    }
    @DeleteMapping("/{id}/like/{userId}")
    public Optional<Review> removeLike(@PathVariable(value = "id") Integer reviewId, @PathVariable Integer userId) {
        return reviewService.removeLike(reviewId, userId);
    }
    @DeleteMapping("/{id}/dislike/{userId}")
    public Optional<Review> removeDislike(@PathVariable(value = "id") Integer reviewId, @PathVariable Integer userId) {
        return reviewService.addDislike(reviewId, userId);
    }
    @GetMapping()
    public List<Review> getReviewByFilmId(@RequestParam(required = false) Integer filmId, @RequestParam(defaultValue = "10", required = false) int count) {
        if (filmId == null) {
            return reviewService.getAllReviews();
        }
        if (count < 0) {
            throw new IncorrectParameterException("Количество искомых отзывов не может быть отрицательным", "count");
        }
        return reviewService.getReviewsByFilmId(filmId, count);
    }
}

