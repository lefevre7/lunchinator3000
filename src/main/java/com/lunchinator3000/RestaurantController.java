package com.lunchinator3000;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

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
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews1 = null;
        ArrayList<Integer> averageRatings = null;
        RestaurantSuggestion restaurantSuggestion = null;
        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = null;
        JsonNode rootNode = null;
        //time = Time.valueOf()

        ObjectMapper mapper = new ObjectMapper();

        RestTemplate restTemplate = new RestTemplate();
        ////Ballot ballot = new Ballot();
        String result = restTemplate.getForObject("https://interview-project-17987.herokuapp.com/api/restaurants", String.class);


        //String result = "{'name' : 'mkyong'}";
        //result.replaceAll("\\[", "").replaceAll("\\]", "");

        /*try {
            incomingRestaurants = new ObjectMapper().readValue(result, new ArrayList<IncomingRestaurant>().getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


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


        /*try {
            incomingRestaurants = mapper.readValue(new URL("https://interview-project-17987.herokuapp.com/api/restaurants"), ArrayList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        ////Ballot ballot = restTemplate.getForObject(uri, ballot);

        //HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        //ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);


        //System.out.println(result);
        System.out.println("Trying to print incomingRestaurants");
        System.out.println(incomingRestaurants);

        //ballotId = UUID.randomUUID();

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
        RestaurantsReviews restaurantsReviews = new RestaurantsReviews();


        restaurantsReviews1 = restaurantsReviews.getRestaurantsReviews(fiveRandomRestaurants);

        //get the average ratings for the five random restaurants
        System.out.println("Getting averageRatings");
        averageRatings = getAverageRestaurantRating(restaurantsReviews1);

        //get the restaurant suggestion,
        // We need averageRatings to get the average rating and restarantsReviews to get what the reviewers say
        restaurantSuggestion = getRestaurantSuggestion(averageRatings, restaurantsReviews1);
        restaurantChoicesBefore = getRestaurantChoiceBefore(averageRatings, fiveRandomRestaurants);

        //todo: make variables and methods for the RestaurantChoiceBefore, After, and Winner too

        return fiveRandomRestaurants; //return new ResponseEntity<Ballot>(ballot, HttpStatus.OK); //this return value should probably change to the suggestion, choices, winner, and choices
    }
    //can make almost all methods here private (because they're only used by this one RestaurantController class) if I have time
    public /*@ResponseBody*/
    ArrayList<IncomingRestaurant> randomlyPick5Restaurants(ArrayList<IncomingRestaurant> incomingRestaurants) {
        ArrayList<IncomingRestaurant> randomRestaurants = incomingRestaurants;
        Collections.shuffle(randomRestaurants);
        randomRestaurants = (ArrayList<IncomingRestaurant>) randomRestaurants.subList(0, 4);
        return randomRestaurants;
    }

    public ArrayList<Integer> getAverageRestaurantRating(ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {
        ArrayList<Integer> averageRatings = new ArrayList<>();
        Integer averageRating = 0;
        Float actualAverageRating = Float.valueOf(0);

        for (int i = 0; i < restaurantsReviews.size(); i++) {
            for (int j = 0; j < restaurantsReviews.get(i).size(); j++) {
                averageRating += restaurantsReviews.get(i).get(j).getRating();
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