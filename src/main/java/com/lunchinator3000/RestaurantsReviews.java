package com.lunchinator3000;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.DisposableBean;
import sun.net.www.http.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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


            String result = restTemplate.getForObject(url, String.class);
            if (result.equals("[]"))//could try while
                result = restTemplate.getForObject(url, String.class);
            System.out.println(result);
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
            restaurantReviews.clear();
        }
        return restaurantsReviews;
    }

    /*public ArrayList<RestaurantsReviews>  getRestaurantSuggestion(ArrayList<IncomingRestaurant> incomingRestaurants) {

        return
    }*/

}
