package com.lunchinator3000.dto.restaurant;

/**
 * Created by Jeremy L on 5/13/2017.
 */
public class IncomingRestaurants {
        private String waitTimeMinutes;
        private String description;

        public IncomingRestaurants() {
            super();
        }

        public IncomingRestaurants(int id, String name, String waitTimeMinutes, String description) {
            this.waitTimeMinutes = waitTimeMinutes;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWaitTimeMinutes() {
            return waitTimeMinutes;
        }

        public void setWaitTimeMinutes(String waitTimeMinutes) {
            this.waitTimeMinutes = waitTimeMinutes;
        }

}
