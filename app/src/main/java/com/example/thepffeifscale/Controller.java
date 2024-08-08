package com.example.thepffeifscale;

import com.example.thepffeifscale.infinitum.bind.BindableEvent;
import com.example.thepffeifscale.infinitum.bind.InvokableFunction;
import com.example.thepffeifscale.infinitum.dev.Instance;

import java.util.ArrayList;

public class Controller extends Instance {
    private static Controller controller = null;
    private static final Model model = Model.getSingleton();

    public InvokableFunction logInRequest;
    public InvokableFunction getCurrentActivity;
    public BindableEvent toastMessage;
    public BindableEvent currentActivityChanged;
    public UserData userData;
    private ArrayList<Review> reviews = new ArrayList<>();


    private Controller() {
        super();
        this.currentActivityChanged = new BindableEvent();
        this.toastMessage = new BindableEvent();
        this.getCurrentActivity = new InvokableFunction();
        this.logInRequest = new InvokableFunction();
        logInRequest.setFunc(model::onLogInRequest);
    }

    public void clearReviews() {
        reviews.clear();
    }

    public void addReview(Review review) {
        reviews.add(0, review);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public static Controller getSingleton() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }
}
