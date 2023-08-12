package com.example.android_parcel_delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    TextView regBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regBtn = findViewById(R.id.regbtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://euser.info.bd/android-parcel-delivery/hello.php";

                /*
                try{
                    URL url = new URL(rUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //Set up request
                    con.setRequestMethod("GET");
                    con.connect();

                    //response
                    InputStream inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = reader.readLine()) != null){
                        result += line;
                    }


                }catch (Exception e){

                }

                 */




            }
        });

    }


    /*
    private HttpTask(String url,){
        protected String doInBackground(String.. urls, String.. method){
            String result = "";
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Set up request
                con.setRequestMethod("GET");
                con.connect();

                //response
                InputStream inputStream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null){
                    result += line;
                }


            }catch (Exception e){
        }
    }
    */

    public void loginActivity(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}