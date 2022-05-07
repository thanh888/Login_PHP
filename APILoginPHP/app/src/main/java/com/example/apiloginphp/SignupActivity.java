package com.example.apiloginphp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText editFullname, editUsername, editPassword, editEmail;
    Button btnsignup;
    TextView tvLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editFullname = findViewById(R.id.fullname);
        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        editEmail = findViewById(R.id.email);
        btnsignup = findViewById(R.id.buttonSignUp);
        tvLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = editFullname.getText().toString();
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String email = editEmail.getText().toString();

                if (fullname.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                }else {
                   progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "password";
                            field[3] = "email";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = fullname;
                            data[1] = username;
                            data[2] = password;
                            data[3] = email;
                            PutData putData = new PutData("http://192.168.1.7/Login_php/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext() , result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                        Toast.makeText(getApplicationContext() , result, Toast.LENGTH_SHORT).show();
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });

                }
                
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}