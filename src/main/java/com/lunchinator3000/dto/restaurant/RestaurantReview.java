package com.lunchinator3000.dto.restaurant;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantReview {
    private int id;
    private String restaurant;
    private String reviewer;
    private int rating;
    private String review;

    public RestaurantReview() {

    }
    public RestaurantReview(int id, String restaurant, String reviewer, int rating, String review) {
        this.id = id;
        this.restaurant = restaurant;
        this.reviewer = reviewer;
        this.rating = rating;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
