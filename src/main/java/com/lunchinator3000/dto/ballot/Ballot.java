package com.lunchinator3000.dto.ballot;


import com.lunchinator3000.dto.restaurant.IncomingRestaurant;
import com.lunchinator3000.dto.vote.Voter;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jeremy L on 11/8/2018
 */
public class Ballot {
    private UUID ballotId;
    private Date endTime;
    private ArrayList<Voter> voters;
    private ArrayList<IncomingRestaurant> restaurants;

    private static Ballot ballot;

    private Ballot(UUID ballotId, Date time, ArrayList<Voter> voters, ArrayList<IncomingRestaurant> restaurants) {
        this.ballotId = ballotId;
        this.endTime = time;
        this.voters = voters;
        this.restaurants = restaurants;
    }

    // Prevents any other class from instantiating it
    private Ballot() {
    }

    // Provides a global point of access
    public static Ballot getBallot() {

        //todo: include the daily new making logic here or maybe in getNewBallot()
        if (null == ballot) {
            ballot = new Ballot();
        }
        return ballot;
    }

    public UUID getBallotId() {
        return ballotId;
    }

    public void setBallotId(UUID ballotId) {
        this.ballotId = ballotId;
    }

    public Date getTime() {
        return endTime;
    }

    public void setTime(Date time) {
        this.endTime = time;
    }

    public ArrayList<Voter> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<Voter> voters) {
        this.voters = voters;
    }

    public ArrayList<IncomingRestaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<IncomingRestaurant> restaurants) {
        this.restaurants = restaurants;
    }
}