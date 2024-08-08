package com.example.thepffeifscale;

import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class AppManager {
    private static HashMap<String, UserData> userDataHashMap = createHashMap();
    private static ArrayList<Review> reviewList = createArrayList();


    public static void addReview(Review review) {
        reviewList.add(0, review);
    }

    private static HashMap<String, UserData> createHashMap() {
        HashMap<String, UserData> hashMap = new HashMap<>();
        return hashMap;
    }

    private static ArrayList<Review> createArrayList() {

        return new ArrayList<>();
    }

    public static ArrayList<Review> getReviewList() {
        return reviewList;
    }

    static UserData login(String email, String password) {
        if(verify(email, password)) {
            return userDataHashMap.get(email);
        } else {
            return null;
        }
    }

    private static boolean verify(String email, String password) {
        UserData userData = userDataHashMap.get(email);
        return userData != null && userData.getPassword().equals(password);
    }

    public static String getDisplayNameFromUser(FirebaseUser user) {
        if(user.getDisplayName() != null && !user.getDisplayName().equals("")) {
            return user.getDisplayName();
        } else {
            return "Anonymous";
        }
    }

}
