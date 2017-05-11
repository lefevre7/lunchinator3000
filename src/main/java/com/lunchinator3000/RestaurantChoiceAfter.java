package com.lunchinator3000;

import java.util.Date;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantChoiceAfter extends AbstractRestaurant {
    private int votes;

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
}
