package com.lunchinator3000.dto.ballot;

import com.lunchinator3000.dto.vote.Voter;

import java.util.ArrayList;

/**
* Created by Jeremy L on 11/8/2018
*/
public class InitialBallot {
    private String endTime;
    private ArrayList<Voter> voters;

    public InitialBallot(String endTime, ArrayList<Voter> voters) {
        this.voters = voters;
        this.endTime = endTime;
    }
    public InitialBallot() {

    }

    public ArrayList<Voter> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<Voter> voters) {
        this.voters = voters;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String time) {
        this.endTime = time;
    }
}
