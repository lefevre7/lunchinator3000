package com.lunchinator3000;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/12/2017.
 */
public class InitialBallot {
    private Date time;
    private ArrayList<Voter> voters;

    public InitialBallot(Date time, ArrayList<Voter> voters) {
        this.voters = voters;
        this.time = time;
    }
    public InitialBallot() {

    }

    public ArrayList<Voter> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<Voter> voters) {
        this.voters = voters;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
