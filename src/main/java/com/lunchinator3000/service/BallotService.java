package com.lunchinator3000.service;

import com.lunchinator3000.dto.ballot.*;
import com.lunchinator3000.dto.restaurant.*;
import com.lunchinator3000.dto.vote.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BallotService {
    private final Logger logger = LoggerFactory.getLogger(BallotService.class);
    private static ArrayList<IncomingRestaurant> randomRestaurants;
    private static UUID ballotId;
    private static String endTime;

    public ResponseEntity<BallotInterface> getBallot(UUID ballotId) {
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews;
        ArrayList<Integer> averageRatings;
        RestaurantSuggestion restaurantSuggestion;

        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore;
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter;
        // To make them compatible with the created interfaces
        ArrayList<RestaurantChoice> restaurantChoicesAfter1 = new ArrayList();
        ArrayList<RestaurantChoice> restaurantChoicesBefore1 = new ArrayList();

        RestaurantWinner restaurantWinner = new RestaurantWinner();

        BallotService ballotService = new BallotService();
        Ballot ballot = ballotService.getBallot();

        logger.debug("In the getBallot() method.");


        logger.debug("Creating new restaurant controller");
        RestaurantService restaurantService = new RestaurantService();
        randomRestaurants = ballot.getRestaurants();
        restaurantsReviews = restaurantService.getRestaurantsReviews(randomRestaurants);

        // If there was a ballot created
        logger.debug(String.valueOf(ballotId));
        logger.debug(String.valueOf(ballot.getBallotId()));
        if (ballotId.toString().equals(ballot.getBallotId().toString())){
            // Continue
        }

        else { // If there was not a ballot created
            BallotError ballotError = new BallotError();
            ballotError.setMessage("There is no ballotId (which probably means you haven't created a ballot or requested a previous one).");
            BallotInterface message = ballotError;
            logger.error("There is no ballotId (which probably means you haven't created a ballot or requested a previous one).");
            return new ResponseEntity<BallotInterface>(message, HttpStatus.BAD_REQUEST);
        }

        logger.debug("Getting averageRatings");
        averageRatings = restaurantService.getAverageRestaurantRating(restaurantsReviews);

        restaurantSuggestion = restaurantService.getRestaurantSuggestion(averageRatings, restaurantsReviews, randomRestaurants);
        restaurantChoicesBefore = restaurantService.getRestaurantChoiceBefore(averageRatings, randomRestaurants);

        // Make it so the restaurantChoicesBefore is compatible with the suggestion object
        logger.debug("Here are the restaruantChoicesBefore");
        for (int i = 0; i < restaurantChoicesBefore.size(); i++) {
            logger.debug(restaurantChoicesBefore.get(i).getName());
            logger.debug(restaurantChoicesBefore.get(i).getDescription());
            restaurantChoicesBefore1.add(restaurantChoicesBefore.get(i));
        }

        // Getting the most current date and time as possible
        logger.debug("Printing date in MMM dd, yyyy HH:mma");
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mma");
        Date date = new Date();
        dateFormat.format(date);
        logger.debug(String.valueOf(date));

        Date ballotDate = ballot.getTime();
        dateFormat.format(ballotDate);
        logger.debug("Printing ballotDate");
        logger.debug(String.valueOf(ballotDate));
        logger.debug(dateFormat.format(ballotDate));


        if(date.before(ballotDate)) {
            Collections.shuffle(restaurantChoicesBefore1);
            BallotInterface suggestion = new BallotBefore(restaurantSuggestion, restaurantChoicesBefore1);
            return new ResponseEntity<BallotInterface>(suggestion, HttpStatus.OK);
        }
        else {// After the current date
            VoteService voteService = new VoteService();
            HashMap<String,Vote> votes = voteService.getVotes();

            restaurantChoicesAfter = restaurantService.getRestaurantChoicesAfter(votes, restaurantChoicesBefore);

            // Make it so the restaurantChoicesAfter is compatible with the restaurantWinner object
            logger.debug("Here are the restaruantChoicesAfter");
            for (int i = 0; i < restaurantChoicesAfter.size(); i++) {
                logger.debug(restaurantChoicesAfter.get(i).getName());
                logger.debug(String.valueOf(restaurantChoicesAfter.get(i).getVotes()));
                restaurantChoicesAfter1.add(restaurantChoicesAfter.get(i));
            }
            restaurantWinner = restaurantService.getRestaurantWinner(restaurantChoicesAfter);
            restaurantWinner.setDatetime(dateFormat.format(ballotDate));

            Collections.shuffle(restaurantChoicesAfter1);
            BallotInterface winner = new BallotAfter(restaurantWinner, restaurantChoicesAfter1);
            return new ResponseEntity<BallotInterface>(winner, HttpStatus.OK);
        }
    }

    public ResponseEntity<String> createBallot(InitialBallot initialBallot) {
        String ballotId = null;
        logger.debug("Here is the initialBallot");
        logger.debug(initialBallot.getEndTime());
        logger.debug(initialBallot.getVoters().get(0).getName());
        logger.debug(initialBallot.getVoters().get(0).getEmailAddress());

        Ballot ballot = null;
        try {
            ballot = getNewBallot(initialBallot);
        } catch (ParseException e) {
            e.printStackTrace();
            ballotId = "Error with parsing the input date. It could be that it wasn't in this format: MM/dd/yy HH:mm";
            logger.error("Error with parsing the input date. It could be that it wasn't in this format: MM/dd/yy HH:mm");
            return new ResponseEntity<String>(ballotId, HttpStatus.BAD_REQUEST);
        }
        ballotId = "{\n\t\"ballotId\":\"" + ballot.getBallotId().toString() + "\"\n}"; // Format it like JSON
        return new ResponseEntity<String>(ballotId, HttpStatus.CREATED);
    }
    private Ballot getNewBallot(InitialBallot initialBallot) throws ParseException { //Ballot ballot = createBallot.getBallot() is how you access the ballot
        ballotId = UUID.randomUUID();
        Ballot ballot = getBallot();
        ballot.setBallotId(ballotId);

        //todo: add support for 12 hr time and update readme
        endTime = initialBallot.getEndTime();
        Date date = new SimpleDateFormat("MM/dd/yy HH:mm").parse(endTime);
        ballot.setTime(date);

        ballot.setVoters(initialBallot.getVoters());
        RestaurantService restaurantService = new RestaurantService();

        ArrayList<IncomingRestaurant> randomRestaurants = restaurantService.getRestaurants();
        ballot.setRestaurants(randomRestaurants);

        return ballot;
    }

    public static Ballot getBallot() {
        return Ballot.getBallot();
    }
}
