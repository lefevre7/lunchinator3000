package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantChoiceAfter implements RestaurantController.RestaurantChoice {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
