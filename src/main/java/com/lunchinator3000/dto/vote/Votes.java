package com.lunchinator3000.dto.vote;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Votes {

    public Votes(int restaurant_id, String ballot_id, String voter_name, String email_address) {
        this.restaurant_id = restaurant_id;
        this.ballot_id = ballot_id;
        this.voter_name = voter_name;
        this.email_address = email_address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "serial")
    private Long id;

    private int restaurant_id;

    private String ballot_id;

    private String voter_name;

    private String email_address;
}