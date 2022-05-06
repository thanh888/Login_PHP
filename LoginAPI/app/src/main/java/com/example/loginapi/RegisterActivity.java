package com.example.loginapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, confirm_pass;
    Button register;
    String eName, eEmail, ePassword, eConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.res_email);
        password = findViewById(R.id.res_password);
        confirm_pass = findViewById(R.id.confirm_password);
        register = findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegister();
            }
        });
    }

    private void checkRegister() {
        eName = name.getText().toString();
        eEmail = email.getText().toString();
        ePassword = password.getText().toString();
        eConfirm = confirm_pass.getText().toString();
        if (eName.isEmpty() || eEmail.isEmpty() || ePassword.isEmpty() || eConfirm.isEmpty()){
            alertFailed("Name or Email or Password or Confirm Password is Empty");
        }
        if (!ePassword.equals(eConfirm)){
            alertFailed("Password or Confirm Password is different");
        }
        else sendRegister();
    }

    private void sendRegister() {

        JSONObject params = new JSONObject();
        try {
            params.put("name", eName);
            params.put("email", eEmail);
            params.put("password", ePassword);
            params.put("password_confirmation", eConfirm);
        }catch (JSONException e){
            e.printStackTrace();
        }
        String data = params.toString();
        String url = getString(R.string.api_server)+"/register";
//        Toast.makeText(RegisterActivity.this, data+url, Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Http http = new Http(RegisterActivity.this, url);
                http.setMethod("post");
                http.setData(data);
                http.send();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer code = http.getStatusCode();
                        Toast.makeText(RegisterActivity.this, code.toString(), Toast.LENGTH_SHORT).show();
                        alertFailed(data);
                        if (code ==201 || code == 200){
                            alertSuccess("Register success");
                        }
                        else if(code == 422){
                            try {
                                JSONObject response = new JSONObject(http.getResponse());
                                String nsg = response.getString("message");
                                alertFailed(nsg);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else Toast.makeText(RegisterActivity.this, "Error" + code, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void alertSuccess(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setIcon(R.drawable.ic_check)
                .setMessage(s)
                .setPositiveButton("Login",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                }).show();
    }

    private void alertFailed(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Failed")
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