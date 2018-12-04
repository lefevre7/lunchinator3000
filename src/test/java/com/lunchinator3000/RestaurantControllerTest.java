package com.lunchinator3000;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.db.SheetsDb;
import com.lunchinator3000.dto.restaurant.RestaurantChoiceAfter;
import com.lunchinator3000.dto.restaurant.RestaurantChoiceBefore;
import com.lunchinator3000.dto.restaurant.RestaurantReview;
import com.lunchinator3000.dto.restaurant.RestaurantWinner;
import com.lunchinator3000.dto.vote.Vote;
import com.lunchinator3000.service.DbService;
import com.lunchinator3000.service.RestaurantService;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/16/2017.
 */
public class RestaurantControllerTest {
    @Test
    public void getRestaurantChoicesAfter() throws Exception {
        RestaurantService restaurantService = new RestaurantService(new RestTemplate(), new ObjectMapper(), new DbService(new SheetsDb(new ChromeDriver())));
        HashMap<String,Vote> votes = new HashMap<>();
        UUID uuid = new UUID(12, 12);
        votes.put("someone@gmail.com",new Vote(uuid, "someone@gmail.com", 2, "Someone"));
        ArrayList<RestaurantChoiceBefore> restaurantChoicesBefore = new ArrayList<>();
        restaurantChoicesBefore.add(new RestaurantChoiceBefore(2, "Hi", 4, "Here's the description"));
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter1 = restaurantService.getRestaurantChoicesAfter(votes, restaurantChoicesBefore);
        restaurantChoicesAfter.add(new RestaurantChoiceAfter(2, "Hi", 1));

        Assert.assertEquals(restaurantChoicesAfter.get(0).getName(), restaurantChoicesAfter1.get(0).getName());
        Assert.assertEquals(restaurantChoicesAfter.get(0).getId(), restaurantChoicesAfter1.get(0).getId());
        Assert.assertEquals(restaurantChoicesAfter.get(0).getVotes(), restaurantChoicesAfter1.get(0).getVotes());
    }

    @Test
    public void getRestaurantWinner() throws Exception {

        RestaurantService restaurantService = new RestaurantService(new RestTemplate(), new ObjectMapper(), new DbService(new SheetsDb(new ChromeDriver())));
        ArrayList<RestaurantChoiceAfter> restaurantChoicesAfter = new ArrayList<>();
        restaurantChoicesAfter.add(new RestaurantChoiceAfter(2, "Hi", 1));
        restaurantChoicesAfter.add(new RestaurantChoiceAfter(4, "High", 2));
        restaurantChoicesAfter.add(new RestaurantChoiceAfter(5, "Higher", 3));
        restaurantChoicesAfter.add(new RestaurantChoiceAfter(7, "Highest", 4));
        RestaurantWinner restaurantWinner = new RestaurantWinner();

        restaurantWinner = restaurantService.getRestaurantWinner(restaurantChoicesAfter);

        Assert.assertEquals(restaurantWinner.getName(), "Highest");
        Assert.assertEquals(restaurantWinner.getId(), 7);
        Assert.assertEquals(restaurantWinner.getVotes(), 4);
    }

    @Test
    public void getAverageRestaurantRating() throws Exception {
        ArrayList<ArrayList<RestaurantReview>> restaurantsReviews = new ArrayList<ArrayList<RestaurantReview>>();
        ArrayList<RestaurantReview> restaurantReviews = new ArrayList<>();
        RestaurantReview restaurantReview = new RestaurantReview();
        restaurantReview.setRating(4);
        restaurantReview.setReviewer("Jim");
        restaurantReview.setReview("Here it is");
        restaurantReview.setRestaurant("Jimbo's");

        RestaurantReview restaurantReview1 = new RestaurantReview();
        restaurantReview1.setRating(3);
        restaurantReview1.setReviewer("Josh");
        restaurantReview1.setReview("Here it is");
        restaurantReview1.setRestaurant("Jimbo's");

        restaurantReviews.add(restaurantReview);
        restaurantReviews.add(restaurantReview1);
        restaurantsReviews.add(restaurantReviews);

        RestaurantService restaurantService = new RestaurantService(new RestTemplate(), new ObjectMapper(), new DbService(new SheetsDb(new ChromeDriver())));
        ArrayList<Integer> averageRatings = new ArrayList<>();
        averageRatings = restaurantService.getAverageRestaurantRating(restaurantsReviews);
        ArrayList<Integer> averageRatings1 = new ArrayList<>();
        averageRatings1.add(4);

        Assert.assertEquals(averageRatings1.get(0), averageRatings.get(0));

    }

    @Test
    public void getRestaurantSuggestion() throws Exception {

    }

    @Test
    public void getRestaurantSuggestionTopReviewer() throws Exception {
    }

    @Test
    public void getRestaurantChoiceBefore() throws Exception {
    }

}