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

    private final AtomicLong counter = new AtomicLong();

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
            ballotId = "Time in the wrong format (supposed to be: MM/dd/YYY HH:mm)";
        }
        ballotId = "{\n\t\"ballotId\":\"" + ballot.getBallotId().toString() + "\"\n}"; // Format it like JSON
        return new ResponseEntity<String>(ballotId, HttpStatus.CREATED);
    }
    private Ballot1 getNewBallot(InitialBallot1 initialBallot) throws ParseException { //Ballot1 ballot1 = createBallot.getBallot() is how you access the ballot
        ballotId = UUID.randomUUID();
        Ballot1 ballot1 = getBallot1();
        ballot1.setBallotId(ballotId);

        endTime = initialBallot.getEndTime();
        Date date = new SimpleDateFormat("MM/dd/yy HH:mm").parse(endTime);
        ballot1.setTime(date);

        System.out.println("Here is the initialBallot time");
        System.out.println(endTime);

        ballot1.setVoters(initialBallot.getVoters());

        System.out.println("Here is what getBallot1() does");
        Ballot1 anotherBallot = getBallot1();
        System.out.println(anotherBallot);

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
        private static Ballot1 ballot1;

        //private BallotAfter ballotAfter;
        //private BallotBefore ballotBefore;

        private BallotBeforeOrAfter ballotBeforeOrAfter;

        private Ballot1(UUID ballotId, Date time, ArrayList<Voter1> voters) {
            this.ballotId = ballotId;
            this.endTime = time;
            this.voters = voters;
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

        public BallotBeforeOrAfter getBallotBeforeOrAfter() {
            return ballotBeforeOrAfter;
        }

        public void setBallotBeforeOrAfter(BallotBeforeOrAfter ballotBeforeOrAfter) {
            this.ballotBeforeOrAfter = ballotBeforeOrAfter;
        }


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
    public static class BallotBeforeOrAfter {
        private ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> ballotBeforeOrAfter;

        public BallotBeforeOrAfter(ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> ballotBeforeOrAfter) {
            this.ballotBeforeOrAfter = ballotBeforeOrAfter;
        }

        public ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> getBallotBeforeOrAfter() {
            return ballotBeforeOrAfter;
        }

        public void setBallotBeforeOrAfter(ArrayList<ArrayList<RestaurantController.AbstractRestaurant>> ballotBeforeOrAfter) {
            this.ballotBeforeOrAfter = ballotBeforeOrAfter;
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