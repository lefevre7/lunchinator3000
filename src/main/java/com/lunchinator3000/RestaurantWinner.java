package com.lunchinator3000;

import java.util.Date;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantWinner extends RestaurantController.AbstractRestaurant {
    private Date date;
    private int votes;

    public RestaurantWinner(int id, String name, Date date, int votes) {
        super(id, name);
        this.date = date;
        this.votes = votes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String convertDate(Date date){
        return "";
    }
}
