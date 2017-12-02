package com.hznhta.tickit_user.Models;

import com.google.firebase.database.Exclude;

/**
 * Created by Haziq Farooq on 12/1/2017.
 */

public class ReviewRating {

    @Exclude
    public String mUserId;
    private int mRating;
    private String mReview;

    public ReviewRating() { }

    public ReviewRating(String userId, int rating, String review) {
        mUserId = userId;
        mRating = rating;
        mReview = review;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String review) {
        mReview = review;
    }
}
