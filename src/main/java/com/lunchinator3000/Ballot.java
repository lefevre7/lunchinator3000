package com.lunchinator3000;

import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/10/2017.
 */
public class Ballot {
    private UUID ballotId;
    private Time time;
    private ArrayList<Voter> voters;

    public Ballot(UUID ballotId, Time time, ArrayList<Voter> voters) {
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public ArrayList<Voter> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<Voter> voters) {
        this.voters = voters;
    }
}
