package com.example.thepffeifscale;

import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.dev.Instance;

import java.util.HashMap;

public class Model extends Instance {
    private static Model model = null;
    private HashMap<String, String> logInMap;

    private Model() {
        super();
        this.logInMap = new HashMap<>();
    }

    public static Model getSingleton() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public HashBundle onLogInRequest(HashBundle logInBundle) {
        String email = logInBundle.getString("EMAIL");
        String password = logInBundle.getString("PASSWORD");
        String result = this.logInMap.get(email + "_" + password);
        HashBundle hashBundle = new HashBundle();
        hashBundle.set("SUCCESS", result != null);
        hashBundle.set("RESULT", result);
        return hashBundle;
    }
}
