package com.lunchinator3000.controllers;

import com.lunchinator3000.dto.ballot.BallotInterface;
import com.lunchinator3000.dto.ballot.InitialBallot;
import com.lunchinator3000.service.BallotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Jeremy L on 5/11/2017.
 * BallotController obtains the ballotId, finds the proper ballot, obtains the restaurant suggestion,
 * choices, and winner, then outputs the proper ones in JSON.
 */
@RestController
public class BallotController {

    private BallotService ballotService;

    @Autowired
    public BallotController(BallotService ballotService) {
        this.ballotService = ballotService;
    }

    @RequestMapping(value = "/api/ballot/{ballotId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<BallotInterface> getBallot (@PathVariable("ballotId") UUID ballotId) {
        return ballotService.getBallot(ballotId);
    }

    @RequestMapping(value = "/api/create-ballot", method = RequestMethod.POST, headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<String> createBallot(@RequestBody InitialBallot initialBallot) {
        return ballotService.createBallot(initialBallot);
    }
}
