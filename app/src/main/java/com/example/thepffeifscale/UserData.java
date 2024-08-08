package com.example.thepffeifscale;

import java.util.HashMap;

public class UserData {
    private String password;
    private String username;
    public UserData(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}


