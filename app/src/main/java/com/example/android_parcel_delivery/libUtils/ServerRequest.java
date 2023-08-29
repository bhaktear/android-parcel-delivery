package com.example.android_parcel_delivery.libUtils;



import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerRequest {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    List<Map<String, Object>> respData = new ArrayList<>();



    public interface ServerResponseListener {
        void onResponse(int err, String msg);
    }

    public static void performRequest(final String method, final String url, final Map<String, String> postData, final ServerResponseListener listener) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL urlObj = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
                    connection.setRequestMethod(method);

                    if (method.equals("POST")) {
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setDoOutput(true);

                        // Encode POST data
                        StringBuilder postDataString = new StringBuilder();
                        for (Map.Entry<String, String> entry : postData.entrySet()) {
                            if (postDataString.length() != 0) {
                                postDataString.append("&");
                            }
                            postDataString.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                            postDataString.append("=");
                            postDataString.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                        }

                        try (OutputStream outputStream = connection.getOutputStream()) {
                            outputStream.write(postDataString.toString().getBytes("UTF-8"));
                            outputStream.flush();
                        }
                    }

                    // Read and process the response
                    int responseCode = connection.getResponseCode();
                    //showToastOnMainThread(context,responseCode);
                    Log.d("TAG","" +responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        // Parse the JSON response
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        Log.d("TAG","msg" + jsonResponse.toString());
                        int err = jsonResponse.getInt("err");
                        String msg = jsonResponse.getString("msg");
                        JSONObject respData = jsonResponse.getJSONObject("data");
                        Log.d("TAG","resp" +respData);
                        listener.onResponse(err, msg);
                    }else{
                        listener.onResponse(1, "Error");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    listener.onResponse(1, e.getMessage());
                }
            }
        });
    }

    private static void showToastOnMainThread(final Context context, final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

