package com.lunchinator3000;

import java.util.Date;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class RestaurantWinner implements RestaurantController.AbstractRestaurant {
    private Integer id;
    private Date datetime;
    private String name;
    private int votes;

    public RestaurantWinner() {
    }

    public RestaurantWinner(int id, String name, Date datetime, int votes) {
        this.name = name;
        this.id = id;
        this.datetime = datetime;
        this.votes = votes;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
