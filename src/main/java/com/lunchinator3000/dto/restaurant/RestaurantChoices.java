package com.lunchinator3000.dto.restaurant;

import java.util.ArrayList;

/**
 * Created by Jeremy L on 5/16/2017.
 */
public interface RestaurantChoices extends Restaurant {
    ArrayList<RestaurantChoice> restaurantChoices = null;

    public ArrayList<RestaurantChoice> getRestaurantChoices();

    public void setRestaurantChoices(ArrayList<RestaurantChoice> restaurantChoices);
}
