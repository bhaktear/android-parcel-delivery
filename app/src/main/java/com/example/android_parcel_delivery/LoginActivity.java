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
        loginBtn = findViewById(R.id.submit);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = userId.getText().toString();
                String password = loginPassword.getText().toString();
                if(!user_id.equals("") && !password.equals("")){
                    Map<String, String> postData = new HashMap<>();
                    postData.put("user_id",user_id);
                    postData.put("password",password);
                    String url = ServerRequest.makeURL("api/login.php");
                    //Toast.makeText(LoginActivity.this,url,Toast.LENGTH_SHORT).show();

                    ServerRequest.performRequest("POST", url, postData, new ServerRequest.ServerResponseListener() {
                        @Override
                        public void onResponse(int err, String msg) {
                            if(err == 0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    public void registerActivity(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}