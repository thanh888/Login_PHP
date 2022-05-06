package com.example.loginapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvTime;
    Button button_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvTime = findViewById(R.id.tvTime);
        button_logout = findViewById(R.id.button_logout);

        getUser();
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout() {
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getUser() {
        tvName.setText("Name: ");
        tvEmail.setText("Email: ");
        tvTime.setText("Create at: ");
    }
}