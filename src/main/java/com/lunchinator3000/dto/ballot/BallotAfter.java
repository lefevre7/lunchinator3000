package com.lunchinator3000.dto.ballot;


import com.lunchinator3000.dto.restaurant.RestaurantChoice;
import com.lunchinator3000.dto.restaurant.RestaurantInterface;
import com.lunchinator3000.dto.restaurant.RestaurantWinner;

import java.util.ArrayList;

/**
 *  This class' ArrayList will hold the winner and the choices after time is up for the ballot so that it can
 *  display any type of RestaurantInterface and return it in one object
 */
public class BallotAfter implements BallotInterface{
    private RestaurantWinner winner;
    private ArrayList<RestaurantChoice> restaurantChoices;


    public BallotAfter(RestaurantWinner winner, ArrayList<RestaurantChoice> restaurantChoices) {
        this.winner = winner;
        this.restaurantChoices = restaurantChoices;
    }

    public BallotAfter() {
    }

    public RestaurantInterface getWinner() {
        return winner;
    }

    public void setWinner(RestaurantWinner winner) {
        this.winner = winner;
    }

    public ArrayList<RestaurantChoice> getChoices() {
        return restaurantChoices;
    }

    public void setChoices(ArrayList<RestaurantChoice> restaurantChoices) {
        this.restaurantChoices = restaurantChoices;
    }
}
