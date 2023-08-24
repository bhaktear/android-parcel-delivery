package com.example.android_parcel_delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_parcel_delivery.libUtils.OkHttpUtils;
import com.example.android_parcel_delivery.libUtils.SpinnerItem;
import com.example.android_parcel_delivery.libUtils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {
    Button regBtn;
    TextView login;
    EditText merchantName,merchantMobile,email,customerAddress,userID,password,confirmPass;
    Spinner custDist,custThana;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        merchantName = findViewById(R.id.merchantName);
        merchantMobile = findViewById(R.id.merchantMobile);
        email = findViewById(R.id.email);
        customerAddress = findViewById(R.id.customerAddress);
        userID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPass);

        custDist = findViewById(R.id.customerDistrict);
        custThana = findViewById(R.id.customerThana);

        Utils.getDistrict(new Utils.ResponseCallback(){
            @Override
            public void onResponse(List<Map<String, String>> data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utils.populateSpinner(custDist,data);
                    }
                });
            }
        });

        custDist.setOnItemSelectedListener(this);
        regBtn = findViewById(R.id.regbtn);
        regBtn.setOnClickListener(this);
    }


    public void loginActivity(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        SpinnerItem selected = (SpinnerItem) adapterView.getItemAtPosition(pos);
        String name = selected.getName();
        String value = selected.getValue();
        String finalSelected = "Name-" + name + " value-" + value;
        if (adapterView == custDist){
            Toast.makeText(this,finalSelected,Toast.LENGTH_SHORT).show();
            if(value != ""){
                Utils.getThana(value, new Utils.ResponseCallback() {
                    @Override
                    public void onResponse(List<Map<String, String>> data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.populateSpinner(custThana,data);
                            }
                        });
                    }
                });

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(view == regBtn ){
            addReg();
        }
    }

    private void addReg() {
        String name = merchantName.getText().toString();
        String mobile = merchantMobile.getText().toString();
        String address = customerAddress.getText().toString();
        String emailad = email.getText().toString();
        String dist = ((SpinnerItem) custDist.getSelectedItem()).getValue();
        String thana = ((SpinnerItem) custThana.getSelectedItem()).getValue();
        String user_id = userID.getText().toString();
        String pass = password.getText().toString();
        String confirm_pass = confirmPass.getText().toString();

        Map<String,String> obj = new HashMap<>();
        try{
            obj.put("name",name);
            obj.put("mobile",mobile);
            obj.put("address",address);
            obj.put("email",emailad);
            obj.put("district",dist);
            obj.put("thana",thana);
            obj.put("user_id",user_id);
            obj.put("password",pass);
            obj.put("confirm_pass",confirm_pass);
            //Log.d("TAG","registerData:" + obj.toString());
            //Toast.makeText(RegisterActivity.this,obj.toString(),Toast.LENGTH_SHORT).show();
            OkHttpUtils okHttpUtils = new OkHttpUtils();
            String url = Utils.makeURL("api/reg.php");
            okHttpUtils.postRequest(url, obj, new OkHttpUtils.ResponseCallback() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String msg = response.getString("msg");
                        int err = response.getInt("err");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(err == 0){
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }else{
                                    Utils.alert(RegisterActivity.this,msg,"Ok","");
                                }

                            }
                        });
                    }catch (Exception e){

                    }

                }

                @Override
                public void onError(String error) {
                    Log.d("TAG","errorRegSubmit-" + error);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }


}