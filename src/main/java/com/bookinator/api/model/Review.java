package com.bookinator.api.model;

/**
 * Created by Sabina on 4/14/2018.
 */
public class Review {
    private int id;
    private int holdingRequestId;
    private int reviewerId;
    private int revieweeId;
    private String reviewText;
    private int score;

    public Review() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHoldingRequestId() {
        return holdingRequestId;
    }

    public void setHoldingRequestId(int holdingRequestId) {
        this.holdingRequestId = holdingRequestId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getRevieweeId() {
        return revieweeId;
    }

    public void setRevieweeId(int revieweeId) {
        this.revieweeId = revieweeId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
