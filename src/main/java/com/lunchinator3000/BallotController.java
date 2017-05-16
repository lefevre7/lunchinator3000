package com.lunchinator3000;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public /*HashMap<RestaurantController.AbstractRestaurant,RestaurantController.RestaurantChoices>*/ResponseEntity<BallotInterface> getBallot (@PathVariable("ballotId") UUID ballotId) {
        //final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants"; //http://localhost:8080/springrestexample/employees.json";
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();
        ArrayList<Integer> averageRatings = new ArrayList<>();
        //RestaurantController.AbstractRestaurant restaurantSuggestion = new RestaurantSuggestion();
        RestaurantSuggestion restaurantSuggestion = new RestaurantSuggestion();
        ArrayList<RestaurantController.RestaurantChoices> restaurantSuggestions = new ArrayList<>();

        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = new ArrayList<>();
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();
        ArrayList<RestaurantController.RestaurantChoice> restaurantChoicesAfter1 = new ArrayList<>();
        ArrayList<RestaurantController.RestaurantChoice> restaurantChoicesBefore1 = new ArrayList<>();

        //RestaurantController.AbstractRestaurant restaurantWinner = new RestaurantWinner();
        RestaurantWinner restaurantWinner = new RestaurantWinner();
        ArrayList<RestaurantController.AbstractRestaurant> restaurantWinners = new ArrayList<>();

        ballotBeforeOrAfter1 = new HashMap<RestaurantController.AbstractRestaurant,RestaurantController.RestaurantChoices>();
        ArrayList<Restaurant> ballotBeforeOrAfter2 = new ArrayList<>();

        CreateBallot createBallot = new CreateBallot();
        CreateBallot.Ballot1 ballot = createBallot.getBallot();


        System.out.println("Creating new restaurant controller");
        RestaurantController restaurantController = new RestaurantController(time);
        randomRestaurants = ballot.getRestaurants();//restaurantController.getRestaurants();
        restaurantsReviews = restaurantController.getRestaurantsReviews(randomRestaurants);

        if(restaurantsReviews == null){
            BallotInterface winner = new CreateBallot.BallotAfter();
            //winner
            return new ResponseEntity<BallotInterface>(winner, HttpStatus.BAD_REQUEST);
        }

        averageRatings = restaurantController.getAverageRestaurantRating(restaurantsReviews);

        restaurantSuggestion = restaurantController.getRestaurantSuggestion(averageRatings, restaurantsReviews);
        restaurantChoicesBefore = restaurantController.getRestaurantChoiceBefore(averageRatings, randomRestaurants);

        System.out.println("Here are the restaruantChoicesBefore");
        for (int i = 0; i < restaurantChoicesBefore.size(); i++) {
            System.out.println(restaurantChoicesBefore.get(i).getName());
            System.out.println(restaurantChoicesBefore.get(i).getDescription());
            restaurantChoicesBefore1.add(restaurantChoicesBefore.get(i));
        }


        // Getting the most current date and time as possible
        System.out.println("Printing date in MMM dd, yyyy HH:mma");
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mma");
        Date date = new Date();
        dateFormat.format(date);
        System.out.println(date);

        Date ballotDate = ballot.getTime();
        dateFormat.format(ballotDate);
        System.out.println("Printing ballotDate");
        System.out.println(ballotDate);
        System.out.println(dateFormat.format(ballotDate));

        if(date.before(ballotDate)) {
            BallotInterface suggestion = new CreateBallot.BallotBefore(restaurantSuggestion, restaurantChoicesBefore1);
            //return suggestion;
            return new ResponseEntity<BallotInterface>(suggestion, HttpStatus.OK);
        }
        else {
            VoteController voteController = new VoteController();
            HashMap<String,Vote> votes = voteController.getVotes();

            restaurantChoicesAfter = restaurantController.getRestaurantChoicesAfter(votes, restaurantChoicesBefore);

            System.out.println("Here are the restaruantChoicesAfter");
            for (int i = 0; i < restaurantChoicesAfter.size(); i++) {
                System.out.println(restaurantChoicesAfter.get(i).getName());
                System.out.println(restaurantChoicesAfter.get(i).getVotes());
                restaurantChoicesAfter1.add(restaurantChoicesAfter.get(i));
            }
            restaurantWinner = restaurantController.getRestaurantWinner(restaurantChoicesAfter);
            restaurantWinner.setDatetime(dateFormat.format(ballotDate)); // set again (not sure why it needs to be)

            /*ArrayList<RestaurantController.IncomingRestaurant> randomTemp = incomingRestaurants;
            ArrayList<RestaurantController.IncomingRestaurant> randomRestaurants = new ArrayList<>();
            Collections.shuffle(randomTemp);*/
            Collections.shuffle(restaurantChoicesAfter1);

            BallotInterface winner = new CreateBallot.BallotAfter(restaurantWinner, restaurantChoicesAfter1);
            //return winner;
            return new ResponseEntity<BallotInterface>(winner, HttpStatus.OK);
        }

    }
    @RequestMapping(value = "/error", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> error(@PathVariable("ballotId") UUID ballotId){
        String error = "There has been an error (there probably wasn't an GUID for the ballot, or if there was, it " +
                "wasn't a correct one";
        return new ResponseEntity<String>(error.toString(), HttpStatus.BAD_REQUEST);
    }
}
