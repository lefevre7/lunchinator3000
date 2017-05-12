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

    UUID ballotId;
    Time time;
    ArrayList<Voter> voters;
    HashMap<String,Vote> votes = new HashMap<String,Vote>();

    public VoteController() {
    }

    Ballot ballot = new Ballot(ballotId, time, voters);//a created ballot with guid for that day

    @RequestMapping(value = "/api/vote", method = RequestMethod.POST)
    public @ResponseBody /*ResponseEntity<HashMap<String,Vote>>*/ void getVote(@RequestParam("id") int id, @RequestParam("ballotId") UUID ballotId,
                                                 @RequestParam("voterName") String voterName,
                                                 @RequestParam("emailAddress") String emailAddress) {
        String string = "hi";
        System.out.println(string);

        //todo check if the individual request params are correct (and send the appropriate responce if not)

        Vote vote = new Vote(ballotId, emailAddress, id, voterName);
        if (votes.containsKey(emailAddress))
            votes.replace(emailAddress, vote);
        else
            votes.put(emailAddress, vote);

            //recordVote(id, ballotId, voterName, emailAddress);
        //return new ResponseEntity(votes, HttpStatus.OK);
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
