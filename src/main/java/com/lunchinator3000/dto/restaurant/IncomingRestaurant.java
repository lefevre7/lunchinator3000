package com.lunchinator3000.dto.restaurant;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class IncomingRestaurant implements RestaurantInterface {
    private String waitTimeMinutes;
    private String description;
    private String name;
    private Integer id;

    public IncomingRestaurant() {

    }

    public IncomingRestaurant(int id, String name, String waitTimeMinutes, String description) {
        this.name = name;
        this.id = id;
        this.waitTimeMinutes = waitTimeMinutes;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWaitTimeMinutes() {
        return waitTimeMinutes;
    }

    public void setWaitTimeMinutes(String waitTimeMinutes) {
        this.waitTimeMinutes = waitTimeMinutes;
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
