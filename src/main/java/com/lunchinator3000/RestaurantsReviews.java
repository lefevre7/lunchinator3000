package com.lunchinator3000;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jeremy L on 5/12/2017.
 */
public class RestaurantsReviews {
    //private ArrayList<RestaurantReview> restaurantReviews;
    //private ArrayList<ArrayList<RestaurantReview>> restaurantsReviews;

    public RestaurantsReviews() {

    }

    public  @ResponseBody ArrayList<ArrayList<RestaurantReview>> getRestaurantsReviews(ArrayList<RestaurantController.IncomingRestaurant> incomingRestaurants) {
        //ArrayList<IncomingRestaurant> incomingRestaurants = null;
        ArrayList<RestaurantReview> restaurantReviews = new ArrayList<RestaurantReview>();
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();

        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < incomingRestaurants.size(); i++) {
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
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);

            System.out.println("Getting incoming restaurantReviews");
            try {
                rootNode = mapper.readTree(result);
                System.out.println(rootNode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < rootNode.size(); j++) {
                RestaurantReview restaurantReview = new RestaurantReview();
                restaurantReview.setId(rootNode.get(i).get("Id").asInt());
                restaurantReview.setRestaurant(rootNode.get(i).get("restaurant").asText());
                restaurantReview.setReviewer(rootNode.get(i).get("reviewer").asText());
                restaurantReview.setRating(rootNode.get(i).get("rating").asInt());
                restaurantReview.setReview(rootNode.get(i).get("review").asText());
                System.out.println(restaurantReview);

                restaurantReviews.add(restaurantReview);
            }
            restaurantsReviews.add(restaurantReviews);
        }
        return restaurantsReviews;
    }

    /*public ArrayList<RestaurantsReviews>  getRestaurantSuggestion(ArrayList<IncomingRestaurant> incomingRestaurants) {

        return
    }*/

}
