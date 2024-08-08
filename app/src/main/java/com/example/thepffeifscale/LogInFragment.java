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
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        View fragment = inflater.inflate(R.layout.fragment_log_in, container, false);

        EditText emailText = fragment.findViewById(R.id.loginEditTextEmail);
        EditText passwordText = fragment.findViewById(R.id.loginEditTextPassword);
        Button button = fragment.findViewById(R.id.loginFragmentButton);
        OneEvent oneEvent = OneEvent.singleton();
        HashBundle hashBundle = new HashBundle();
        hashBundle.set("message", "hello there");
        button.setOnClickListener(view -> {
            HashBundle logInBundle = new HashBundle();
            logInBundle.set("EMAIL", emailText.getText().toString());
            logInBundle.set("PASSWORD", passwordText.getText().toString());
            oneEvent.fire("LOGIN", logInBundle);
        });

        Button forgotPasswordButton = fragment.findViewById(R.id.loginForgotPasswordButton);
        forgotPasswordButton.setOnClickListener(view -> oneEvent.fire("FORGOT", new HashBundle()));
        return fragment;

    }

    private boolean validateEmail(String email) {
        return email.length() > 0;
    }

    private boolean validatePassword(String password) {
        return password.length() > 0;
    }
}