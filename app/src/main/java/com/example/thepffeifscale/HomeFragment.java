package com.example.thepffeifscale;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thepffeifscale.infinitum.lua.LuaCore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private String sortMethod = "MOST_RECENT";
    private boolean loaded = false;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment\
        View fragment = inflater.inflate(R.layout.fragment_home, container, false);
        Button refreshButton = fragment.findViewById(R.id.homeRefreshButton);
        refreshButton.setOnClickListener(view -> this.refresh(inflater, fragment));
        Button sortButton = fragment.findViewById(R.id.filterButton);
        sortButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.getContext(), sortButton);
            popupMenu.inflate(R.menu.filter);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.mostRecent) {
                    this.sortMethod = "MOST_RECENT";
                    this.refresh(inflater, fragment);
                    return true;
                } else if (menuItem.getItemId() == R.id.ratingHighToLow) {
                    this.sortMethod = "RATING_HIGH_TO_LOW";
                    this.refresh(inflater, fragment);
                    return true;
                } else if (menuItem.getItemId() == R.id.ratingLowToHigh) {
                    this.sortMethod = "RATING_LOW_TO_HIGH";
                    this.refresh(inflater, fragment);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });

        if (!this.loaded) {
            this.loaded = true;
            this.refresh(inflater, fragment);
        } else {
            this.reload(inflater, fragment);
        }
        return fragment;
    }

    private void reload(LayoutInflater inflater, View fragment) {
        mAuth = FirebaseAuth.getInstance();
        LinearLayout linearLayout = fragment.findViewById(R.id.searchReviews);
        linearLayout.removeAllViews();
        Controller controller = Controller.getSingleton();
        ArrayList<Review> reviews = controller.getReviews();

        if (Objects.equals(this.sortMethod, "MOST_RECENT")) {
            reviews.sort((review, t1) -> {
                if (review.getDate().before(t1.getDate())) {
                    return 1;
                } else if (review.getDate().after(t1.getDate())) {
                    return -1;
                } else {
                    return 0;
                }
            });
        } else if (Objects.equals(this.sortMethod, "RATING_HIGH_TO_LOW")) {
            reviews.sort((review, t1) -> Double.compare(t1.getRating(), review.getRating()));
        } else if (Objects.equals(this.sortMethod, "RATING_LOW_TO_HIGH")) {
            reviews.sort(Comparator.comparingDouble(Review::getRating));
        }

        for(Review review : reviews) {
            View view = inflater.inflate(R.layout.reviewcard, null);
            View barrier = inflater.inflate(R.layout.invisible, null);
            ImageButton deleteButton = view.findViewById(R.id.reviewDeleteButton);
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                if (user.getUid().equals(review.getUserid())) {
                    deleteButton.setVisibility(View.VISIBLE);
                }
            }

            deleteButton.setOnClickListener(view1 -> {
                LayoutInflater onClickInflater = this.getLayoutInflater();
                View popUp = onClickInflater.inflate(R.layout.delete_card, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setCancelable(false);
                builder.setView(popUp);
                AlertDialog confirmationView = builder.create();

                Button confirmButton = popUp.findViewById(R.id.deleteConfirmButton);
                Button cancelButton = popUp.findViewById(R.id.deleteCancelButton);
                confirmButton.setOnClickListener(view2 -> {
                    this.onDelete(review, linearLayout, view, barrier);
                    confirmationView.cancel();
                });

                cancelButton.setOnClickListener(view22 -> {
                    confirmationView.cancel();
                });

                confirmationView.show();
            });
            TextView nameText = view.findViewById(R.id.reviewNameText);
            nameText.setText(review.getUsername());
            TextView dateText = view.findViewById(R.id.reviewDate);
            dateText.setText(review.getDate().toString());
            RatingBar ratingBar = view.findViewById(R.id.reviewRating);
            float ratingFloat = Float.parseFloat(Double.toString(review.getRating()));
            LuaCore.print(ratingFloat);
            TextView reviewText = view.findViewById(R.id.reviewText);
            reviewText.setText(review.getReview());
            linearLayout.addView(view);
            linearLayout.addView(barrier);
            LuaCore.spawn(() -> {
                LuaCore.wait(1f/60f);
                requireActivity().runOnUiThread(() -> {
                    ratingBar.setRating(ratingFloat);
                });
            });
        }
    }

    private void refresh(LayoutInflater inflater, View fragment) {
        this.firestore = FirebaseFirestore.getInstance();
        LinearLayout linearLayout = fragment.findViewById(R.id.searchReviews);
        linearLayout.removeAllViews();
        this.firestore.collection("reviews").orderBy("date")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Controller controller = Controller.getSingleton();
                        controller.clearReviews();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Object name = document.get("username");
                            Object text = document.get("text");
                            Object rating = document.get("rating");
                            Object userid = document.get("userid");
                            DocumentReference doc_uid = document.getReference();
                            Date date = document.getDate("date");
                            if(name != null && text != null && rating != null) {
                                controller.addReview(new Review(
                                        Float.parseFloat(rating.toString()),
                                        text.toString(),
                                        name.toString(),
                                        date,
                                        doc_uid,
                                        userid.toString()
                                ));
                            }
                        }
                        this.reload(inflater, fragment);
                    }
                });
    }

    private void onDelete(Review review, LinearLayout linearLayout, View view, View barrier) {
        review.getDocumentReference().delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        linearLayout.removeView(view);
                        linearLayout.removeView(barrier);
                        Toast.makeText(this.getContext(), "Post Deletion Successful.",
                                Toast.LENGTH_SHORT).show();
                        review.destroy();
                    } else {
                        Toast.makeText(this.getContext(), "Post Deletion Failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}