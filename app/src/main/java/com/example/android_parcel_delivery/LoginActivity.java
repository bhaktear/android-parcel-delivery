package com.example.android_parcel_delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  {
    private EditText userId,loginPassword;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userId = findViewById(R.id.userID);
        loginPassword = findViewById(R.id.password);

    }

    public void loginAction(View view){
        String user_id = userId.getText().toString();
        String password = loginPassword.getText().toString();
        if(!user_id.equals("") && !password.equals("")){
            //String text = "UserId: " + user_id + " pass: " + password;
            Map<String, String> postData = new HashMap<>();
            postData.put("user_id",user_id);
            postData.put("password",password);
            String url = "https://euser.info.bd/android-parcel-delivery/hello.php";

            ServerRequest.performRequest("GET", url, postData, new ServerRequest.ServerResponseListener() {
                @Override
                public void onResponse(int err, String message) {
                    if(err == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }



    public void registerActivity(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}