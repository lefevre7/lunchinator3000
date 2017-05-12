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
    private ArrayList<Voter1> voters;

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/api/create-ballot", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<String> createBallot(/*@JsonProperty("endTime")*/ @RequestBody InitialBallot1 initialBallot) {

        Ballot1 ballot = getBallot(initialBallot);
        String ballotId = "{\n\t\"ballotId\":\"" + ballot.getBallotId().toString() + "\"\n}";
        return new ResponseEntity<String>(ballotId, HttpStatus.CREATED);
    }
    public Ballot1 getBallot(InitialBallot1 initialBallot) {
        final String uri = "https://interview-project-17987.herokuapp.com/api/restaurants";
        ballotId = UUID.randomUUID();
        //time = Time.valueOf()

        //RestTemplate restTemplate = new RestTemplate();
        //Ballot ballot = new Ballot();
        //String result = restTemplate.getForObject(uri, String.class);
        //Ballot ballot = restTemplate.getForObject(uri, ballot);

        //System.out.println(result);
        return new Ballot1(ballotId, initialBallot.getTime(), initialBallot.getVoters());
    }

    /**
     * This is a sub-class of CreateBallot because in order for the springframework to notice an incoming JSON object,
     * there needs to be an inner static class that looks like the object and that has an empty constructor.
     */
    public static class InitialBallot1 {
        private Date time;
        private ArrayList<Voter1> voters;

        public InitialBallot1(Date time, ArrayList<Voter1> voters) {
            this.voters = voters;
            this.time = time;
        }
        public InitialBallot1() {

        }

        public ArrayList<Voter1> getVoters() {
            return voters;
        }

        public void setVoters(ArrayList<Voter1> voters) {
            this.voters = voters;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

    /**
     * This is a sub-class of CreateBallot because in order for the springframework to notice an incoming JSON object,
     * there needs to be an inner static class that looks like the object and that has an empty constructor.
     */
    public static class Voter1 {
        private String name;
        private String email;

        public Voter1(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Voter1() {
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    /**
     * This is a sub-class of CreateBallot because in order for the springframework to notice an incoming JSON object,
     * there needs to be an inner static class that looks like the object and that has an empty constructor (Voter1 -
     * which needs to be included in the Ballot object).
     */
    public static class Ballot1 {
        private UUID ballotId;
        private Date time;
        private ArrayList<Voter1> voters;

        private ArrayList<IncomingRestaurant> incomingRestaurants;
        private RestaurantSuggestion restaurantSuggestion;
        private ArrayList<RestaurantChoiceBefore> restaurantChoiceBefores;
        private ArrayList<RestaurantChoiceAfter> restaurantChoiceAfters;
        private RestaurantWinner restaurantWinner;

        public Ballot1(UUID ballotId, Date time, ArrayList<Voter1> voters) {
            this.ballotId = ballotId;
            this.time = time;
            this.voters = voters;
        }

        public UUID getBallotId() {
            return ballotId;
        }

        public void setBallotId(UUID ballotId) {
            this.ballotId = ballotId;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public ArrayList<Voter1> getVoters() {
            return voters;
        }

        public void setVoters(ArrayList<Voter1> voters) {
            this.voters = voters;
        }

        public ArrayList<IncomingRestaurant> getIncomingRestaurants() {
            return incomingRestaurants;
        }

        public void setIncomingRestaurants(ArrayList<IncomingRestaurant> incomingRestaurants) {
            this.incomingRestaurants = incomingRestaurants;
        }

        public RestaurantSuggestion getRestaurantSuggestion() {
            return restaurantSuggestion;
        }

        public void setRestaurantSuggestion(RestaurantSuggestion restaurantSuggestion) {
            this.restaurantSuggestion = restaurantSuggestion;
        }

        public ArrayList<RestaurantChoiceBefore> getRestaurantChoiceBefores() {
            return restaurantChoiceBefores;
        }

        public void setRestaurantChoiceBefores(ArrayList<RestaurantChoiceBefore> restaurantChoiceBefores) {
            this.restaurantChoiceBefores = restaurantChoiceBefores;
        }

        public ArrayList<RestaurantChoiceAfter> getRestaurantChoiceAfters() {
            return restaurantChoiceAfters;
        }

        public void setRestaurantChoiceAfters(ArrayList<RestaurantChoiceAfter> restaurantChoiceAfters) {
            this.restaurantChoiceAfters = restaurantChoiceAfters;
        }

        public RestaurantWinner getRestaurantWinner() {
            return restaurantWinner;
        }

        public void setRestaurantWinner(RestaurantWinner restaurantWinner) {
            this.restaurantWinner = restaurantWinner;
        }
    }


}