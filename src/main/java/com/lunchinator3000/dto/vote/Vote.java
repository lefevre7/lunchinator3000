package com.lunchinator3000.dto.vote;

import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class Vote {
    private UUID ballotId;
    private String emailAddress;
    private int restaurantId;
    private String voterName;

    public Vote(UUID ballotId, String emailAddress, int restaurantId, String voterName) {
        this.ballotId = ballotId;
        this.emailAddress = emailAddress;
        this.restaurantId = restaurantId;
        this.voterName = voterName;
    }

    public UUID getBallotId() {
        return ballotId;
    }

    public void setBallotId(UUID ballotId) {
        this.ballotId = ballotId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }
}
