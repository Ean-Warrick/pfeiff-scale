package com.example.thepffeifscale;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.bind.OneEvent;
import com.example.thepffeifscale.infinitum.lua.LuaCore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */

    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_sign_in, container, false);

        Button submitButton = fragment.findViewById(R.id.signUpSubmitButton);
        EditText emailText = fragment.findViewById(R.id.signUpEmailText);
        EditText passwordText = fragment.findViewById(R.id.signUpPasswordText);
        EditText usernameText = fragment.findViewById(R.id.signUpUsernameText);
        submitButton.setOnClickListener(view -> {
            HashBundle hashBundle = new HashBundle();
            hashBundle.set("EMAIL", emailText.getText().toString());
            hashBundle.set("PASSWORD", passwordText.getText().toString());
            hashBundle.set("USERNAME", usernameText.getText().toString());
            if(Validation.isEmailValid(hashBundle.getString("EMAIL")) && Validation.isPasswordValid(hashBundle.getString("PASSWORD")) && Validation.isUserNameValid(hashBundle.getString("USERNAME"))) {
                OneEvent.singleton().fire("SIGNUP", hashBundle);
            } else {
                if(!Validation.isEmailValid(hashBundle.getString("EMAIL"))) {
                    emailText.setError("Email not recognized as a simpson college email");
                } else {
                    emailText.setError(null);
                }

                if (!Validation.isPasswordValid(hashBundle.getString("PASSWORD"))) {
                    passwordText.setError("Password is shorter than 8 characters");
                } else {
                    passwordText.setError(null);
                }

                if (!Validation.isUserNameValid(hashBundle.getString("USERNAME"))) {
                    usernameText.setError("Username is either less than 5 characters or uses special characters");
                } else {
                    usernameText.setError(null);
                }
            }
        });

        return fragment;
    }
}