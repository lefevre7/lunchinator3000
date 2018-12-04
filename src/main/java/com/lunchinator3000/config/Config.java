package com.lunchinator3000.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.controllers.BallotController;
import com.lunchinator3000.controllers.VoteController;
import com.lunchinator3000.service.BallotService;
import com.lunchinator3000.service.DbService;
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
    public RestaurantService restaurantService(RestTemplate restTemplate, DbService dbService) {return new RestaurantService(restTemplate, objectMapper(), dbService);}

    @Bean
    public BallotService ballotService(RestaurantService restaurantService, DbService dbService) {return new BallotService(restaurantService, dbService);}

    @Bean
    public VoteService voteService(BallotService ballotService, DbService dbService) {return new VoteService(ballotService, dbService);}

    @Bean
    public BallotController ballotController(BallotService ballotService) {
        return new BallotController(ballotService);
    }

    @Bean
    public VoteController voteController(VoteService voteService) {
        return new VoteController(voteService);
    }

}
