package com.example.thepffeifscale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thepffeifscale.infinitum.bind.BindableEvent;
import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.bind.OneEvent;
import com.example.thepffeifscale.infinitum.lua.LuaCore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private static final OneEvent oneEvent = OneEvent.singleton();
    private static final Controller controller = Controller.getSingleton();
    private FirebaseAuth mAuth;
    private BindableEvent.Connection loginConnection;
    private BindableEvent.Connection signupConnection;
    private BindableEvent.Connection forgotConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerLoginSignUp, new LogInFragment())
                .commit();
        this.setUp();
    }

    private void setUp() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            LuaCore.print("USER EXISTS!");
            this.onLoggedIn(user);
        } else {
            LuaCore.print("USER DOES NOT EXIST!");
        }
        Button skipButton = this.findViewById(R.id.loginSignupSkipButton);
        skipButton.setOnClickListener(view -> {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser anonUser = mAuth.getCurrentUser();
                            this.onLoggedIn(anonUser);
                            Toast.makeText(MainActivity.this, "Anonymous Authentication Succeeded.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            LuaCore.print(task.getException().toString());
                            Toast.makeText(MainActivity.this, "Anonymous Authentication Failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        this.loginConnection = oneEvent.connect("LOGIN", this::onLogInUserButton);
        this.signupConnection = oneEvent.connect("SIGNUP", this::onSignUpUserButton);
        this.forgotConnection = oneEvent.connect("FORGOT", this::onForgotPassword);
        TabLayout tableLayout = this.findViewById(R.id.tabLayoutLoginSignUp);
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(tab.getPosition() == 0 && !fragmentManager.isDestroyed()) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_right
                            )
                            .replace(R.id.fragmentContainerLoginSignUp, new LogInFragment())
                            .commit();
                } else if(tab.getPosition() == 1 && !fragmentManager.isDestroyed()) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_left
                            )
                            .replace(R.id.fragmentContainerLoginSignUp, new SignInFragment())
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void onLogInButton(HashBundle hashBundle) {
        LuaCore.print("printing message: V");
        String email = hashBundle.getString("EMAIL");
        String password = hashBundle.getString("PASSWORD");
        LuaCore.print("Login attempt:", email, password);
        UserData userData = AppManager.login(email, password);
        if(userData != null) {
            LuaCore.print("Success!");
            Controller controller = Controller.getSingleton();
            controller.userData = userData;
            Intent intent = new Intent(MainActivity.this, MainPage.class);
            SharedPreferences shared = this.getPreferences(Context.MODE_PRIVATE);
            if(!shared.getBoolean("LOGGED_IN", false)) {
                LuaCore.print("saving:", email, password);
                shared.edit()
                        .putBoolean("LOGGED_IN", true)
                        .putString("EMAIL", email)
                        .putString("PASSWORD", password)
                        .apply();
            }
            this.startActivity(intent);
            this.finish();
        } else {
            LuaCore.print("Failure!");
        }
    }

    private void onLogInUserButton(HashBundle hashBundle) {
        String email = hashBundle.getString("EMAIL");
        LuaCore.print("IS VERIFIED:", Validation.isEmailValid(email));
        String password = hashBundle.getString("PASSWORD");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Authentication succeeded.",
                                Toast.LENGTH_SHORT).show();
                        this.onLoggedIn(user);
                    } else {
                        LuaCore.print("FAILUREEEE");
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onSignUpUserButton(HashBundle hashBundle) {
        String email = hashBundle.getString("EMAIL");
        String password = hashBundle.getString("PASSWORD");
        String username  = hashBundle.getString("USERNAME");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                        builder.setDisplayName(username);
                        user.updateProfile(builder.build());
                        this.onLoggedIn(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onForgotPassword(HashBundle hashBundle) {
        Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
        this.startActivity(intent);
    }

    private void onLoggedIn(FirebaseUser user) {
        if (user.isEmailVerified() || user.isAnonymous()) {
            Intent intent = new Intent(MainActivity.this, MainPage.class);
            this.startActivity(intent);
            this.finish();
        } else {
            Intent intent = new Intent(MainActivity.this, VerifyActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.signupConnection != null) {
            this.signupConnection.disconnect();
            this.signupConnection = null;
        }
        if (this.loginConnection != null) {
            this.loginConnection.disconnect();
            this.loginConnection = null;
        }
        if (this.forgotConnection != null) {
            this.forgotConnection.disconnect();
            this.forgotConnection = null;
        }
    }
}