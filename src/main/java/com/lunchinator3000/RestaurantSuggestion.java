package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantSuggestion implements AbstractRestaurant {
    private Integer id;
    private String name;
    private int averageReview;
    private String TopReviewer;
    private String Review;

    public RestaurantSuggestion() {
    }

    public RestaurantSuggestion(int id, String name, int averageReview, String TopReviewer, String Review) {
        this.id = id;
        this.name = name;
        this.averageReview = averageReview;
        this.TopReviewer = TopReviewer;
        this.Review = Review;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
