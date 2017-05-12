package com.lunchinator3000;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jeremy L on 5/11/2017.
 */
@RestController
public class RestaurantController {
    private UUID ballotId;

    public RestaurantController() {

    }
    //@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)//how to do in java 3)
    public @ResponseBody ArrayList<IncomingRestaurant> /*ResponseEntity<String>*/ getRestaurants(/*@RequestBody ArrayList<IncomingRestaurant> incomingRestaurants/*CreateBallot.InitialBallot1 initialBallot*/) throws IOException { //ResponseEntity<Ballot>
        ArrayList<IncomingRestaurant> incomingRestaurants = null;
         //time = Time.valueOf()
        ObjectMapper mapper = new ObjectMapper();
         //RestTemplate restTemplate = new RestTemplate();
         ////Ballot ballot = new Ballot();
         //String result = restTemplate.getForObject(uri, String.class);
         //String result = "{'name' : 'mkyong'}";
         //result.replaceAll("\\[", "").replaceAll("\\]", "");
        incomingRestaurants = mapper.readValue(new URL("https://interview-project-17987.herokuapp.com/api/restaurants"), new ArrayList<IncomingRestaurant>().getClass());
         ////Ballot ballot = restTemplate.getForObject(uri, ballot);

         //HttpHeaders headers = new HttpHeaders();
         //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         //HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

         //ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);


         //System.out.println(result);
        //System.out.println(incomingRestaurants);

        ballotId = UUID.randomUUID();
        return incomingRestaurants; //return new ResponseEntity<Ballot>(ballot, HttpStatus.OK);
    }
}
