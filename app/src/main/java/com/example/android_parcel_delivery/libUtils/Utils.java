package com.example.android_parcel_delivery.libUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class Utils {

    public interface ResponseCallback {
        void onResponse(List<Map<String, String>> data);
        //void onError(String error);
    }

    public interface JSONRespCallback{
        void onResponse(JSONObject obj) throws JSONException;
        void onError(JSONObject obj) throws JSONException;
    }

    public static String makeURL(String urlPostfix){
        String domain = "https://euser.info.bd/android-parcel-delivery/";
        String url = domain + urlPostfix;
        return url;
    }

    public static Map<String, String> getAuth(Context context){
        Map<String, String> authData = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String user_id = sharedPreferences.getString("user_id","");
        if (!token.isEmpty()) {
            authData.put("token",token);
            authData.put("user_id",user_id);
        }else{
            alert(context,"Login Session Expired. Please Login Again","Ok","");
        }
        return authData;
    }



    public static void alert(Context context, String msg,String okBtn,String negBtn){
        String title = "Alert";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(okBtn, null)
                .setNegativeButton(negBtn, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void requestToken(String url,Context context,JSONRespCallback callback){
        Map<String, String> authData = getAuth(context);
        postData(url,authData,callback);
    }

    public static void getDeliveryCharge(String weight,Context context,JSONRespCallback callback){
        String url = makeURL("api/calculate_delivery_charge.php");
        Map<String, String> data = getAuth(context);
        data.put("weight",weight);
        //Log.d("TAG","url: " + url.toString());
        postData(url,data,callback);
    }

    private static void postData(String url,Map<String, String > requestData, JSONRespCallback callback){
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.postRequest(url, requestData, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    Log.d("TAG","json: err" + resp.toString());
                    int err = resp.getInt("err");
                    String msg = resp.getString("msg");
                    if(err == 0) {
                        callback.onResponse(resp.getJSONObject("data"));
                    }else{
                        callback.onError(resp);
                    }
                }catch (Exception e){
                    Log.d("TAG","jsonException:" + resp.toString());
                    Log.d("TAG","Exception-" + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public static void fetchData(String url,ResponseCallback callback){
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.getRequest(url, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    //Log.d("TAG","json: err");
                    int err = resp.getInt("err");
                    String msg = resp.getString("msg");
                    if(err == 0) {
                        JSONArray data = resp.getJSONArray("data");
                        List<Map<String, String>> dataList = jsonArrayToMapList(data);
                        callback.onResponse(dataList);
                    }else{

                    }

                }catch (Exception e){
                    Log.d("TAG","jsonException:" + resp.toString());
                    Log.d("TAG","Exception-" + e.getMessage());
                }
            }
            @Override
            public void onError(String error) {
                Log.d("TAG","Exception-" + error);
            }
        });
    }

    public static void getDistrict(ResponseCallback callback){
        String url = makeURL("api/get_district.php");
        fetchData(url,callback);
    }

    public static void getThana(String code,ResponseCallback callback){
        String url = makeURL("api/get_div_dist_thana.php?type=thana&code=" + code);
        Log.d("TAG","url: " + url.toString());
        fetchData(url,callback);
    }

    public static void populateSpinner(Spinner spinner, List<Map<String, String>> mapList) {
        List<SpinnerItem> spinnerItems = new ArrayList<>();
        spinnerItems.add(new SpinnerItem("Choose One",""));
        try{
            for (Map<String, String> map : mapList) {
                //Log.d("TAG","resp: " + map.toString());
                String name = map.get("name");
                String code = map.get("code");
                spinnerItems.add(new SpinnerItem(name,code));
            }
            Log.d("TAG","spinner: " + spinnerItems);

            ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(
                    spinner.getContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerItems
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }catch (Exception e){
            Log.d("TAG","Exception-" + e.getMessage());
        }

    }

    public static List<Map<String, String>> jsonArrayToMapList(JSONArray jsonArray) throws JSONException {
        List<Map<String, String>> mapList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String, String> map = new HashMap<>();

            // Iterate through keys of each JSON object and add them to the map
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
            }

            mapList.add(map);
        }

        return mapList;
    }



}
