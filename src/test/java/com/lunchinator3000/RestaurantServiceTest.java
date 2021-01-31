package com.lunchinator3000;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.exception.LunchinatorException;
import com.lunchinator3000.service.RestaurantService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

public class RestaurantServiceTest {

    public RestTemplate restTemplate = new RestTemplate();

    public ObjectMapper objectMapper() {return new ObjectMapper();}

    @Autowired
    public RestaurantService restaurantService() {return new RestaurantService(restTemplate, objectMapper());}

    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setUp() {
        this.mockRestServiceServer = MockRestServiceServer.createServer(this.restTemplate);
    }

    @Test
    public void testGetAllRestaurantsSuccess(){
        mockRestServiceServer.expect(requestTo("https://interview-project-17987.herokuapp.com/api/restaurants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("a string", MediaType.TEXT_PLAIN));

        this.restaurantService().getAllRestaurants();
    }

    @Test(expected = LunchinatorException.class)
    public void testGetAllRestaurantsServerError(){
        mockRestServiceServer.expect(requestTo("https://interview-project-17987.herokuapp.com/api/restaurants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        this.restaurantService().getAllRestaurants();
    }

    @Test(expected = LunchinatorException.class)
    public void testGetAllRestaurantsBadRequest(){
        mockRestServiceServer.expect(requestTo("https://interview-project-17987.herokuapp.com/api/restaurants"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        this.restaurantService().getAllRestaurants();
    }

    @Test
    public void testGetRestaurantReviewsSuccess(){
        mockRestServiceServer.expect(requestTo("a.url"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("a string", MediaType.TEXT_PLAIN));

        this.restaurantService().getRestaurantReviews(URI.create("a.url"));
    }

    @Test(expected = LunchinatorException.class)
    public void testGetRestaurantReviewsServerError(){
        mockRestServiceServer.expect(requestTo("a.url"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        this.restaurantService().getRestaurantReviews(URI.create("a.url"));
    }

    @Test(expected = LunchinatorException.class)
    public void testGetRestaurantReviewsBadRequest(){
        mockRestServiceServer.expect(requestTo("a.url"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        this.restaurantService().getRestaurantReviews(URI.create("a.url"));
    }
}
