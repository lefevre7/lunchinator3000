package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantSuggestion extends RestaurantController.AbstractRestaurant {
    private int averageReview;
    private String TopReviewer;
    private String Review;

    public RestaurantSuggestion(int id, String name, int averageReview, String TopReviewer, String Review) {
        super(id, name);
        this.averageReview = averageReview;
        this.TopReviewer = TopReviewer;
        this.Review = Review;
    }

    public int getAverageReview() {
        return averageReview;
    }

    public void setAverageReview(int averageReview) {
        this.averageReview = averageReview;
    }

    public String getTopReviewer() {
        return TopReviewer;
    }

    public void setTopReviewer(String topReviewer) {
        this.TopReviewer = topReviewer;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        this.Review = review;
    }
}
