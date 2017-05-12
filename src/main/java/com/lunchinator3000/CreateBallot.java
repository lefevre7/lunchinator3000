package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/10/2017.
 */
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class CreateBallot {
    private UUID ballotId;
    private Date time;
    private ArrayList<Voter> voters;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /*@RequestMapping(value = "/employees", method = RequestMethod.POST)
    public ResponseEntity<String> createEmployee(@RequestBody Ballot employee)
    {
        System.out.println(employee);
        return new ResponseEntity(HttpStatus.CREATED);
    }*/

    @RequestMapping(value = "/api/create-ballot", method = RequestMethod.POST)
    public ResponseEntity<String> createBallot(@JsonProperty("endTime") @RequestBody Date time, @JsonProperty("voters")@RequestBody ArrayList<Voter> voters) {
        //return new Ballot(ballotId, time,voters);
        Ballot ballot = getBallot(time, voters);
        String ballotId = "{\n\t\"ballotId\":\"" + ballot.getBallotId().toString() + "\"\n}";
        return new ResponseEntity<String>(ballotId, HttpStatus.CREATED);
    }
    public Ballot getBallot(Date time, ArrayList<Voter> voters) {
        final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants"; //http://localhost:8080/springrestexample/employees.json";
        ballotId = UUID.randomUUID();
        //time = Time.valueOf()

        //RestTemplate restTemplate = new RestTemplate();
        //Ballot ballot = new Ballot();
        //String result = restTemplate.getForObject(uri, String.class);
        //Ballot ballot = restTemplate.getForObject(uri, ballot);

        //System.out.println(result);
        return new Ballot(ballotId, time, voters);//just return ballot id
    }
}