package com.lunchinator3000;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jeremy L on 5/12/2017.
 */
public class RestaurantsReviews {
    private ArrayList<RestaurantReview> restaurantReviews;
    private ArrayList<ArrayList<RestaurantReview>> restaurantsReviews;

    public RestaurantsReviews() {

    }

    public ArrayList<ArrayList<RestaurantReview>> getRestaurantsReviews(ArrayList<IncomingRestaurant> incomingRestaurants) throws IOException {
        //ArrayList<IncomingRestaurant> incomingRestaurants = null;
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < incomingRestaurants.size(); i++) {
            String url = "https://interview-project-17987.herokuapp.com/api/reviews/" + incomingRestaurants.get(i).getName();
            restaurantReviews = mapper.readValue(new URL(url), new ArrayList<IncomingRestaurant>().getClass());
            restaurantsReviews.add(restaurantReviews);
        }

        return restaurantsReviews;
    }

    /*public ArrayList<RestaurantsReviews>  getRestaurantSuggestion(ArrayList<IncomingRestaurant> incomingRestaurants) {

        return
    }*/

}
