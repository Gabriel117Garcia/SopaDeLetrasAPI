package com.gamg.wordsearch.config;

public class InvalidRequestException extends Throwable {
    public InvalidRequestException(String s) {
        System.out.println("Invalid Request Exception: " + s);
    }
}
