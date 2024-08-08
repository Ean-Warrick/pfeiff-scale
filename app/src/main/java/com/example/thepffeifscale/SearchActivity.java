package com.example.thepffeifscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thepffeifscale.infinitum.lua.LuaCore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.firestore = FirebaseFirestore.getInstance();

        LinearLayout layout = this.findViewById(R.id.searchReviews);
        TextView searchResultText = this.findViewById(R.id.searchResultText);
        layout.removeAllViews();
        EditText searchBar = this.findViewById(R.id.searchBar);
        searchBar.setOnKeyListener((view, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                this.search(layout, searchBar, searchResultText);
                return true;
            }
            return false;
        });
        searchBar.requestFocus();
        searchBar.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.search(layout, searchBar, searchResultText);
                searchBar.clearFocus();
                return true;
            } else {
                return false;
            }
        });
        Button searchButton = this.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            this.search(layout, searchBar, searchResultText);
        });
    }

    private void search(LinearLayout layout, EditText searchBar, TextView searchResultText) {
        layout.removeAllViews();
        String searchText = searchBar.getText().toString();
        this.firestore.collection("reviews").orderBy("date").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(SearchActivity.this, "Search Succeeded.",
                                Toast.LENGTH_SHORT).show();
                        QuerySnapshot arrayList = task.getResult();
                        int size = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Object name = document.get("username");
                            Object text = document.get("text");
                            Object rating = document.get("rating");
                            Object userid = document.get("userid");
                            Date date = document.getDate("date");
                            LuaCore.print(searchText, text, text.toString().contains(searchText));
                            DocumentReference doc_uid = document.getReference();

                            if(name != null && text != null && rating != null && (text.toString().contains(searchText))) {
                                size += 1;
                                LayoutInflater layoutInflater = this.getLayoutInflater();
                                View reviewCard = layoutInflater.inflate(R.layout.reviewcard, null);
                                View barrier = layoutInflater.inflate(R.layout.invisible, null);
                                TextView nameText = reviewCard.findViewById(R.id.reviewNameText);
                                nameText.setText(name.toString());
                                TextView dateText = reviewCard.findViewById(R.id.reviewDate);
                                dateText.setText(date.toString());
                                RatingBar ratingBar = reviewCard.findViewById(R.id.reviewRating);
                                float ratingFloat = Float.parseFloat(rating.toString());

                                TextView reviewText = reviewCard.findViewById(R.id.reviewText);
                                reviewText.setText(text.toString());
                                layout.addView(reviewCard);
                                layout.addView(barrier);
                                LuaCore.spawn(() -> {
                                    LuaCore.wait(1f/60f);
                                    this.runOnUiThread(() -> {
                                        ratingBar.setRating(ratingFloat);
                                    });
                                });
                            }
                        }
                        if (size > 1) {
                            searchResultText.setText("Found " + size + " Results");
                        } else if (size > 0) {
                            searchResultText.setText("Found " + size + " Result");
                        } else {
                            searchResultText.setText("No Results Found");
                        }
                    } else {
                        Toast.makeText(SearchActivity.this, "Search failed.",
                                Toast.LENGTH_SHORT).show();
                        LuaCore.print(task.getException().toString());
                    }
                });
    }
}