package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantSuggestion implements RestaurantController.AbstractRestaurant {
    private int averageReview;
    private String TopReviewer;
    private String Review;
    private String name;
    private Integer id;

    public RestaurantSuggestion() {
    }

    public RestaurantSuggestion(int id, String name, int averageReview, String TopReviewer, String Review) {
        this.name = name;
        this.id = id;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
