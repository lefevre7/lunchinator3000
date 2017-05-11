package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
public class IncomingRestaurant extends AbstractRestaurant {
    private String waitTimeMinutes;
    private String description;

    public IncomingRestaurant(int id, String name, String waitTimeMinutes, String description) {
        super(id, name);
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
