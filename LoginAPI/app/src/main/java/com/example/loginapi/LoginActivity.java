package com.example.loginapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button button_login, button_register;
    String eEmail, ePassword;
    LocalStorage localStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        localStorage = new LocalStorage(LoginActivity.this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                sendLogin();
                checkLogin();
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin() {
        eEmail= email.getText().toString();
        ePassword = password.getText().toString();
        if (eEmail.isEmpty() || ePassword.isEmpty()){
            alertFailed("Email or password is empty");
        }
        else{
            sendLogin();
        }

    }

    private void sendLogin() {
        JSONObject params = new JSONObject();
        try {
            params.put("email", eEmail);
            params.put("password", ePassword);
        }catch(Exception e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"/login";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(LoginActivity.this, url);
                http.setMethod("post");
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
                        if (code==200){
                            try {
                                JSONObject response= new JSONObject(http.getResponse());
                                String token = response.getString("token");
                                localStorage.setToken(token);
                                Intent intent= new Intent(LoginActivity.this, UserActivity.class);
                                startActivity(intent);
                                finish();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else if(code==422)
                        {
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("mess");
                                alertFailed(msg);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else if(code == 401){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String msg = response.getString("mess");
                                alertFailed(msg);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else Toast.makeText(LoginActivity.this, "Error"+ code, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void alertFailed(String s) {
        new AlertDialog.Builder(this).setTitle("Failed")
                .setIcon(R.drawable.ic_warning)
                .setMessage(s)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}