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

import com.lunchinator3000.exception.LunchinatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestaurantService {

    private static final String RESTAURANTS_API = "https://interview-project-17987.herokuapp.com/api/restaurants";
    private static final String RESTAURANT_REVIEWS_URL = "https://interview-project-17987.herokuapp.com/api/reviews/";

    private final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private RestTemplate restTemplate;
    private ObjectMapper mapper;

    @Autowired
    public RestaurantService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
    }

    public String getAllRestaurants() {
        try {
            ResponseEntity<String> restaurantsApiResponse = restTemplate.getForEntity(RESTAURANTS_API, String.class);
            if (restaurantsApiResponse.getStatusCodeValue() < 400) {
                String response = restaurantsApiResponse.getBody();
                if ((null == response) || response.isEmpty()) {
                    logger.warn("Restaurants API call succeeded but returned a null or empty result. Here is the body: "
                            + response + " And here is the status code: " + restaurantsApiResponse.getStatusCodeValue());
                }
                return response;
            } else {
                String warn = "Restaurants API call errored. Here is the body: " + restaurantsApiResponse.getBody() +
                        " And here is the status code: " + restaurantsApiResponse.getStatusCodeValue();
                logger.warn("Restaurants API call errored. Here is the body: " + restaurantsApiResponse.getBody() +
                        " And here is the status code: " + restaurantsApiResponse.getStatusCodeValue());
                throw new LunchinatorException(warn);
            }
        } catch (Exception e) {
            logger.error("There has been an error calling the restaurants api: " + e.getMessage());
            throw new LunchinatorException(e.getMessage());
        }
    }

    public String getRestaurantReviews(URI url) {
        try {
            ResponseEntity<String> restaurantReviewsResponse = restTemplate.getForEntity(url, String.class);
            if (restaurantReviewsResponse.getStatusCodeValue() < 400) {
                String response = restaurantReviewsResponse.getBody();
                if ((null == response) || response.isEmpty()) {
                    logger.warn("Restaurant reviews call succeeded but returned a null or empty result. Here is the body: "
                            + response + " And here is the status code: " + restaurantReviewsResponse.getStatusCodeValue());
                }
                return response;
            } else {
                String warn = "Restaurant reviews call errored. Here is the body: " + restaurantReviewsResponse.getBody() +
                        " And here is the status code: " + restaurantReviewsResponse.getStatusCodeValue();
                logger.warn(warn);
                throw new LunchinatorException(warn);
            }
        } catch (Exception e) {
            logger.error("There has been an error calling the restaurant reviews url: " + e.getMessage());
            throw new LunchinatorException(e.getMessage());
        }
    }

    public @ResponseBody
    ArrayList<IncomingRestaurant> getRestaurants() {
        ArrayList<IncomingRestaurant> incomingRestaurants = new ArrayList<IncomingRestaurant>();
        ArrayList<IncomingRestaurant> fiveRandomRestaurants = null;

        JsonNode rootNode = null;

        String result = getAllRestaurants();

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
        fiveRandomRestaurants = randomlyPick5Restaurants(incomingRestaurants);
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
                String url = RESTAURANT_REVIEWS_URL + incomingRestaurants.get(i).getName().replaceAll(" ", "%20");
                logger.debug(url);

                JsonNode rootNode = null;

                URI uri = URI.create(url);
                //todo make a different call to own service, or own json/string list on this server if doesn't work, or --return a ballot without reviews--
                try {
                    String result = getRestaurantReviews(uri);

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
                } catch (LunchinatorException e) {
                    RestaurantReview restaurantReview = new RestaurantReview();
                    restaurantReview.setId(incomingRestaurants.get(i).getId());
                    restaurantReview.setRestaurant(incomingRestaurants.get(i).getName());
                    restaurantReview.setReviewer("None");
                    restaurantReview.setRating(-1);
                    restaurantReview.setReview("None could be obtained");
                    restaurantReviews.add(restaurantReview);
                }
                logger.debug("Here are the reviews of the restaurants");
                logger.debug(String.valueOf(restaurantReviews));
                restaurantsReviews.add(restaurantReviews);
            }
        } else
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
        RestaurantReview restaurantReview = new RestaurantReview();
        try {
            restaurantReview = restaurantsReviews.get(index).get(index1);
        } catch (NullPointerException e) {
            if (restaurantsReviews.size() > 0)
                restaurantReview = restaurantsReviews.get(0).get(0);
        }
        return restaurantReview;
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