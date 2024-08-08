package com.example.thepffeifscale;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Review {
    private double rating;
    private String review;
    private String username;
    private Date date;
    private String userid;
    private DocumentReference documentReference;

    public Review(double rating, String review, String username, Date date, DocumentReference documentReference, String userid) {
        this.rating = rating;
        this.review = review;
        this.username = username;
        this.userid = userid;
        this.documentReference = documentReference;

        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public double getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public String getUsername() {
        return username;
    }

    public Date getDate() {
        return date;
    }

    public void destroy() {
        Controller.getSingleton().removeReview(this);
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }
}
