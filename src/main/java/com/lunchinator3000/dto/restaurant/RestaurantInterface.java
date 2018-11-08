package com.lunchinator3000.dto.restaurant;

/**
 * Created by Jeremy L on 5/11/2017.
 */

public interface RestaurantInterface extends Restaurant{
    String name = "";
    Integer id = null;


    public String getName();

    public void setName(String name);

    public int getId();

    public void setId(int id);
}
