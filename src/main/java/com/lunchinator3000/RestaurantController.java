package com.lunchinator3000;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class RestaurantController {
    private UUID ballotId;

    public RestaurantController() {

    }
    public @ResponseBody ArrayList<IncomingRestaurant>  getRestaurants() throws IOException { //todo: this return value should probably change to the suggestion, choices, winner, and choices
        ArrayList<IncomingRestaurant> incomingRestaurants = null;
        ArrayList<IncomingRestaurant> fiveRandomRestaurants = null;
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews1 = null;
        ArrayList<Integer> averageRatings = null;
        RestaurantSuggestion restaurantSuggestion = null;
          //time = Time.valueOf()
        ObjectMapper mapper = new ObjectMapper();
          //RestTemplate restTemplate = new RestTemplate();
          ////Ballot ballot = new Ballot();
          //String result = restTemplate.getForObject(uri, String.class);
          //String result = "{'name' : 'mkyong'}";
          //result.replaceAll("\\[", "").replaceAll("\\]", "");
        incomingRestaurants = mapper.readValue(new URL("https://interview-project-17987.herokuapp.com/api/restaurants"), new ArrayList<IncomingRestaurant>().getClass());
          ////Ballot ballot = restTemplate.getForObject(uri, ballot);

          //HttpHeaders headers = new HttpHeaders();
          //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
          //HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

          //ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);


          //System.out.println(result);
        //System.out.println(incomingRestaurants);

        //ballotId = UUID.randomUUID();

        //get the five random restaurants
        fiveRandomRestaurants = randomlyPick5Restaurants(incomingRestaurants);

        //get restaurant reviews
        RestaurantsReviews restaurantsReviews = new RestaurantsReviews();
        restaurantsReviews1 = restaurantsReviews.getRestaurantsReviews(fiveRandomRestaurants);

        //get the average ratings for the five random restaurants
        averageRatings = getAverageRestaurantRating(restaurantsReviews1);

        //get the restaurant suggestion,
        // We need averageRatings to get the average rating and restarantsReviews to get what the reviewers say
        restaurantSuggestion = getRestaurantSuggestion(averageRatings, restaurantsReviews1);

        //todo: make variables and methods for the RestaurantChoiceBefore, After, and Winner too

        return fiveRandomRestaurants; //return new ResponseEntity<Ballot>(ballot, HttpStatus.OK); //this return value should probably change to the suggestion, choices, winner, and choices
    }
    public @ResponseBody ArrayList<IncomingRestaurant> randomlyPick5Restaurants(ArrayList<IncomingRestaurant> incomingRestaurants) {
        Random random;
        ArrayList<IncomingRestaurant> randomRestaurants = new ArrayList<IncomingRestaurant>();
        for(int i = 0; i < 5; i++) {
            random = new Random();
            randomRestaurants.add(incomingRestaurants.get(random.nextInt(incomingRestaurants.size())));
        }
        return randomRestaurants;
    }

    public ArrayList<Integer> getAverageRestaurantRating(ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {
        ArrayList<Integer> averageRatings = null;
        Integer averageRating = 0;
        Float actualAverageRating = Float.valueOf(0);

        for (int i = 0; i < restaurantsReviews.size(); i++) {
            for (int j = 0; j < restaurantsReviews.get(i).size(); j++) {
                averageRating += restaurantsReviews.get(i).get(j).getRating();
            }
            actualAverageRating = averageRating.floatValue()/(restaurantsReviews.get(i).size()-1);
            averageRating = Math.round(actualAverageRating);
            averageRatings.add(averageRating);
            averageRating = 0;

        }

        return averageRatings;
    }
    public RestaurantSuggestion getRestaurantSuggestion(ArrayList<Integer> averageRatings, ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {

        return new RestaurantSuggestion(1, "hi", 2, "Heather", "akjsdl");
    }
}
