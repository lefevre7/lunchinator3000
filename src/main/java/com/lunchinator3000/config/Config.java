package com.lunchinator3000.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.controllers.BallotController;
import com.lunchinator3000.controllers.VoteController;
import com.lunchinator3000.service.BallotService;
import com.lunchinator3000.service.RestaurantService;
import com.lunchinator3000.service.VoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}

    @Bean
    public ObjectMapper objectMapper() {return new ObjectMapper();}

    @Bean
    public RestaurantService restaurantService(RestTemplate restTemplate) {return new RestaurantService(restTemplate, objectMapper());}

    @Bean
    public BallotService ballotService(RestaurantService restaurantService) {return new BallotService(restaurantService);}

    @Bean
    public VoteService voteService(BallotService ballotService) {return new VoteService(ballotService);}

    @Bean
    public BallotController ballotController(BallotService ballotService) {
        return new BallotController(ballotService);
    }

    @Bean
    public VoteController voteController(VoteService voteService) {
        return new VoteController(voteService);
    }

}
