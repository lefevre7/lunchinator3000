package com.lunchinator3000;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class BallotController {

    Date time;
    //ArrayList<CreateBallot.Voter1> voters;
    private ArrayList<RestaurantController.IncomingRestaurant> randomRestaurants;
    private RestaurantSuggestion restaurantSuggestion;
    private BallotInterface ballotBeforeOrAfter;
    //private ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> ballotBefore;
    //private ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> ballotAfter;

    //private RestaurantController.RestaurantChoices restaurantChoicesAfter1;

    private static HashMap<RestaurantController.AbstractRestaurant,RestaurantController.RestaurantChoices/*RestaurantController.RestaurantChoices*/> ballotBeforeOrAfter1;


    public BallotController() {

    }

    @RequestMapping(value = "/api/ballot/{ballotId}", method = RequestMethod.GET, produces = "application/json")
    public /*HashMap<RestaurantController.AbstractRestaurant,RestaurantController.RestaurantChoices>*/BallotInterface getBallot (@PathVariable("ballotId") UUID ballotId) {
        //final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants"; //http://localhost:8080/springrestexample/employees.json";
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();
        ArrayList<Integer> averageRatings = new ArrayList<>();
        //RestaurantController.AbstractRestaurant restaurantSuggestion = new RestaurantSuggestion();
        RestaurantSuggestion restaurantSuggestion = new RestaurantSuggestion();
        ArrayList<RestaurantController.RestaurantChoices> restaurantSuggestions = new ArrayList<>();

        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = new ArrayList<>();
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();
        RestaurantController.RestaurantChoices restaurantChoicesAfter1 = new RestaurantChoicesAfter();
        RestaurantController.RestaurantChoices restaurantChoicesBefore1 = new RestaurantChoicesBefore();

        //RestaurantController.AbstractRestaurant restaurantWinner = new RestaurantWinner();
        RestaurantWinner restaurantWinner = new RestaurantWinner();
        ArrayList<RestaurantController.AbstractRestaurant> restaurantWinners = new ArrayList<>();

        ballotBeforeOrAfter1 = new HashMap<RestaurantController.AbstractRestaurant,RestaurantController.RestaurantChoices>();
        ArrayList<Restaurant> ballotBeforeOrAfter2 = new ArrayList<>();

        CreateBallot createBallot = new CreateBallot();
        CreateBallot.Ballot1 ballot = createBallot.getBallot();


        System.out.println("Creating new restaurant controller");
        RestaurantController restaurantController = new RestaurantController(time);
        randomRestaurants = restaurantController.getRestaurants();
        restaurantsReviews = restaurantController.getRestaurantsReviews(randomRestaurants);
        averageRatings = restaurantController.getAverageRestaurantRating(restaurantsReviews);

        restaurantSuggestion = restaurantController.getRestaurantSuggestion(averageRatings, restaurantsReviews);
        restaurantChoicesBefore = restaurantController.getRestaurantChoiceBefore(averageRatings, randomRestaurants);

        System.out.println("Here are the restaruantChoicesBefore");
        for (int i = 0; i < restaurantChoicesBefore.size(); i++) {
            System.out.println(restaurantChoicesBefore.get(i).getName());
            System.out.println(restaurantChoicesBefore.get(i).getDescription());
            restaurantChoicesBefore1.getRestaurantChoices().add(restaurantChoicesBefore.get(i));
        }


        // Getting the most current date and time as possible
        DateFormat dateFormat = new SimpleDateFormat("M dd, yyyy HH:mma");
        Date date = new Date();
        dateFormat.format(date);

        Date ballotDate = ballot.getTime();
        dateFormat.format(ballotDate);

        if(date.before(ballotDate)) {
            //ballotBeforeOrAfter.setBallotBeforeOrAfter(ballotBefore.add(restaurantChoicesBefore));
            ballotBeforeOrAfter1.putIfAbsent(restaurantSuggestion, restaurantChoicesBefore1);
            //restaurantSuggestions.add(restaurantSuggestion);
            //ballotBeforeOrAfter2.add(restaurantSuggestions);
            ballotBeforeOrAfter2.add(restaurantSuggestion);
            ballotBeforeOrAfter2.add(restaurantChoicesBefore1);
            //ballotBeforeOrAfter = new CreateBallot.BallotBefore();
            //ballotBeforeOrAfter.setRestaurantChoices(restaurantSuggestion, restaurantChoicesBefore1)
            BallotInterface suggestion = new CreateBallot.BallotBefore(restaurantSuggestion, restaurantChoicesBefore1);
            return suggestion;
        }
        else {
            VoteController voteController = new VoteController();
            HashMap<String,Vote> votes = voteController.getVotes();

            restaurantChoicesAfter = restaurantController.getRestaurantChoicesAfter(votes, restaurantChoicesBefore);
            //restaurantChoicesAfter1 = (RestaurantController.RestaurantChoices) restaurantChoicesAfter;
            System.out.println("Here are the restaruantChoicesAfter");
            for (int i = 0; i < restaurantChoicesAfter.size(); i++) {
                System.out.println(restaurantChoicesAfter.get(i).getName());
                System.out.println(restaurantChoicesAfter.get(i).getVotes());
                restaurantChoicesAfter1.getRestaurantChoices().add(restaurantChoicesAfter.get(i));
            }
            restaurantWinner = restaurantController.getRestaurantWinner(restaurantChoicesAfter);
            //restaurantWinners.add(restaurantWinner);
            /*ballotBeforeOrAfter.setBallotBeforeOrAfter(ballotAfter.add(restaurantWinners);//);*/
            //ballotAfter.add(restaurantWinners);
            //ballotAfter.add(restaurantChoicesAfter);
            //ballotBeforeOrAfter.setBallotBeforeOrAfter(ballotAfter);
            ballotBeforeOrAfter1.put(restaurantWinner, restaurantChoicesAfter1);

            ballotBeforeOrAfter2.add(restaurantWinner);
            ballotBeforeOrAfter2.add(restaurantChoicesAfter1);

            //ballotBeforeOrAfter = new CreateBallot.BallotAfter(restaurantWinner, restaurantChoicesBefore1);
            BallotInterface winner = new CreateBallot.BallotAfter(restaurantWinner, restaurantChoicesBefore1);
            return winner;
        }


        //randomRestaurants.
        //System.out.println(randomRestaurants);

        // Implement this pseudo code
        //if (ballot.getTime() > new Date().getTime())
            //then stick it into the ballotBeforeOrAfter object and return it
        //return  ;
        //else
        // stick stick stuff into the ballotBeforeOrAfter object and return it
        //return  ;



        //return ballotBeforeOrAfter;
        //return ballotBeforeOrAfter2;
    }
}
