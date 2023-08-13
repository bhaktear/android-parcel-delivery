package com.example.android_parcel_delivery;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static void getDistrict(){
        Map<String, String> postData = new HashMap<>();
        String url = ServerRequest.makeURL("api/get_district.php");
        ServerRequest.performRequest("GET", url, postData, new ServerRequest.ServerResponseListener() {
            @Override
            public void onResponse(int err, String msg) {
                Log.d("TAG","msg"+err);
            }
        });

    }
}
