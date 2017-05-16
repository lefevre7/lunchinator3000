package com.lunchinator3000;

import java.util.ArrayList;

/**
 * Created by Jeremy L on 5/15/2017.
 */
public class RestaurantChoicesBefore implements RestaurantChoices {
    private ArrayList<RestaurantChoice> restaurantChoices = new ArrayList<>();

    @Override
    public ArrayList<RestaurantChoice> getRestaurantChoices() {
        return restaurantChoices;
    }

    @Override
    public void setRestaurantChoices(ArrayList<RestaurantChoice> restaurantChoices) {
        this.restaurantChoices = restaurantChoices;
    }
}
