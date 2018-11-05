package com.lunchinator3000.service;

import com.lunchinator3000.controllers.VoteController;
import com.lunchinator3000.dto.vote.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class VoteService {


    private static HashMap<String,Vote> votes = null;
    // Implemented (in a singleton way) (if there isn't one, then create one)
    // that makes a new one of them each day

    // Provides a global point of access
    public static HashMap<String,Vote> getVotes() {

        if (votes == null) {
            votes = new HashMap<String,Vote>();
        }
        return votes;
    }

    // Prevents any other class from instantiating it
    public VoteService() {
    }


    public static ResponseEntity<String> getVote(int id, UUID ballotId, String voterName, String emailAddress) {

        // This is the correct way to get the votes
        VoteService voteService = new VoteService();
        HashMap<String, Vote> votes = voteService.getVotes();

        BallotService createBallot = new BallotService();
        BallotService.Ballot1 ballot = createBallot.getBallot();

        Vote vote = new Vote(ballotId, emailAddress, id, voterName);

        //recordVote(id, ballotId, voterName, emailAddress);
        System.out.println("Printing date in MMM dd, yyyy HH:mma");
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mma");
        Date date = new Date();
        dateFormat.format(date);
        System.out.println(date);

        // If the current date is before the ballot's time
        if (ballot.getTime().before(date)) {

            String error = "";
            return new ResponseEntity<String>(error, HttpStatus.CONFLICT);
        } else {
            if (votes.containsKey(emailAddress))
                votes.replace(emailAddress, vote);
            else
                votes.put(emailAddress, vote);

            String message = "";
            return new ResponseEntity<String>(message, HttpStatus.OK);
        }
    }

    //todo: implement this for a database
    /*private void recordVote(int id, UUID ballotId, String voterName, String emailAddress) {

        if (ballot.getBallotId() == ballotId){
            Voter voter = new Voter(voterName, emailAddress);
            if(ballot.getVoters().contains(voter))
            {//replace voter guess where the voter guessed

            }
            else {
                ballot.getVoters().add(voter);//and guess
                ballot.getRestaurantChoiceAfters().get(id).incrementVotes(); //make sure it's one of the five (it kind of has to be though)
            }
        }

    }*/
}
