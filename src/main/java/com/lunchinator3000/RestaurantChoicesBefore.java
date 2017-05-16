package com.lunchinator3000;

import java.util.ArrayList;

/**
 * Created by Jeremy L on 5/15/2017.
 */
public class RestaurantChoicesBefore implements RestaurantController.RestaurantChoices{
    private ArrayList<RestaurantController.RestaurantChoice> restaurantChoices = new ArrayList<>();

    @Override
    public ArrayList<RestaurantController.RestaurantChoice> getRestaurantChoices() {
        return restaurantChoices;
    }

    @Override
    public void setRestaurantChoices(ArrayList<RestaurantController.RestaurantChoice> restaurantChoices) {
        this.restaurantChoices = restaurantChoices;
    }
}
