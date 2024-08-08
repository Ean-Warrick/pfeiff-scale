package com.example.thepffeifscale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thepffeifscale.infinitum.lua.LuaCore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null && user.isEmailVerified()) {
            onVerified();
        } else if(user != null) {
            sendVerificationEmail();
        }

        Button completeButton = this.findViewById(R.id.verifyCompleteButton);
        Button resendButton = this.findViewById(R.id.verifyResendVerificationButton);
        Button skipButton = this.findViewById(R.id.verifySkipButton);

        completeButton.setOnClickListener(view -> {
            FirebaseUser clickUser = mAuth.getCurrentUser();
            clickUser.reload();
            FirebaseUser reloadedUser = mAuth.getCurrentUser();
            if(reloadedUser != null && reloadedUser.isEmailVerified()) {
                this.onVerified();
            } else {
                LuaCore.print("ERROR! STILL NOT VERIFIED!");
                Toast.makeText(VerifyActivity.this, "You are not verified.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        skipButton.setOnClickListener(view -> this.onSkip());

        resendButton.setOnClickListener(view -> this.sendVerificationEmail());
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null && !user.isEmailVerified()) {
            user.sendEmailVerification();
        }
    }

    private void onVerified() {
        Intent intent = new Intent(VerifyActivity.this, MainPage.class);
        this.startActivity(intent);
        this.finish();
    }

    private void onSkip() {
        Intent intent = new Intent(VerifyActivity.this, MainPage.class);
        this.startActivity(intent);
        this.finish();
    }
}