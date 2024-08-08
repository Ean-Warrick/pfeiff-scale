package com.example.thepffeifscale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.bind.OneEvent;
import com.example.thepffeifscale.infinitum.lua.LuaCore;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainPage extends AppCompatActivity {

    private HomeFragment homeFragment;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        this.mAuth = FirebaseAuth.getInstance();
        OneEvent.singleton().connect("LOGOUT", this::onLogOut);
        this.setUp();
    }

    private void onLogOut(HashBundle hashBundle) {
        this.mAuth.signOut();
        Intent intent = new Intent(MainPage.this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    private void onGoTo(HashBundle hashBundle) {
        runOnUiThread(() -> {
            int destination = hashBundle.getInt("DESTINATION");
            TabLayout tabLayout = this.findViewById(R.id.mainTabLayout);
            tabLayout.selectTab(tabLayout.getTabAt(destination));
        });
    }

    private void setUp() {
        OneEvent.singleton().connect("GOTO", this::onGoTo);
        TabLayout tabLayout = this.findViewById(R.id.mainTabLayout);

        ImageButton searchButton = this.findViewById(R.id.mainSearchButton);
        searchButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainPage.this, SearchActivity.class);
            this.startActivity(intent);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            int current = 0;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int difference = current - tab.getPosition();
                int inSlide = R.anim.slide_in_right;
                int outSlide = R.anim.slide_out_right;
                if (difference < 0) {
                    inSlide = R.anim.slide_in_left;
                    outSlide = R.anim.slide_out_left;
                }
                current = tab.getPosition();
                if(tab.getPosition() == 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if(!fragmentManager.isDestroyed()) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        inSlide,
                                        outSlide,
                                        inSlide,
                                        outSlide
                                )
                                .replace(R.id.mainFragmentContainer, getHomeFragment())
                                .commit();
                    }
                } else if(tab.getPosition() == 1) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if(!fragmentManager.isDestroyed()) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        inSlide,
                                        outSlide,
                                        inSlide,
                                        outSlide
                                )
                                .replace(R.id.mainFragmentContainer, new ReviewFragment())
                                .commit();
                    }

                } else if (tab.getPosition() == 2) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if(!fragmentManager.isDestroyed()) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(
                                        inSlide,
                                        outSlide,
                                        inSlide,
                                        outSlide
                                )
                                .replace(R.id.mainFragmentContainer, new ProfileFragment())
                                .commit();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private HomeFragment getHomeFragment() {
        if(homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }
}