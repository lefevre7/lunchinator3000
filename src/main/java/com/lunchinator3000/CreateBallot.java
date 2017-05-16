package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/10/2017.
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lunchinator3000.CreateBallot.Ballot1.getBallot1;

@RestController
public class CreateBallot {
    private UUID ballotId;
    private String endTime;
    //private ArrayList<Voter1> voters;
    //private ArrayList<RestaurantController.IncomingRestaurant> randomRestaurants;
    //private Ballot1 ballot11 = getBallot1(); // So this CreateBallot class can be in-charge of the ballot

    //private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/api/create-ballot", method = RequestMethod.POST, headers="Accept=application/json", consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity<String> createBallot(/*@JsonProperty("endTime")*/ @RequestBody InitialBallot1 initialBallot) {
        String ballotId = null;
        System.out.println("Here is the initialBallot");
        System.out.println(initialBallot.getEndTime());
        System.out.println(initialBallot.getVoters().get(0).getName());
        System.out.println(initialBallot.getVoters().get(0).getEmailAddress());

        Ballot1 ballot = null;
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
    private Ballot1 getNewBallot(InitialBallot1 initialBallot) throws ParseException { //Ballot1 ballot1 = createBallot.getBallot() is how you access the ballot
        ballotId = UUID.randomUUID();
        Ballot1 ballot1 = getBallot1();
        ballot1.setBallotId(ballotId);

        System.out.println("Here is the ballotId");
        System.out.println(ballot1.getBallotId());

        endTime = initialBallot.getEndTime();
        Date date = new SimpleDateFormat("MM/dd/yy HH:mm").parse(endTime);
        ballot1.setTime(date);

        System.out.println("Here is the initialBallot time");
        System.out.println(endTime);

        System.out.println("Here is the ballot1 time");
        System.out.println(ballot1.getTime());


        ballot1.setVoters(initialBallot.getVoters());
        RestaurantController restaurantController = new RestaurantController(date);

        ArrayList<RestaurantController.IncomingRestaurant> randomRestaurants = restaurantController.getRestaurants();
        ballot1.setRestaurants(randomRestaurants);

        System.out.println("Here is what getBallot1() does");
        //Ballot1 anotherBallot = getBallot1();
        //System.out.println(anotherBallot);

        return ballot1; //new Ballot1(ballotId, initialBallot.getTime(), initialBallot.getVoters());
    }

    public Ballot1 getBallot() { //CreateBallot creatBallot; Ballot1 ballot1 = createBallot.getBallot(); is how you access the ballot
        return getBallot1();
    }

    /**
     * This is a sub-class of CreateBallot because in order for the springframework to notice an incoming JSON object,
     * there needs to be an inner static class that looks like the object and that has an empty constructor.
     */
    public static class InitialBallot1 {
        private String endTime;
        private ArrayList<Voter1> voters;

        public InitialBallot1(String endTime, ArrayList<Voter1> voters) {
            this.voters = voters;
            this.endTime = endTime;
        }
        public InitialBallot1() {

        }

        public ArrayList<Voter1> getVoters() {
            return voters;
        }

        public void setVoters(ArrayList<Voter1> voters) {
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
        private ArrayList<Voter1> voters;
        private ArrayList<RestaurantController.IncomingRestaurant> restaurants;

        private static Ballot1 ballot1;

        //private BallotAfter ballotAfter;
        //private BallotBefore ballotBefore;

        //private BallotBefore ballotBefore;

        private Ballot1(UUID ballotId, Date time, ArrayList<Voter1> voters, ArrayList<RestaurantController.IncomingRestaurant> restaurants) {
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

        public ArrayList<Voter1> getVoters() {
            return voters;
        }

        public void setVoters(ArrayList<Voter1> voters) {
            this.voters = voters;
        }

        public ArrayList<RestaurantController.IncomingRestaurant> getRestaurants() {
            return restaurants;
        }

        public void setRestaurants(ArrayList<RestaurantController.IncomingRestaurant> restaurants) {
            this.restaurants = restaurants;
        }

        /*public BallotBefore getBallotBefore() {
            return ballotBefore;
        }

        public void setBallotBefore(BallotBefore ballotBefore) {
            this.ballotBefore = ballotBefore;
        }*/


        /*public BallotAfter getBallotAfter() {
            return ballotAfter;
        }

        public void setBallotAfter(BallotAfter ballotAfter) {
            this.ballotAfter = ballotAfter;
        }

        public BallotBefore getBallotBefore() {
            return ballotBefore;
        }

        public void setBallotBefore(BallotBefore ballotBefore) {
            this.ballotBefore = ballotBefore;
        }*/
    }

    /**
     *  This class' ArrayList will hold the winner and the choices after time is up for the ballot so that it can
     *  display anytype of AbstractRestaurant and return it in one object
     */
    public static class BallotBefore implements BallotInterface{
        private RestaurantSuggestion suggestion;
        private ArrayList<RestaurantController.RestaurantChoice> restaurantChoices;


        public BallotBefore(RestaurantSuggestion suggestion, ArrayList<RestaurantController.RestaurantChoice> restaurantChoices) {
            this.suggestion = suggestion;
            this.restaurantChoices = restaurantChoices;
        }
        public BallotBefore() {
        }

        public RestaurantController.AbstractRestaurant getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(RestaurantSuggestion suggestion) {
            this.suggestion = suggestion;
        }

        public ArrayList<RestaurantController.RestaurantChoice> getChoices() {
            return restaurantChoices;
        }

        public void setChoices(ArrayList<RestaurantController.RestaurantChoice> restaurantChoices) {
            this.restaurantChoices = restaurantChoices;
        }

    }

    public static class BallotAfter implements BallotInterface{
        private RestaurantWinner winner;
        private ArrayList<RestaurantController.RestaurantChoice> restaurantChoices;


        public BallotAfter(RestaurantWinner winner, ArrayList<RestaurantController.RestaurantChoice> restaurantChoices) {
            this.winner = winner;
            this.restaurantChoices = restaurantChoices;
        }

        public BallotAfter() {
        }

        public RestaurantController.AbstractRestaurant getWinner() {
            return winner;
        }

        public void setWinner(RestaurantWinner winner) {
            this.winner = winner;
        }

        public ArrayList<RestaurantController.RestaurantChoice> getChoices() {
            return restaurantChoices;
        }

        public void setChoices(ArrayList<RestaurantController.RestaurantChoice> restaurantChoices) {
            this.restaurantChoices = restaurantChoices;
        }
    }
    /**
     *  This class' ArrayList will hold the suggestion and the choices before time is up for the ballot so that it can
     *  display anytype of AbstractRestaurant and return it in one object
     */
    /*public static class BallotBefore {
        private ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> before;

        public BallotBefore(ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> before) {
            this.before = before;
        }

        public ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> getBefore() {
            return before;
        }

        public void setBefore(ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> before) {
            this.before = before;
        }
    }*/

    /**
     *  This class' ArrayList will hold the winner and the choices after time is up for the ballot so that it can
     *  display anytype of AbstractRestaurant and return it in one object
     */
    /*public static class BallotAfter {
        private ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> after;

        public BallotAfter(ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> after) {
            this.after = after;
        }

        public ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> getAfter() {
            return after;
        }

        public void setAfter(ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> after) {
            this.after = after;
        }*/
}