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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import com.lunchinator3000.dto.vote.Voter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    private DbService dbService;

    @Autowired
    public RestaurantService(RestTemplate restTemplate, ObjectMapper objectMapper, DbService dbService) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.dbService = dbService;
    }

    public @ResponseBody
    ArrayList<IncomingRestaurant> getRestaurants(ArrayList<Voter> voters) {
        ArrayList<IncomingRestaurant> incomingRestaurants = new ArrayList<IncomingRestaurant>();
        ArrayList<IncomingRestaurant> fiveRandomRestaurants = null;

        JsonNode rootNode = null;

        String result = restTemplate.getForObject("https://interview-project-17987.herokuapp.com/api/restaurants", String.class);

        logger.info("Getting incoming restaurants");
        try {
            rootNode = mapper.readTree(result);
            logger.debug(String.valueOf(rootNode));
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

            logger.debug(String.valueOf(incomingRestaurants));

            incomingRestaurants.add(incomingRestaurant);
        }

        logger.debug("Trying to print incomingRestaurants");
        logger.debug(String.valueOf(incomingRestaurants));

        //get the five random restaurants
        logger.debug("Getting 5 random restaurants");
        fiveRandomRestaurants = randomlyPick5Restaurants(incomingRestaurants, voters);
        logger.debug("Printing five restaurants");
        logger.debug(fiveRandomRestaurants.get(0).getName());
        logger.debug(fiveRandomRestaurants.get(1).getName());
        logger.debug(fiveRandomRestaurants.get(2).getName());
        logger.debug(fiveRandomRestaurants.get(3).getName());
        logger.debug(fiveRandomRestaurants.get(4).getName());

        return fiveRandomRestaurants;
    }
    public ArrayList<RestaurantChoiceAfter> getRestaurantChoicesAfter(HashMap<String,Vote> votes, ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore){
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();

        for (int i = 0; i < restaurantChoicesBefore.size(); i++) {
            logger.debug("Putting in restaurant votes");

            logger.debug("Here are the restaurantChoicesBefore.get(i).getId()");
            logger.debug(String.valueOf(restaurantChoicesBefore.get(i).getId()));
            RestaurantChoiceAfter restaurantChoiceAfter = new RestaurantChoiceAfter();
            restaurantChoiceAfter.setId(restaurantChoicesBefore.get(i).getId());
            restaurantChoiceAfter.setName(restaurantChoicesBefore.get(i).getName());
            restaurantChoiceAfter.setVotes(0);
            restaurantChoicesAfter.add(restaurantChoiceAfter);
        }

        logger.debug("Printing HashMap votes");
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
            logger.debug(key + " " + value);
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

        if (incomingRestaurants != null) {
            logger.info("Getting incoming restaurantReviews");
            for (int i = 0; i < incomingRestaurants.size(); i++) {
                ArrayList<RestaurantReview> restaurantReviews = new ArrayList<>();
                logger.debug(incomingRestaurants.get(i).getName());
                String url = "https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName().replaceAll(" ", "%20");
                logger.debug(url);

                JsonNode rootNode = null;

                URI url1 = URI.create("https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName().replaceAll(" ", "%20"));
                String result = restTemplate.getForObject(url1, String.class);

                try {
                    rootNode = mapper.readTree(result);
                    logger.debug(String.valueOf(rootNode));
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
                    logger.debug("Here is the restaurant review");
                    logger.debug(String.valueOf(restaurantReview));

                    restaurantReviews.add(restaurantReview);
                }
                logger.debug("Here are the reviews of the restaurants");
                logger.debug(String.valueOf(restaurantReviews));
                restaurantsReviews.add(restaurantReviews);
            }
        } else {}

        return restaurantsReviews;
    }
    //can make almost all methods here private (because they're only used by this one RestaurantController class) if I have time
    private ArrayList<IncomingRestaurant> randomlyPick5Restaurants(ArrayList<IncomingRestaurant> incomingRestaurants, ArrayList<Voter> voters) {
        ArrayList<IncomingRestaurant> randomTemp = incomingRestaurants;
        ArrayList<IncomingRestaurant> randomRestaurants = new ArrayList<>();
        ArrayList<ArrayList<String>> weekOfBallots = dbService.getAWeekOfBallots();
        ArrayList<ArrayList<String>> previousVoters = new ArrayList<>();
        ArrayList<String> currentVoters = new ArrayList<>();
        for (int i = 0; i < voters.size(); i++) {
            currentVoters.add(voters.get(i).getName().toUpperCase());
        }

        for (int i = 0; i < weekOfBallots.get(1).size(); i++) {
            ArrayList<String> ballotVotersArray = embeddedStringArrayToArrayList(weekOfBallots.get(1).get(i));
            previousVoters.add(ballotVotersArray);
        }

        Collections.shuffle(randomTemp);
        int numOfRandomRestaurants = 5;
        for (int i = 0; i < numOfRandomRestaurants; i++) {
            //if it does have the current voters - then get restaurants that are not those 5 ids
            if ((i < previousVoters.size()) && previousVoters.get(i).containsAll(currentVoters)) { //should go through the [pairs] of voters
                if (!weekOfBallots.get(0).get(i).equals(String.valueOf(randomTemp.get(i).getId()))) {
                    randomRestaurants.add(randomTemp.get(i));
                }
                else {
                    numOfRandomRestaurants++;
                }
            } else {
                //if it does not have the current voters - then just add the restaurants
                randomRestaurants.add(randomTemp.get(i));
            }
        }
        return randomRestaurants;
    }

    private ArrayList<String> embeddedStringArrayToArrayList(String s) {
        ArrayList<String> ballotVotersArray = new ArrayList<>();
        String [] ballotVoters = s.substring(1).substring(0, s.length() - 2).split(",");
        for (int j = 0; j < ballotVoters.length ; j++) {
            ballotVotersArray.add(ballotVoters[j].trim());
        }
        return ballotVotersArray;
    }

    public ArrayList<Integer> getAverageRestaurantRating(ArrayList<ArrayList<RestaurantReview>> restaurantsReviews) {
        ArrayList<Integer> averageRatings = new ArrayList<>();
        Integer averageRating = 0;
        Float actualAverageRating = Float.valueOf(0);
        int restaurantsReviewsSize = restaurantsReviews.size();

        for (int i = 0; i < restaurantsReviewsSize; i++) {
            int restaurantReviewsSize = restaurantsReviews.get(i).size();

            logger.debug("Here is the restaurantReviewSize");
            logger.debug(String.valueOf(restaurantReviewsSize));

            for (int j = 0; j < restaurantReviewsSize; j++) {
                int rating = restaurantsReviews.get(i).get(j).getRating();

                logger.debug("Here is the rating");
                logger.debug(String.valueOf(rating));

                averageRating = averageRating + rating;

                logger.debug("Here is the averageRating");
                logger.debug(String.valueOf(averageRating));

            }
            actualAverageRating = averageRating.floatValue() / (restaurantsReviews.get(i).size());

            logger.debug("Here is the actualAverageRating");
            logger.debug(String.valueOf(actualAverageRating));

            averageRating = Math.round(actualAverageRating);

            logger.debug("Here is the averageRating rounded");
            logger.debug(String.valueOf(averageRating));

            averageRatings.add(averageRating);

            logger.debug("Here is the averageRatings");
            for (int j = 0; j < averageRatings.size(); j++) {
                logger.debug(String.valueOf(averageRatings.get(j)));
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

        logger.debug("Here is the index of the max rating");
        logger.debug(String.valueOf(index));

        logger.debug("Here is the size of the restaurantsReviews");
        logger.debug(String.valueOf(restaurantsReviews.size()));

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