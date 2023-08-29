package com.example.android_parcel_delivery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_parcel_delivery.libUtils.OkHttpUtils;
import com.example.android_parcel_delivery.libUtils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;


public class OrderSearchFragment extends Fragment {
    EditText statusInvoice;
    Button orderStatusBtn;
    TextView searchResult;

    public OrderSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusInvoice = view.findViewById(R.id.statusInvoice);
        orderStatusBtn = view.findViewById(R.id.orderStatusBtn);
        searchResult = view.findViewById(R.id.searchResult);

        orderStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackOrder();
            }
        });
    }

    private void trackOrder() {
        Map<String, String> data = Utils.getAuth(getContext());
        data.put("invoice",statusInvoice.getText().toString());

        OkHttpUtils okHttpUtils = new OkHttpUtils();
        String url = Utils.makeURL("api/search.php");
        okHttpUtils.postRequest(url, data, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    int err = response.getInt("err");

                    if(err == 1){
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alert(getContext(), msg, "OK", "");
                            }
                        });
                    }else{
                        //String searchData = "";
                        JSONArray data = response.getJSONArray("data");
                        if(data.length() >0){
                            JSONObject obj = data.getJSONObject(0);
                            String invoice = obj.getString("invoice");
                            String custName = obj.getString("customer_name");
                            String merchantName = obj.getString("marchant_name");
                            String status = obj.getString("status");

                            String searchData = "Invoice- " + invoice + "\n"
                                    + "Merchant Name- " + merchantName + "\n"
                                    + "Customer Name- " + custName + "\n"
                                    + "Status- " + status;



                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),searchData,Toast.LENGTH_SHORT).show();
                                    //searchResult.setVisibility(true);
                                    searchResult.setText(searchData);
                                }
                            });
                        }


                    }

                }catch (Exception e){

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}