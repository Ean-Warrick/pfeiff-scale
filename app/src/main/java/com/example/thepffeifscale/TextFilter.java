package com.example.thepffeifscale;

public class TextFilter {
    public static String filter(String message) {
        return message;
    }

    // this is a list of bad words that will be censored if anyone posts them.
    // Censoring will occur on ever local machine so it can't be avoided
    // This is just a band-aid over the issue, it is not expected to stop all profanity
    // future updates will make this filter better
    private String[] bannedStrings = new String[]{

    };
}
