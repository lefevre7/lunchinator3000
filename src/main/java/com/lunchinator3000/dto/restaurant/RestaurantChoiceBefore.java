package com.lunchinator3000.dto.restaurant;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantChoiceBefore implements RestaurantChoice {
    private Integer id;
    private String name;
    private int averageReview;
    private String description;

    public RestaurantChoiceBefore() {
    }

    public RestaurantChoiceBefore(int id, String name, int averageReview, String description) {
        this.id = id;
        this.name = name;
        this.averageReview = averageReview;
        this.description = description;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
