package com.lunchinator3000.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lunchinator3000.controllers.BallotController;
import com.lunchinator3000.controllers.VoteController;
import com.lunchinator3000.service.BallotService;
import com.lunchinator3000.service.RestaurantService;
import com.lunchinator3000.service.VoteService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

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

    /*@Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String pass;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(user);
        config.setPassword(pass);
        config.addDataSourceProperty("ddl-auto", "update");
        return new HikariDataSource(config);
    }*/
}
