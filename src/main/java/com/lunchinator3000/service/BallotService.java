package com.lunchinator3000.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.controllers.VoteController;
import com.lunchinator3000.dto.ballot.BallotInterface;
import com.lunchinator3000.dto.restaurant.*;
import com.lunchinator3000.dto.vote.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lunchinator3000.service.BallotService.Ballot1.getBallot1;

public class BallotService {

    private static ArrayList<IncomingRestaurant> randomRestaurants;
    private static UUID ballotId;
    private static String endTime;

    public static ResponseEntity<BallotInterface> getBallot(UUID ballotId) {
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();
        ArrayList<Integer> averageRatings;
        RestaurantSuggestion restaurantSuggestion = new RestaurantSuggestion();

        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = new ArrayList();
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList();
        // To make them compatible with the created interfaces
        ArrayList<RestaurantChoice> restaurantChoicesAfter1 = new ArrayList();
        ArrayList<RestaurantChoice> restaurantChoicesBefore1 = new ArrayList();

        RestaurantWinner restaurantWinner = new RestaurantWinner();

        BallotService ballotService = new BallotService();
        BallotService.Ballot1 ballot = ballotService.getBallot();

        System.out.println("In the getBallot() method.");


        System.out.println("Creating new restaurant controller");
        RestaurantService restaurantService = new RestaurantService();
        randomRestaurants = ballot.getRestaurants();
        restaurantsReviews = restaurantService.getRestaurantsReviews(randomRestaurants);

        // If there was a ballot created
        System.out.println(ballotId);
        System.out.println(ballot.getBallotId());
        if (ballotId.toString().equals(ballot.getBallotId().toString())){
            // Continue
        }

        else { // If there was not a ballot created
            BallotService.BallotError ballotError = new BallotService.BallotError();
            ballotError.setMessage("There is no ballotId (which probably means you haven't created a ballot or requested a previous one).");
            BallotInterface message = ballotError;
            return new ResponseEntity<BallotInterface>(message, HttpStatus.BAD_REQUEST);
        }

        System.out.println("Getting averageRatings");
        averageRatings = restaurantService.getAverageRestaurantRating(restaurantsReviews);

        restaurantSuggestion = restaurantService.getRestaurantSuggestion(averageRatings, restaurantsReviews, randomRestaurants);
        restaurantChoicesBefore = restaurantService.getRestaurantChoiceBefore(averageRatings, randomRestaurants);

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
            BallotInterface suggestion = new BallotBefore(restaurantSuggestion, restaurantChoicesBefore1);
            return new ResponseEntity<BallotInterface>(suggestion, HttpStatus.OK);
        }
        else {// After the current date
            VoteService voteService = new VoteService();
            HashMap<String,Vote> votes = voteService.getVotes();

            restaurantChoicesAfter = restaurantService.getRestaurantChoicesAfter(votes, restaurantChoicesBefore);

            // Make it so the restaurantChoicesAfter is compatible with the restaurantWinner object
            System.out.println("Here are the restaruantChoicesAfter");
            for (int i = 0; i < restaurantChoicesAfter.size(); i++) {
                System.out.println(restaurantChoicesAfter.get(i).getName());
                System.out.println(restaurantChoicesAfter.get(i).getVotes());
                restaurantChoicesAfter1.add(restaurantChoicesAfter.get(i));
            }
            restaurantWinner = restaurantService.getRestaurantWinner(restaurantChoicesAfter);
            restaurantWinner.setDatetime(dateFormat.format(ballotDate));

            Collections.shuffle(restaurantChoicesAfter1);
            BallotInterface winner = new BallotAfter(restaurantWinner, restaurantChoicesAfter1);
            return new ResponseEntity<BallotInterface>(winner, HttpStatus.OK);
        }
    }

    public static ResponseEntity<String> createBallot(BallotService.InitialBallot1 initialBallot) {
        String ballotId = null;
        System.out.println("Here is the initialBallot");
        System.out.println(initialBallot.getEndTime());
        System.out.println(initialBallot.getVoters().get(0).getName());
        System.out.println(initialBallot.getVoters().get(0).getEmailAddress());

        BallotService.Ballot1 ballot = null;
        try {
            ballot = getNewBallot(initialBallot);
        } catch (ParseException e) {
            e.printStackTrace();
            ballotId = "Error with parsing the input date. It could be that it wasn't in this format: MM/dd/yy HH:mm";
            return new ResponseEntity<String>(ballotId, HttpStatus.BAD_REQUEST);
        }
        ballotId = "{\n\t\"ballotId\":\"" + ballot.getBallotId().toString() + "\"\n}"; // Format it like JSON
        return new ResponseEntity<String>(ballotId, HttpStatus.CREATED);
    }
    private static BallotService.Ballot1 getNewBallot(BallotService.InitialBallot1 initialBallot) throws ParseException { //Ballot1 ballot1 = createBallot.getBallot() is how you access the ballot
        ballotId = UUID.randomUUID();
        BallotService.Ballot1 ballot1 = getBallot1();
        ballot1.setBallotId(ballotId);

        //todo: add support for 12 hr time and update readme
        endTime = initialBallot.getEndTime();
        Date date = new SimpleDateFormat("MM/dd/yy HH:mm").parse(endTime);
        ballot1.setTime(date);

        ballot1.setVoters(initialBallot.getVoters());
        RestaurantService restaurantService = new RestaurantService();

        ArrayList<IncomingRestaurant> randomRestaurants = restaurantService.getRestaurants();
        ballot1.setRestaurants(randomRestaurants);

        return ballot1;
    }

    public BallotService.Ballot1 getBallot() { //CreateBallot creatBallot; Ballot1 ballot1 = createBallot.getBallot(); is how you access the ballot
        return getBallot1();
    }

    /**
     * This is a sub-class of CreateBallot because in order for the springframework to notice an incoming JSON object,
     * there needs to be an inner static class that looks like the object and that has an empty constructor.
     */
    public static class InitialBallot1 {
        private String endTime;
        private ArrayList<BallotService.Voter1> voters;

        public InitialBallot1(String endTime, ArrayList<BallotService.Voter1> voters) {
            this.voters = voters;
            this.endTime = endTime;
        }
        public InitialBallot1() {

        }

        public ArrayList<BallotService.Voter1> getVoters() {
            return voters;
        }

        public void setVoters(ArrayList<BallotService.Voter1> voters) {
            this.voters = voters;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String time) {
            this.endTime = time;
        }
    }

    /**
     * This is a sub-class of CreateBallot because in order for the springframework to notice an incoming JSON object,
     * there needs to be an inner static class that looks like the object and that has an empty constructor.
     */
    public static class Voter1 {
        private String name;
        private String emailAddress;

        public Voter1(String name, String email) {
            this.name = name;
            this.emailAddress = email;
        }

        public Voter1() {
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String email) {
            this.emailAddress = email;
        }
    }

    /**
     * This is a sub-class of CreateBallot because in order for the springframework to notice an incoming JSON object,
     * there needs to be an inner static class that looks like the object and that has an empty constructor (Voter1 -
     * which needs to be included in the Ballot object).
     */
    public static class Ballot1 {
        private UUID ballotId;
        private Date endTime;
        private ArrayList<BallotService.Voter1> voters;
        private ArrayList<IncomingRestaurant> restaurants;

        private static Ballot1 ballot1;

        private Ballot1(UUID ballotId, Date time, ArrayList<BallotService.Voter1> voters, ArrayList<IncomingRestaurant> restaurants) {
            this.ballotId = ballotId;
            this.endTime = time;
            this.voters = voters;
            this.restaurants = restaurants;
        }

        // Prevents any other class from instantiating it
        private Ballot1() {
        }

        // Provides a global point of access
        public static Ballot1 getBallot1() {

            //todo: include the daily new making logic here or maybe in getNewBallot()
            if (null == ballot1) {
                ballot1 = new Ballot1();
            }
            return ballot1;
        }

        public UUID getBallotId() {
            return ballotId;
        }

        public void setBallotId(UUID ballotId) {
            this.ballotId = ballotId;
        }

        public Date getTime() {
            return endTime;
        }

        public void setTime(Date time) {
            this.endTime = time;
        }

        public ArrayList<BallotService.Voter1> getVoters() {
            return voters;
        }

        public void setVoters(ArrayList<BallotService.Voter1> voters) {
            this.voters = voters;
        }

        public ArrayList<IncomingRestaurant> getRestaurants() {
            return restaurants;
        }

        public void setRestaurants(ArrayList<IncomingRestaurant> restaurants) {
            this.restaurants = restaurants;
        }
    }

    /**
     *  This class' ArrayList will hold the suggestion and the choices before time is up for the ballot so that it can
     *  display any type of RestaurantInterface and return it in one object
     */
    public static class BallotBefore implements BallotInterface {
        private RestaurantSuggestion suggestion;
        private ArrayList<RestaurantChoice> restaurantChoices;


        public BallotBefore(RestaurantSuggestion suggestion, ArrayList<RestaurantChoice> restaurantChoices) {
            this.suggestion = suggestion;
            this.restaurantChoices = restaurantChoices;
        }
        public BallotBefore() {
        }

        public RestaurantInterface getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(RestaurantSuggestion suggestion) {
            this.suggestion = suggestion;
        }

        public ArrayList<RestaurantChoice> getChoices() {
            return restaurantChoices;
        }

        public void setChoices(ArrayList<RestaurantChoice> restaurantChoices) {
            this.restaurantChoices = restaurantChoices;
        }

    }

    /**
     *  This class' ArrayList will hold the winner and the choices after time is up for the ballot so that it can
     *  display any type of RestaurantInterface and return it in one object
     */
    public static class BallotAfter implements BallotInterface{
        private RestaurantWinner winner;
        private ArrayList<RestaurantChoice> restaurantChoices;


        public BallotAfter(RestaurantWinner winner, ArrayList<RestaurantChoice> restaurantChoices) {
            this.winner = winner;
            this.restaurantChoices = restaurantChoices;
        }

        public BallotAfter() {
        }

        public RestaurantInterface getWinner() {
            return winner;
        }

        public void setWinner(RestaurantWinner winner) {
            this.winner = winner;
        }

        public ArrayList<RestaurantChoice> getChoices() {
            return restaurantChoices;
        }

        public void setChoices(ArrayList<RestaurantChoice> restaurantChoices) {
            this.restaurantChoices = restaurantChoices;
        }
    }

    /**
     *  This class' message will hold the error message so that it can
     *  display any type of BallotInterface and return it in one object
     */
    public static class BallotError implements BallotInterface{
        private String message = "Error";


        public BallotError(String message) {
            this.message = message;
        }

        public BallotError() {
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
