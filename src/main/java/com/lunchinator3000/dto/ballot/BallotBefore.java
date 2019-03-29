package com.lunchinator3000.dto.ballot;


import com.lunchinator3000.dto.restaurant.RestaurantChoice;
import com.lunchinator3000.dto.restaurant.RestaurantInterface;
import com.lunchinator3000.dto.restaurant.RestaurantSuggestion;

import java.util.ArrayList;

/**
 *  This class' ArrayList will hold the suggestion and the choices before time is up for the ballot so that it can
 *  display any type of RestaurantInterface and return it in one object
 */
public class BallotBefore implements BallotInterface {
    private RestaurantSuggestion suggestion;
    private ArrayList<RestaurantChoice> restaurantChoices;


    public BallotBefore(RestaurantSuggestion suggestion, ArrayList<RestaurantChoice> restaurantChoices) {
        this.suggestion = suggestion;
        this.restaurantChoices = restaurantChoices;
    }
    public BallotBefore() {
    }

    public RestaurantInterface getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(RestaurantSuggestion suggestion) {
        this.suggestion = suggestion;
    }

    public ArrayList<RestaurantChoice> getChoices() {
        return restaurantChoices;
    }

    public void setChoices(ArrayList<RestaurantChoice> restaurantChoices) {
        this.restaurantChoices = restaurantChoices;
    }

}
