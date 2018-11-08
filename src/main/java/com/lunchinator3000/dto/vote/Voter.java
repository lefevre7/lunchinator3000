package com.lunchinator3000.dto.vote;

/**
 * Created by Jeremy L on 11/8/2018
 */
public class Voter {
    private String name;
    private String emailAddress;

    public Voter(String name, String email) {
        this.name = name;
        this.emailAddress = email;
    }

    public Voter() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String email) {
        this.emailAddress = email;
    }
}