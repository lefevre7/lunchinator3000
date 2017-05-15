package com.lunchinator3000;

import java.util.ArrayList;

/**
 * Created by Jeremy L on 5/15/2017.
 */
public class RestaurantChoicesAfter implements RestaurantController.RestaurantChoices {
    private ArrayList<RestaurantController.RestaurantChoices> restaurantChoices = new ArrayList<>();

    @Override
    public ArrayList<RestaurantController.RestaurantChoices> getRestaurantChoices() {
        return restaurantChoices;
    }

    @Override
    public void setRestaurantChoices(ArrayList<RestaurantController.RestaurantChoices> restaurantChoices) {
        this.restaurantChoices = restaurantChoices;
    }
}
