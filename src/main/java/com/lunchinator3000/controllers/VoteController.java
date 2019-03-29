package com.lunchinator3000.controllers;

import com.lunchinator3000.dto.vote.Vote;
import com.lunchinator3000.service.BallotService;
import com.lunchinator3000.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 * VoteController obtains obtains a voter's vote and records it if it's not past time.
 */
@RestController
public class VoteController {

    private VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @RequestMapping(value = "/api/vote", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> getVote(@RequestParam("id") int id, @RequestParam("ballotId") UUID ballotId,
                                                 @RequestParam("voterName") String voterName,
                                                 @RequestParam("emailAddress") String emailAddress) {
        return voteService.getVote(id, ballotId, voterName, emailAddress);
    }
}
