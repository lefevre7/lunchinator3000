package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantChoiceAfter extends RestaurantController.AbstractRestaurant {
    private int votes;

    public RestaurantChoiceAfter() {
        super();
    }

    public RestaurantChoiceAfter(int id, String name, int votes) {
        super(id, name);
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

}
