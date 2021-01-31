package com.lunchinator3000.service;

import com.lunchinator3000.dto.ballot.Ballot;
import com.lunchinator3000.dto.vote.Vote;
import com.lunchinator3000.dto.vote.Votes;
import com.lunchinator3000.repo.VotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
public class VoteService {

    private final Logger logger = LoggerFactory.getLogger(VoteService.class);
    private BallotService ballotService;

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

    @Autowired
    public VoteService(BallotService ballotService) {
        this.ballotService = ballotService;
    }

    @Autowired
    private VotesRepository repository;

    public ResponseEntity<String> getVote(int id, UUID ballotId, String voterName, String emailAddress) {

        HashMap<String, Vote> votes = getVotes();

        Ballot ballot = ballotService.getBallot();

        Vote vote = new Vote(ballotId, emailAddress, id, voterName);

        logger.debug("Printing date in MMM dd, yyyy HH:mma");
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mma");
        Date date = new Date();
        dateFormat.format(date);
        logger.debug(String.valueOf(date));

        // If the current date is before the ballot's time
        if (ballot.getTime().before(date)) {

            String error = "Vote not counted";
            return new ResponseEntity<String>(error, HttpStatus.CONFLICT);
        } else {
            if (votes.containsKey(emailAddress))
                votes.replace(emailAddress, vote);
            else
                votes.put(emailAddress, vote);

            //recordVote(id, ballotId, voterName, emailAddress);
            repository.save(new Votes(id, ballotId.toString(), voterName, emailAddress));

            String message = "Your vote has been cast";

            logger.info("A vote has been cast");
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
