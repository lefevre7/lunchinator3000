package com.lunchinator3000;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class RestaurantController {
    private UUID ballotId;

    public RestaurantController() {

    }
    @RequestMapping("/restaurants")//how to do in java 3)
    public /*ArrayList<IncomingRestaurant>*/ String getRestaurants() { //ResponseEntity<Ballot>
        final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants"; //http://localhost:8080/springrestexample/employees.json";

        //time = Time.valueOf()

        RestTemplate restTemplate = new RestTemplate();
        //Ballot ballot = new Ballot();
        String result = restTemplate.getForObject(uri, String.class);
        //Ballot ballot = restTemplate.getForObject(uri, ballot);

        System.out.println(result);

        ballotId = UUID.randomUUID();
        return result; //return new ResponseEntity<Ballot>(ballot, HttpStatus.OK);
    }

}
