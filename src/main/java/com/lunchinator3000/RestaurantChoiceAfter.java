package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantChoiceAfter implements RestaurantController.AbstractRestaurant {
    private int votes;
    private String name;
    private Integer id;

    public RestaurantChoiceAfter() {
    }

    public RestaurantChoiceAfter(int id, String name, int votes) {
        this.name = name;
        this.id = id;
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void incrementVotes() {
        votes = votes++;
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
