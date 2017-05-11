package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/10/2017.
 */
import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class CreateBallot {
    private UUID ballotId;
    private Time time;
    private ArrayList<Voter> voters;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /*@RequestMapping(value = "/employees", method = RequestMethod.POST)
    public ResponseEntity<String> createEmployee(@RequestBody Ballot employee)
    {
        System.out.println(employee);
        return new ResponseEntity(HttpStatus.CREATED);
    }*/

    @RequestMapping("/greeting") //supposed to be post
    public Ballot ballot(@RequestParam(value="name", defaultValue="World") String name) {
        return new Ballot(ballotId, time,
                voters);
        //return new ResponseEntity<Ballot>(ballot, HttpStatus.CREATED)
    }
    public Ballot ballot() {//supposed to return a ballot in above method (not just display greeting)
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