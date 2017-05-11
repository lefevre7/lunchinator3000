package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantChoiceBefore extends AbstractRestaurant {
    private int averageReview;
    private String description;

    public RestaurantChoiceBefore(int id, String name, int averageReview, String description) {
        super(id, name);
        this.averageReview = averageReview;
        this.description = description;
    }

    public int getAverageReview() {
        return averageReview;
    }

    public void setAverageReview(int averageReview) {
        this.averageReview = averageReview;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int findAverageReview(RestaurantReview restaurantReview){
        return 0;
    }
}
