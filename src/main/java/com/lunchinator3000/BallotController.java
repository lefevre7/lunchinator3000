package com.lunchinator3000;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jeremy L on 5/11/2017.
 * BallotController obtains the ballotId, finds the proper ballot, obtains the restaurant suggestion,
 * choices, and winner, then outputs the proper ones in JSON.
 */
@RestController
public class BallotController {
    private ArrayList<IncomingRestaurant> randomRestaurants;

    public BallotController() {
    }

    @RequestMapping(value = "/api/ballot/{ballotId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<BallotInterface> getBallot (@PathVariable("ballotId") UUID ballotId) {
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();
        ArrayList<Integer> averageRatings = new ArrayList<>();
        RestaurantSuggestion restaurantSuggestion = new RestaurantSuggestion();

        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = new ArrayList<>();
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();
        // To make them compatible with the created interfaces
        ArrayList<RestaurantChoice> restaurantChoicesAfter1 = new ArrayList<>();
        ArrayList<RestaurantChoice> restaurantChoicesBefore1 = new ArrayList<>();

        RestaurantWinner restaurantWinner = new RestaurantWinner();

        CreateBallot createBallot = new CreateBallot();
        CreateBallot.Ballot1 ballot = createBallot.getBallot();

        System.out.println("In the getBallot() method.");


        System.out.println("Creating new restaurant controller");
        RestaurantController restaurantController = new RestaurantController();
        randomRestaurants = ballot.getRestaurants();
        restaurantsReviews = restaurantController.getRestaurantsReviews(randomRestaurants);

        // If there was a ballot created
        System.out.println(ballotId);
        System.out.println(ballot.getBallotId());
        if (ballotId.toString().equals(ballot.getBallotId().toString())){
            // Continue
        }

        else { // If there was not a ballot created
            CreateBallot.BallotError ballotError = new CreateBallot.BallotError();
            ballotError.setMessage("There is no ballotDate (which probably means you haven't created a ballot).");
            BallotInterface message = ballotError;
            return new ResponseEntity<BallotInterface>(message, HttpStatus.BAD_REQUEST);
        }

        System.out.println("Getting averageRatings");
        averageRatings = restaurantController.getAverageRestaurantRating(restaurantsReviews);

        restaurantSuggestion = restaurantController.getRestaurantSuggestion(averageRatings, restaurantsReviews, randomRestaurants);
        restaurantChoicesBefore = restaurantController.getRestaurantChoiceBefore(averageRatings, randomRestaurants);

        // Make it so the restaurantChoicesBefore is compatible with the suggestion object
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
            Collections.shuffle(restaurantChoicesBefore1);
            BallotInterface suggestion = new CreateBallot.BallotBefore(restaurantSuggestion, restaurantChoicesBefore1);
            return new ResponseEntity<BallotInterface>(suggestion, HttpStatus.OK);
        }
        else {// After the current date
            VoteController voteController = new VoteController();
            HashMap<String,Vote> votes = voteController.getVotes();

            restaurantChoicesAfter = restaurantController.getRestaurantChoicesAfter(votes, restaurantChoicesBefore);

            // Make it so the restaurantChoicesAfter is compatible with the restaurantWinner object
            System.out.println("Here are the restaruantChoicesAfter");
            for (int i = 0; i < restaurantChoicesAfter.size(); i++) {
                System.out.println(restaurantChoicesAfter.get(i).getName());
                System.out.println(restaurantChoicesAfter.get(i).getVotes());
                restaurantChoicesAfter1.add(restaurantChoicesAfter.get(i));
            }
            restaurantWinner = restaurantController.getRestaurantWinner(restaurantChoicesAfter);
            restaurantWinner.setDatetime(dateFormat.format(ballotDate));

            Collections.shuffle(restaurantChoicesAfter1);
            BallotInterface winner = new CreateBallot.BallotAfter(restaurantWinner, restaurantChoicesAfter1);
            return new ResponseEntity<BallotInterface>(winner, HttpStatus.OK);
        }
    }
}
