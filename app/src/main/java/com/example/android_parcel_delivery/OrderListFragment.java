package com.example.android_parcel_delivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android_parcel_delivery.libUtils.OkHttpUtils;
import com.example.android_parcel_delivery.libUtils.OrderItem;
import com.example.android_parcel_delivery.libUtils.OrderListAdapter;
import com.example.android_parcel_delivery.libUtils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderListFragment extends Fragment {

    ListView listView;
    public OrderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.orderListData);

        //String url = Utils.makeURL("api/order_list.php");
        orderList();

    }

    public void orderList(){
        Map<String, String> authData = Utils.getAuth(requireContext());
        String url = Utils.makeURL("api/order_list.php");
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.postRequest(url, authData, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("TAG","orderList- " + response.toString());
                try {
                    int err = response.getInt("err");
                    String msg = response.getString("msg");
                    if (err == 1) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alert(requireContext(), msg, "OK", "");
                            }
                        });
                    }
                    JSONArray dataArray = response.getJSONArray("data");
                    List<OrderItem> orderItemList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject orderData = dataArray.getJSONObject(i);
                        String invoice = orderData.getString("invoice");
                        String merchantName = orderData.getString("marchant_name"); // Correct the field name
                        String customerName = orderData.getString("customer_name");
                        String status = orderData.getString("status");

                        OrderItem orderItem = new OrderItem(invoice, merchantName, customerName, status);
                        orderItemList.add(orderItem);
                    }

                    OrderListAdapter adapter = new OrderListAdapter(requireContext(), orderItemList);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                        }
                    });



                }catch (Exception e){

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}