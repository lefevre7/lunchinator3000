package com.lunchinator3000.dto.ballot;

/**
 *  This class' message will hold the error message so that it can
 *  display any type of BallotInterface and return it in one object
 */
public class BallotError implements BallotInterface{
    private String message = "Error";


    public BallotError(String message) {
        this.message = message;
    }

    public BallotError() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}