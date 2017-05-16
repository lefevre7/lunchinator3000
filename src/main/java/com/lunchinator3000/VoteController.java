package com.lunchinator3000;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class VoteController {

    //UUID ballotId;
    //Time time;
    //ArrayList<Voter> voters;
    private static HashMap<String,Vote> votes = null;
    // (in a singleton way) (if there isn't one, then create one)
    // that makes a new one of them each day

    // Provides a global point of access
    public static HashMap<String,Vote> getVotes() {

        if (votes == null) {
            votes = new HashMap<String,Vote>();
        }
        return votes;
    }

    // Prevents any other class from instantiating it
    public VoteController() {
    }

    //Ballot ballot = new Ballot(ballotId, time, voters);//a created ballot with guid for that day

    @RequestMapping(value = "/api/vote", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> getVote(@RequestParam("id") int id, @RequestParam("ballotId") UUID ballotId,
                                                 @RequestParam("voterName") String voterName,
                                                 @RequestParam("emailAddress") String emailAddress) {
        String string = "hi";
        System.out.println(string);

        // This is the correct way to get the votes
        VoteController voteController = new VoteController();
        HashMap<String,Vote> votes = voteController.getVotes();

        Vote vote = new Vote(ballotId, emailAddress, id, voterName);

        if (votes.containsKey(emailAddress)) //votes needs to be getVotes
            votes.replace(emailAddress, vote); //votes needs to be getVotes
        else
            votes.put(emailAddress, vote); //votes needs to be getVotes

            //recordVote(id, ballotId, voterName, emailAddress);
        //return new ResponseEntity(votes, HttpStatus.OK);
        String error = "";
        return new ResponseEntity<String>(error, HttpStatus.CONFLICT);
    }

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
