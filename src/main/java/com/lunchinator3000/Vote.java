package com.lunchinator3000;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class Vote {

    public Vote() {
    }

    Ballot ballot;//a created ballot with guid for that day

    @RequestMapping(value = "/api/vote", method = RequestMethod.POST)
    public @ResponseBody /*ResponseEntity<String>*/ void getVote(@RequestParam("id") int id, @RequestParam("ballotId") UUID ballotId,
                                                 @RequestParam("voterName") String voterName,
                                                 @RequestParam("emailAddress") String emailAddress) {
        //string = "hi";
        //System.out.println(string);
        //return new ResponseEntity(string, HttpStatus.CREATED);//return new ResponseEntity<Ballot>(ballot, HttpStatus.OK)

        recordVote(id, ballotId, voterName, emailAddress);
    }

    private void recordVote(int id, UUID ballotId, String voterName, String emailAddress) {

        if (ballot.getBallotId() == ballotId){
            Voter voter = new Voter(voterName, emailAddress); //todo check and see if the voter exists (if they already voted)
            if(ballot.getVoters().contains(voter))
            {//replace voter guess where the voter guessed

            }
            else {
                ballot.getVoters().add(voter);//and guess
            }
        }

    }

}
