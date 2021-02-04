package com.are.vehiclemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Reset_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        EditText first_pass, second_pass;
        MaterialButton reset_password;
        TextView back;
        first_pass = findViewById(R.id.first_pass);
        second_pass = findViewById(R.id.second_pass);
        reset_password = findViewById(R.id.reset_button);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 3;
                String first_pass_ = first_pass.getText().toString().trim().length() > 5 ? first_pass.getText().toString().trim() : "";
                String second_pass_ = second_pass.getText().toString().trim().length() > 5 ? second_pass.getText().toString().trim() : "";
                if (first_pass_.length() < 5) {
                    first_pass.setError("Enter password of length >5");
                    count--;
                }
                if (second_pass_.length() < 5) {
                    second_pass.setError("Enter password of length >5");
                    count--;
                }
                if (!second_pass_.equals(first_pass_)) {
                    first_pass.setError("Password mismatch");
                    second_pass.setError("Password mismatch");
                    count--;
                }
                if (count == 3) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.updatePassword(first_pass_);
                    Toast.makeText(Reset_password.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}