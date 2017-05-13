package com.lunchinator3000;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/10/2017.
 */
public class Ballot {
    private UUID ballotId;
    private Date time;
    private ArrayList<Voter> voters;

    private ArrayList<RestaurantController.IncomingRestaurant> incomingRestaurants;
    private RestaurantSuggestion restaurantSuggestion;
    private ArrayList<RestaurantChoiceBefore> restaurantChoiceBefores;
    private ArrayList<RestaurantChoiceAfter> restaurantChoiceAfters;
    private RestaurantWinner restaurantWinner;

    public Ballot(UUID ballotId, Date time, ArrayList<Voter> voters) {
        this.ballotId = ballotId;
        this.time = time;
        this.voters = voters;
    }

    public UUID getBallotId() {
        return ballotId;
    }

    public void setBallotId(UUID ballotId) {
        this.ballotId = ballotId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ArrayList<Voter> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<Voter> voters) {
        this.voters = voters;
    }

    public ArrayList<RestaurantController.IncomingRestaurant> getIncomingRestaurants() {
        return incomingRestaurants;
    }

    public void setIncomingRestaurants(ArrayList<RestaurantController.IncomingRestaurant> incomingRestaurants) {
        this.incomingRestaurants = incomingRestaurants;
    }

    public RestaurantSuggestion getRestaurantSuggestion() {
        return restaurantSuggestion;
    }

    public void setRestaurantSuggestion(RestaurantSuggestion restaurantSuggestion) {
        this.restaurantSuggestion = restaurantSuggestion;
    }

    public ArrayList<RestaurantChoiceBefore> getRestaurantChoiceBefores() {
        return restaurantChoiceBefores;
    }

    public void setRestaurantChoiceBefores(ArrayList<RestaurantChoiceBefore> restaurantChoiceBefores) {
        this.restaurantChoiceBefores = restaurantChoiceBefores;
    }

    public ArrayList<RestaurantChoiceAfter> getRestaurantChoiceAfters() {
        return restaurantChoiceAfters;
    }

    public void setRestaurantChoiceAfters(ArrayList<RestaurantChoiceAfter> restaurantChoiceAfters) {
        this.restaurantChoiceAfters = restaurantChoiceAfters;
    }

    public RestaurantWinner getRestaurantWinner() {
        return restaurantWinner;
    }

    public void setRestaurantWinner(RestaurantWinner restaurantWinner) {
        this.restaurantWinner = restaurantWinner;
    }
}
