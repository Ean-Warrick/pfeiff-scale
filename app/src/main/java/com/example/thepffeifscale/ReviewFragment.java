package com.example.thepffeifscale;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.bind.OneEvent;
import com.example.thepffeifscale.infinitum.lua.LuaCore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */

    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
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
        mAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_review, container, false);
        TextView blockText = fragment.findViewById(R.id.reviewBlockText);
        EditText reviewEditText = fragment.findViewById(R.id.reviewEditText);

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user.isEmailVerified()) {
                blockText.setVisibility(View.INVISIBLE);
            } else {
                reviewEditText.setInputType(InputType.TYPE_NULL);
            }
        }

        Button submitButton = fragment.findViewById(R.id.reviewSubmitButton);
        submitButton.setOnClickListener(view -> {
            TextView reviewText = fragment.findViewById(R.id.reviewEditText);
            RatingBar ratingBar = fragment.findViewById(R.id.reviewRatingBar);
            Controller controller = Controller.getSingleton();
            FirebaseUser user = mAuth.getCurrentUser();
            if(user != null) {
                float rating = ratingBar.getRating();
                String review = reviewText.getText().toString();
                String email = user.getEmail();
                HashMap<String, Object> data = new HashMap<>();

                data.put("rating", rating);
                data.put("text", review);
                data.put("username", AppManager.getDisplayNameFromUser(user));
                data.put("date", FieldValue.serverTimestamp());
                data.put("userid", user.getUid());

                this.firestore.collection("reviews")
                        .add(data)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                reviewText.setText("");
                                ratingBar.setRating(0);
                                HashBundle hashBundle = new HashBundle();
                                hashBundle.set("DESTINATION", 0);
                                OneEvent.singleton().fire("GOTO", hashBundle);
                                Toast.makeText(this.getContext(), "Post Succeeded.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this.getContext(), "Post Failed. Please Try Again.",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        return fragment;
    }
}