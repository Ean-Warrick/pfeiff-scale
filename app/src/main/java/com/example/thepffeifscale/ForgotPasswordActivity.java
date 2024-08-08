package com.example.thepffeifscale;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        Button submitResetButton = this.findViewById(R.id.forgotPasswordSubmitButton);
        EditText emailText = this.findViewById(R.id.forgotPasswordEmailText);
        submitResetButton.setOnClickListener(view -> {
            String email = emailText.getText().toString();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email sent.",
                                    Toast.LENGTH_SHORT).show();
                            emailText.setText("");
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "ERROR - Email not sent, please try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        Button cancelButton = this.findViewById(R.id.forgotPasswordCancelButton);
        cancelButton.setOnClickListener(view -> this.finish());
    }
}