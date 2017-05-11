package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/10/2017.
 */
public class Voter {
    private String name;
    private String email;

    public Voter(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
