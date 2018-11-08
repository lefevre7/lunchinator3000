package com.lunchinator3000.service;

/**
 * Created by Jeremy L on 5/11/2017.
 * RestaurantController provides functions to obtain the restaurant suggestions, reviews, choices, and winner.
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.dto.restaurant.IncomingRestaurant;
import com.lunchinator3000.dto.restaurant.RestaurantChoiceAfter;
import com.lunchinator3000.dto.restaurant.RestaurantChoiceBefore;
import com.lunchinator3000.dto.restaurant.RestaurantReview;
import com.lunchinator3000.dto.restaurant.RestaurantSuggestion;
import com.lunchinator3000.dto.restaurant.RestaurantWinner;
import com.lunchinator3000.dto.vote.Vote;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestaurantService {
    public RestaurantService() {
    }

    public @ResponseBody
    ArrayList<IncomingRestaurant> getRestaurants() {
        ArrayList<IncomingRestaurant> incomingRestaurants = new ArrayList<IncomingRestaurant>();
        ArrayList<IncomingRestaurant> fiveRandomRestaurants = null;

        JsonNode rootNode = null;

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

            incomingRestaurants.add(incomingRestaurant);
        }

        System.out.println("Trying to print incomingRestaurants");
        System.out.println(incomingRestaurants);

        //get the five random restaurants
        System.out.println("Getting 5 random restaurants");
        fiveRandomRestaurants = randomlyPick5Restaurants(incomingRestaurants);
        System.out.println("Printing five restaurants");
        System.out.println(fiveRandomRestaurants.get(0).getName());
        System.out.println(fiveRandomRestaurants.get(1).getName());
        System.out.println(fiveRandomRestaurants.get(2).getName());
        System.out.println(fiveRandomRestaurants.get(3).getName());
        System.out.println(fiveRandomRestaurants.get(4).getName());

        return fiveRandomRestaurants;
    }
    public ArrayList<RestaurantChoiceAfter> getRestaurantChoicesAfter(HashMap<String,Vote> votes, ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore){
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();

        for (int i = 0; i < restaurantChoicesBefore.size(); i++) {
            System.out.println("Putting in restaurant votes");

            System.out.println("Here are the restaurantChoicesBefore.get(i).getId()");
            System.out.println(restaurantChoicesBefore.get(i).getId());
            RestaurantChoiceAfter restaurantChoiceAfter = new RestaurantChoiceAfter();
            restaurantChoiceAfter.setId(restaurantChoicesBefore.get(i).getId());
            restaurantChoiceAfter.setName(restaurantChoicesBefore.get(i).getName());
            restaurantChoiceAfter.setVotes(0);
            restaurantChoicesAfter.add(restaurantChoiceAfter);
        }

        System.out.println("Printing HashMap votes");
        for (String name: votes.keySet()){
            String key =name.toString();
            Integer value = votes.get(name).getRestaurantId();
            for(int i = 0; i < restaurantChoicesAfter.size(); i++) {
                if(value.equals(restaurantChoicesAfter.get(i).getId())){
                    if(restaurantChoicesAfter.get(i).getVotes() < 1)
                        restaurantChoicesAfter.get(i).setVotes(1);
                    else
                        restaurantChoicesAfter.get(i).setVotes(restaurantChoicesAfter.get(i).getVotes()+1);
                }
            }
            System.out.println(key + " " + value);
        }
        return restaurantChoicesAfter;
    }

    public RestaurantWinner getRestaurantWinner(ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter){
        RestaurantWinner restaurantWinner = new RestaurantWinner();
        Integer max = 0;
        Integer maxTemp = 0;
        Integer index = null;

        // Find max votes in a RestaurantChoiceAfter
        for (int i = 0; i < restaurantChoicesAfter.size(); i++) {
            // Find max review in restaurantsReviews.get(index)
            if ((maxTemp = restaurantChoicesAfter.get(i).getVotes()) > max)
                max = maxTemp;
        }

        // Get the max review's index
        for (int i = 0; i < restaurantChoicesAfter.size(); i++) {
            if (max == restaurantChoicesAfter.get(i).getVotes()) {
                index = i;
                break;
            }
        }
        restaurantWinner.setId(restaurantChoicesAfter.get(index).getId());
        //restaurantWinner.setDatetime(getTime().toString());
        restaurantWinner.setName(restaurantChoicesAfter.get(index).getName());
        restaurantWinner.setVotes(restaurantChoicesAfter.get(index).getVotes());
        return restaurantWinner;
    }

    public  ArrayList<ArrayList<RestaurantReview>> getRestaurantsReviews(ArrayList<IncomingRestaurant> incomingRestaurants) {
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();

        ObjectMapper mapper = new ObjectMapper();

        if (incomingRestaurants != null)
            for (int i = 0; i < incomingRestaurants.size(); i++) {
                ArrayList<RestaurantReview> restaurantReviews = new ArrayList<>();
                System.out.println(incomingRestaurants.get(i).getName());
                String url = "https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName().replaceAll(" ", "%20");
                System.out.println(url);

                JsonNode rootNode = null;

                RestTemplate restTemplate = new RestTemplate();

                URI url1 = URI.create("https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName().replaceAll(" ", "%20"));
                String result = restTemplate.getForObject(url1, String.class);

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
            }
        else
        {} //return null; //todo: maybe do something more
        return restaurantsReviews;
    }
    //can make almost all methods here private (because they're only used by this one RestaurantController class) if I have time
    private ArrayList<IncomingRestaurant> randomlyPick5Restaurants(ArrayList<IncomingRestaurant> incomingRestaurants) {
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

            System.out.println("Here is the restaurantReviewSize");
            System.out.println(restaurantReviewsSize);

            for (int j = 0; j < restaurantReviewsSize; j++) {
                int rating = restaurantsReviews.get(i).get(j).getRating();

                System.out.println("Here is the rating");
                System.out.println(rating);

                averageRating = averageRating + rating;

                System.out.println("Here is the averageRating");
                System.out.println(averageRating);

            }
            actualAverageRating = averageRating.floatValue() / (restaurantsReviews.get(i).size());

            System.out.println("Here is the actualAverageRating");
            System.out.println(actualAverageRating);

            averageRating = Math.round(actualAverageRating);

            System.out.println("Here is the averageRating rounded");
            System.out.println(averageRating);

            averageRatings.add(averageRating);

            System.out.println("Here is the averageRatings");
            for (int j = 0; j < averageRatings.size(); j++) {
                System.out.println(averageRatings.get(j));
            }
            averageRating = 0;
        }
        return averageRatings;
    }

    public RestaurantSuggestion getRestaurantSuggestion(ArrayList<Integer> averageRatings, ArrayList<ArrayList<RestaurantReview>> restaurantsReviews, ArrayList<IncomingRestaurant> fiveRandomRestaurants) {
        // The index in the ArrayList the max rated restaurant is
        Integer index = null;
        RestaurantReview TopReview = null;

        // Get the max integer
        Integer max = null;

        max = Collections.max(averageRatings);

        // Get the max integer's index
        for (int i = 0; i < averageRatings.size(); i++) {
            if (max == averageRatings.get(i)) {
                index = i;
                break;
            }
        }
        TopReview = getRestaurantSuggestionTopReviewer(index, restaurantsReviews);

        return new RestaurantSuggestion(fiveRandomRestaurants.get(index).getId(), TopReview.getRestaurant(), averageRatings.get(index), TopReview.getReviewer(), TopReview.getReview());
    }

    public RestaurantReview getRestaurantSuggestionTopReviewer(Integer index, ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {

        System.out.println("Here is the index of the max rating");
        System.out.println(index);

        System.out.println("Here is the size of the restaurantsReviews");
        System.out.println(restaurantsReviews.size());

        Integer max = 0;
        int maxTemp = 0;
        Integer index1 = null;
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
        for (int i = 0; i < 5; i++) {
            RestaurantChoiceBefore restaurantChoiceBefore = new RestaurantChoiceBefore();
            restaurantChoiceBefore.setId(fiveRandomRestaurants.get(i).getId());
            restaurantChoiceBefore.setName(fiveRandomRestaurants.get(i).getName());
            restaurantChoiceBefore.setAverageReview(averageRatings.get(i));
            restaurantChoiceBefore.setDescription(fiveRandomRestaurants.get(i).getDescription());
            restaurantChoicesBefore.add(restaurantChoiceBefore);
        }
        return restaurantChoicesBefore;
    }
}