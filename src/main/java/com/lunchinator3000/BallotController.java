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

    //Date time;
    //ArrayList<CreateBallot.Voter1> voters;
    private ArrayList<RestaurantController.IncomingRestaurant> randomRestaurants;
    private RestaurantSuggestion restaurantSuggestion;
    private CreateBallot.BallotBeforeOrAfter ballotBeforeOrAfter;


    public BallotController() {

    }

    @RequestMapping(value = "/api/ballot/{ballotId}", method = RequestMethod.GET, produces = "application/json")
    public /*ResponseEntity<*/CreateBallot.BallotBeforeOrAfter/*>*/ getBallot (@PathVariable("ballotId") UUID ballotId) {
        //final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants"; //http://localhost:8080/springrestexample/employees.json";
        CreateBallot createBallot = new CreateBallot();
        CreateBallot.Ballot1 ballot = createBallot.getBallot();


        System.out.println("Creating new restaurant controller");
        RestaurantController restaurantController = new RestaurantController();
        randomRestaurants = restaurantController.getRestaurants();
        //restaurantSuggestion = restaurantController.getRestaurantSuggestion()



        //randomRestaurants.
        System.out.println(randomRestaurants);

        // Implement this pseudo code
        //if (ballot.getTime() > new Date().getTime())
            //then stick it into the ballotBeforeOrAfter object and return it
        //return  ;
        //else
        // stick stick stuff into the ballotBeforeOrAfter object and return it
        //return  ;




        return ballotBeforeOrAfter;
    }
}
