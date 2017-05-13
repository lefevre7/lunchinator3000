package com.lunchinator3000;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class BallotController {

    Date time;
    ArrayList<Voter> voters;
    private ArrayList<RestaurantController.IncomingRestaurant> randomRestaurants;

    public BallotController() {

    }

    @RequestMapping(value = "/api/ballot/{ballotId}", method = RequestMethod.GET, produces = "application/json")
    public /*ResponseEntity<*/Ballot/*>*/ getBallot (@PathVariable("ballotId") UUID ballotId) {
        //final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants"; //http://localhost:8080/springrestexample/employees.json";
        Ballot ballot = new Ballot(ballotId, time, voters);


        RestTemplate restTemplate = new RestTemplate();
        //Ballot ballot = new Ballot();
        //String result = restTemplate.getForObject(uri, String.class);
        //Ballot ballot = restTemplate.getForObject(uri, ballot);

        //System.out.println(result);

        //ballotId = UUID.randomUUID();

        System.out.println("Creating new restaurant controller");
        RestaurantController restaurantController = new RestaurantController();
        randomRestaurants = restaurantController.getRestaurants();


        //randomRestaurants.
        System.out.println(randomRestaurants);
        return /*new ResponseEntity<Ballot>(*/ballot/*, HttpStatus.OK)*/;
    }
}
