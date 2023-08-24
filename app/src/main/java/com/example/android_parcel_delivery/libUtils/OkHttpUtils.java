package com.example.android_parcel_delivery.libUtils;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    private OkHttpClient client = new OkHttpClient();

    public interface ResponseCallback{
        void onResponse(JSONObject response);
        void onError(String error);
    }

    public void getRequest(String url,ResponseCallback callback){
        Request request = new Request.Builder().url(url).build();
        executeRequest(request,callback);
    }

    public void postRequest(String url, Map<String,String> postData,ResponseCallback callback){

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : postData.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }

        RequestBody requestBody = formBodyBuilder.build();
        Log.d("TAG","params"+ requestBody);

         /*
        //MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(
                okhttp3.MediaType.get("application/json; charset=utf-8"),
                postData.toString()
        );

          */
        Log.d("TAG","requestBody:" + postData.toString());
        Request request = new Request.Builder().url(url).post(requestBody).build();
        executeRequest(request,callback);
    }

    private void executeRequest(Request request, ResponseCallback callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //Log.d("TAG","resp: "+ response);
                //int respCode = response.code();
                if(response.isSuccessful()){
                    String resp = response.body().string();
                    Log.d("TAG","resp: "+ resp);
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        callback.onResponse(jsonObject);
                        //int err = obj.getInt("err");
                        //String msg = obj.getString("msg");

                        //JSONArray data = new JSONArray("data");

                        //Log.d("TAG","resp: "+ obj);
                        //Log.d("TAG","resp: "+ err);
                        //Log.d("TAG","resp: "+ msg);
                        //Log.d("TAG","resp: "+ data);
                    }catch (Exception e){
                        e.printStackTrace();
                        callback.onError("Error parsing JSON");
                    }
                }else{
                    callback.onError(response.toString());
                }
            }
        });
    }


}
