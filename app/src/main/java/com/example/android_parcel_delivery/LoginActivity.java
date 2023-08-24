package com.example.android_parcel_delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android_parcel_delivery.libUtils.OkHttpUtils;
import com.example.android_parcel_delivery.libUtils.Utils;

import org.json.JSONObject;

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
        //Utils.alert(this,"Hello");
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {
        String user_id = userId.getText().toString();
        String password = loginPassword.getText().toString();
        if(!user_id.equals("") && !password.equals("")) {
            Map<String, String> postData = new HashMap<>();
            postData.put("user_id", user_id);
            postData.put("password", password);
            String url = Utils.makeURL("api/login.php");

            OkHttpUtils okHttpUtils = new OkHttpUtils();
            okHttpUtils.postRequest(url, postData, new OkHttpUtils.ResponseCallback() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("TAG","reg" + response);
                    try{
                        String msg = response.getString("msg");
                        int err = response.getInt("err");
                        if(err == 0){
                            //store data into session
                            String loggedInUserId = response.getString("user_id");
                            String token = response.getString("token");
                            SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", loggedInUserId);
                            editor.putString("token",token);
                            editor.apply();
                        }
                        //Log.d("TAG","reg- " + msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Utils.alert(LoginActivity.this,msg,"Ok","");
                                if(err == 0){
                                    //call dashboard

                                    Intent intent = new Intent(LoginActivity.this,NavigationActivity.class);
                                    startActivity(intent);
                                }else{
                                    Utils.alert(LoginActivity.this,msg,"Ok","");
                                }
                            }
                        });



                    }catch (Exception e){
                        //Log.d("TAG","exp" + e.getMessage());
                    }
                }

                @Override
                public void onError(String error) {

                }
            });

        }
    }

    public void registerActivity(View view){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}