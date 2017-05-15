package com.lunchinator3000;

import java.util.Date;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantWinner implements RestaurantController.AbstractRestaurant {
    private Date date;
    private int votes;
    private String name;
    private Integer id;

    public RestaurantWinner() {
    }

    public RestaurantWinner(int id, String name, Date date, int votes) {
        this.name = name;
        this.id = id;
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
