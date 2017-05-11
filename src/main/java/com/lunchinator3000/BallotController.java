package com.lunchinator3000;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class BallotController {

    Time time;
    ArrayList<Voter> voters;

    public BallotController() {

    }

    @RequestMapping("/api/ballot/{ballotId}")
    public ResponseEntity<Ballot> getEmployeeById (@PathVariable("ballotId") UUID ballotId) {
        //final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants"; //http://localhost:8080/springrestexample/employees.json";
        Ballot ballot = new Ballot(ballotId, time, voters);


        RestTemplate restTemplate = new RestTemplate();
        //Ballot ballot = new Ballot();
        //String result = restTemplate.getForObject(uri, String.class);
        //Ballot ballot = restTemplate.getForObject(uri, ballot);

        //System.out.println(result);

        ballotId = UUID.randomUUID();
        return new ResponseEntity<Ballot>(ballot, HttpStatus.OK);
    }
}
