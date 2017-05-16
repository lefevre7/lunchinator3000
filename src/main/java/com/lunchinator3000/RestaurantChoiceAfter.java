package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantChoiceAfter implements RestaurantController.RestaurantChoice {
    private Integer id;
    private String name;
    private int votes = 0;

    public RestaurantChoiceAfter() {
    }

    public RestaurantChoiceAfter(int id, String name, int votes) {
        this.name = name;
        this.id = id;
        this.votes = votes;
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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void incrementVotes() {
        this.votes = this.votes++;
    }

}
