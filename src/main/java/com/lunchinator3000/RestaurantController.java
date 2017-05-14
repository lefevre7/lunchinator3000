package com.lunchinator3000;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class RestaurantController {
    private UUID ballotId;
    //private ArrayList<IncomingRestaurant> incomingRestaurants;

    public RestaurantController() {

    }

    public @ResponseBody
    ArrayList<IncomingRestaurant> getRestaurants() { //todo: this return value should probably change to the suggestion, choices, winner, and choices
        ArrayList<IncomingRestaurant> incomingRestaurants = new ArrayList<IncomingRestaurant>();
        ArrayList<IncomingRestaurant> fiveRandomRestaurants = null;
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews1 = new ArrayList<ArrayList<RestaurantReview>>();
        ArrayList<Integer> averageRatings = new ArrayList<>();
        RestaurantSuggestion restaurantSuggestion = new RestaurantSuggestion();
        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = new ArrayList<>();
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();
        RestaurantWinner restaurantWinner = new RestaurantWinner();
        JsonNode rootNode = null;
        //time = Time.valueOf()

        ObjectMapper mapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("https://interview-project-17987.herokuapp.com/api/restaurants", String.class);

        System.out.println("Getting incomming restaurants");
        try {
            rootNode = mapper.readTree(result);
            System.out.println(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < rootNode.size(); i++){
            IncomingRestaurant incomingRestaurant = new IncomingRestaurant();
            incomingRestaurant.setId(rootNode.get(i).get("id").asInt());
            incomingRestaurant.setName(rootNode.get(i).get("name").asText());
            //incomingRestaurant.setWaitTimeMinutes(rootNode.get(i).get("waitTimeMinutes").asText());
            incomingRestaurant.setWaitTimeMinutes("none");
            incomingRestaurant.setDescription(rootNode.get(i).get("description").asText());

            System.out.println(incomingRestaurant);


            //incomingRestaurants.add(new IncomingRestaurant(rootNode.get(i).get("id").asInt(), rootNode.get(i).get("name").asText(), "none", rootNode.get(i).get("description").asText()));
            incomingRestaurants.add(incomingRestaurant);
        }

        //System.out.println(result);
        System.out.println("Trying to print incomingRestaurants");
        System.out.println(incomingRestaurants);

        //get the five random restaurants
        System.out.println("Getting 5 random restaurants");
        fiveRandomRestaurants = randomlyPick5Restaurants(incomingRestaurants);
        System.out.println("Printing five restaurants");
        System.out.println(fiveRandomRestaurants.get(0).getName().toString());
        System.out.println(fiveRandomRestaurants.get(1).getName().toString());
        System.out.println(fiveRandomRestaurants.get(2).getName().toString());
        System.out.println(fiveRandomRestaurants.get(3).getName().toString());
        System.out.println(fiveRandomRestaurants.get(4).getName().toString());
        //get restaurant reviews
        System.out.println("Instantiating RestaurantReviews class");
       // RestaurantsReviews restaurantsReviews = new RestaurantsReviews();


        restaurantsReviews1 = getRestaurantsReviews(fiveRandomRestaurants);

        //get the average ratings for the five random restaurants
        System.out.println("Getting averageRatings");
        averageRatings = getAverageRestaurantRating(restaurantsReviews1);

        //get the restaurant suggestion,
        // We need averageRatings to get the average rating and restarantsReviews to get what the reviewers say
        restaurantSuggestion = getRestaurantSuggestion(averageRatings, restaurantsReviews1);
        restaurantChoicesBefore = getRestaurantChoiceBefore(averageRatings, fiveRandomRestaurants);

        //Collections.shuffle(restaurantChoicesBefore);// restaurantChoicesBefore are now randomized // do this when you put it in the object that gets sent back to the user

        VoteController voteController = new VoteController();
        HashMap<String,Vote> votes = voteController.getVotes();

        restaurantChoicesAfter = getRestaurantChoicesAfter(votes, restaurantChoicesBefore);
        //restaurantWinner = findRestaurantWinner(votes, restaurantChoicesBefore);



        //todo: make variables and methods for the RestaurantChoiceBefore, After, and Winner too

        return fiveRandomRestaurants; //return new ResponseEntity<Ballot>(ballot, HttpStatus.OK); //todo: this return value should probably change to the suggestion, choices, winner, and choices (aka BallotBeforeOrAfter)
    }
    public ArrayList<RestaurantChoiceAfter> getRestaurantChoicesAfter(HashMap<String,Vote> votes, ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore){
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();
        ArrayList<Vote> actualVotes = new ArrayList<Vote>(votes.values());
        ArrayList<Integer> individualVotes = new ArrayList<Integer>();
        HashMap<String, Integer> restaurantVotes = new HashMap<>();
        ArrayList<AtomicInteger> atomicInteger = new ArrayList<>();

        // Get the restaurantId into the individualVotes array indexed by how the votes came in
        for(int i = 0; i < actualVotes.size(); i++) {
            individualVotes.add(actualVotes.get(i).getRestaurantId());
        }

        // When a vote equals a before-choice-restaurant's id, then set it in a hashmap that has the restaurant's name
        // as the key, and a counter that goes with (is dependant on) the order of the restaurantChoicesBefore's rests
        for(int i = 0; i < individualVotes.size(); i++){
            for (int j = 0; j < restaurantChoicesBefore.size(); j++)
            if (individualVotes.get(i) == restaurantChoicesBefore.get(j).getId()){
                if(restaurantVotes.containsKey(restaurantChoicesBefore.get(j).getName()))
                    //stored the restaurants now by their names (not id's)
                    restaurantVotes.replace(restaurantChoicesBefore.get(j).getName(), atomicInteger.get(j).getAndIncrement());
                else
                    restaurantVotes.put(restaurantChoicesBefore.get(j).getName(), atomicInteger.get(j).getAndIncrement());
            }


        }
        return restaurantChoicesAfter;
    }

    /*public RestaurantWinner findRestaurantWinner(HashMap<String,Vote> votes, ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore){
        votes.values().
    }*/

    public  ArrayList<ArrayList<RestaurantReview>> getRestaurantsReviews(ArrayList<RestaurantController.IncomingRestaurant> incomingRestaurants) {
        //ArrayList<IncomingRestaurant> incomingRestaurants = null;
        //ArrayList<RestaurantReview> restaurantReviews = new ArrayList<RestaurantReview>();
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();

        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < incomingRestaurants.size(); i++) {
            ArrayList<RestaurantReview> restaurantReviews = new ArrayList<RestaurantReview>();
            System.out.println(incomingRestaurants.get(i).getName());
            String url = "https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName().replaceAll(" ", "%20");
            System.out.println(url);

            /*try {
                restaurantReviews = mapper.readValue(new URL(url), new ArrayList<RestaurantReview>().getClass());
            } catch (IOException e) {
                System.out.println("The webaddress: " + url + " threw an error");
                e.printStackTrace();
            }*/

            JsonNode rootNode = null;
            //HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            //requestFactory.setReadTimeout(50);
            RestTemplate restTemplate = new RestTemplate();


            // Prepare acceptable media type
            List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON); // Set what you need

            // Prepare header
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(acceptableMediaTypes);
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            // Send the request as GET
            try {
                ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                // Add to model
                //model.addAttribute("persons", result.getBody().getData());

            } catch (Exception e) {
                e.printStackTrace();
            }

            String tem = incomingRestaurants.get(i).getName().replaceAll(" ", "%20");
            System.out.println(tem);

            String result = restTemplate.getForObject(url, String.class);
            if (result.equals("[]"))//could try while
                result = restTemplate.getForObject("https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName().replaceAll(" ", "%20"), String.class);
            System.out.println(result);

            URI url1 = URI.create("https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName().replaceAll(" ", "%20"));
            if (result.equals("[]"))//could try while
                result = restTemplate.getForObject(url1, String.class);

            //JSONObject json = readJsonFromUrl("https://graph.facebook.com/19292868552");


            System.out.println("Getting incoming restaurantReviews");
            try {
                rootNode = mapper.readTree(result);
                System.out.println(rootNode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < rootNode.size(); j++) {
                RestaurantReview restaurantReview = new RestaurantReview();
                restaurantReview.setId(rootNode.get(j).get("Id").asInt());
                restaurantReview.setRestaurant(rootNode.get(j).get("restaurant").asText());
                restaurantReview.setReviewer(rootNode.get(j).get("reviewer").asText());
                restaurantReview.setRating(rootNode.get(j).get("rating").asInt());
                restaurantReview.setReview(rootNode.get(j).get("review").asText());
                System.out.println("Here is the restaurant review");
                System.out.println(restaurantReview);

                restaurantReviews.add(restaurantReview);
            }
            System.out.println("Here are the reviews of the restaurants");
            System.out.println(restaurantReviews);
            restaurantsReviews.add(restaurantReviews);
            //restaurantReviews.clear();
        }
        return restaurantsReviews;
    }
    //can make almost all methods here private (because they're only used by this one RestaurantController class) if I have time
    public /*@ResponseBody*/
    ArrayList<IncomingRestaurant> randomlyPick5Restaurants(ArrayList<IncomingRestaurant> incomingRestaurants) {
        ArrayList<IncomingRestaurant> randomTemp = incomingRestaurants;
        ArrayList<IncomingRestaurant> randomRestaurants = new ArrayList<>();
        Collections.shuffle(randomTemp);
        for (int i = 0; i < 5; i++)
            randomRestaurants.add(randomTemp.get(i));

        return randomRestaurants;
    }

    public ArrayList<Integer> getAverageRestaurantRating(ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {
        ArrayList<Integer> averageRatings = new ArrayList<>();
        Integer averageRating = 0;
        Float actualAverageRating = Float.valueOf(0);
        int restaurantsReviewsSize = restaurantsReviews.size();



        for (int i = 0; i < restaurantsReviewsSize; i++) {
            int restaurantReviewsSize = restaurantsReviews.get(i).size();
            for (int j = 0; j < restaurantReviewsSize; j++) {
                int rating = restaurantsReviews.get(i).get(j).getRating();
                averageRating = averageRating + rating;
            }
            actualAverageRating = averageRating.floatValue() / (restaurantsReviews.get(i).size() - 1);
            averageRating = Math.round(actualAverageRating);
            averageRatings.add(averageRating);
            averageRating = 0;

        }

        return averageRatings;
    }

    public RestaurantSuggestion getRestaurantSuggestion(ArrayList<Integer> averageRatings, ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {
        // The index in the ArrayList the max rated restaurant is
        Integer index = null;

        RestaurantReview TopReview = null;
        // Get the max integer
        Integer max = Collections.max(averageRatings);

        // Get the max integer's index
        for (int i = 0; i < averageRatings.size(); i++) {
            if (max == averageRatings.get(i)) {
                index = i;
                break;
            }
        }
        TopReview = getRestaurantSuggestionTopReviewer(index, restaurantsReviews);

        //returns the id of the [first] restaurant [review of the restaurant with the index], the name of the first
        // restaurant with that index (its name), restaurant's review with the index of the highest rating, the top
        // reviewer's name, and their review

        //I could replace the first two with TopReview.getId() and TopReview.getRestaurant()
        return new RestaurantSuggestion(restaurantsReviews.get(index).get(0).getId(), restaurantsReviews.get(index).get(0).getRestaurant(), averageRatings.get(index), TopReview.getReviewer(), TopReview.getReview());
    }

    public RestaurantReview getRestaurantSuggestionTopReviewer(Integer index, ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {
        RestaurantReview restaurantReview = new RestaurantReview();
        //ArrayList<RestaurantReview> restaurantReviews = restaurantsReviews.get(index);
        //Integer max = Collections.max(restaurantReviews.);

        System.out.println("Here is the index of the max rating");
        System.out.println(index);

        System.out.println("Here is the size of the restaurantsReviews");
        System.out.println(restaurantsReviews.size());

        Integer max = 0;
        int maxTemp = 0;
        Integer index1 = 0;
        // Find max review
        for (int i = 0; i < restaurantsReviews.get(index).size(); i++) {
            // Find max review in restaurantsReviews.get(index)
            if ((maxTemp = restaurantsReviews.get(index).get(i).getRating()) > max)
                max = maxTemp;
        }

        // Get the max review's index
        for (int i = 0; i < restaurantsReviews.get(index).size(); i++) {
            if (max == restaurantsReviews.get(index).get(i).getRating()) {
                index1 = i;
                break;
            }
        }
        return restaurantsReviews.get(index).get(index1);
    }

    public ArrayList<RestaurantChoiceBefore> getRestaurantChoiceBefore(ArrayList<Integer> averageRatings, ArrayList<IncomingRestaurant> fiveRandomRestaurants) {
        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            restaurantChoicesBefore.add(new RestaurantChoiceBefore(fiveRandomRestaurants.get(i).getId(), fiveRandomRestaurants.get(i).getName(), averageRatings.get(i), fiveRandomRestaurants.get(i).getDescription()));

        return restaurantChoicesBefore;
    }

    /**
     * Created by Jeremy L on 5/11/2017.
     */
    abstract static class AbstractRestaurant {
        private String name;
        private int id;

        public AbstractRestaurant() {

        }
        public AbstractRestaurant(int id, String name) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    /**
     * Created by Jeremy L on 5/11/2017.
     */
    public static class IncomingRestaurant extends AbstractRestaurant {
        private String waitTimeMinutes;
        private String description;

        public IncomingRestaurant() {
            super();

        }

        public IncomingRestaurant(int id, String name, String waitTimeMinutes, String description) {
            super(id, name);
            this.waitTimeMinutes = waitTimeMinutes;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWaitTimeMinutes() {
            return waitTimeMinutes;
        }

        public void setWaitTimeMinutes(String waitTimeMinutes) {
            this.waitTimeMinutes = waitTimeMinutes;
        }
    }
}