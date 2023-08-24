package com.example.android_parcel_delivery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android_parcel_delivery.libUtils.OkHttpUtils;
import com.example.android_parcel_delivery.libUtils.SpinnerItem;
import com.example.android_parcel_delivery.libUtils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class OrderStatusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Spinner orderStatus;
    Button orderStatusBtn;
    EditText statusInvoice;

    public OrderStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderStatus = view.findViewById(R.id.orderStatus);
        orderStatusBtn = view.findViewById(R.id.orderStatusBtn);
        statusInvoice = view.findViewById(R.id.statusInvoice);

        String url = Utils.makeURL("api/get_status.php");
        Map<String, String> authData = Utils.getAuth(requireContext());
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.postRequest(url, authData, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject resp) {
                try {
                    int err = resp.getInt("err");
                    String msg = resp.getString("msg");
                    if(err == 0){
                        JSONArray data = resp.getJSONArray("data");
                        List<Map<String, String>> dataList = Utils.jsonArrayToMapList(data);
                        Log.d("TAG",dataList.toString());
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.populateSpinner(orderStatus,dataList);
                            }
                        });

                    }else{
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alert(getContext(),msg,"ok","");
                            }
                        });
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onError(String error) {

            }
        });


        orderStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrderStatus();
            }
        });

    }

    private void addOrderStatus() {
        Map<String, String> data = Utils.getAuth(getContext());
        data.put("invoice",statusInvoice.getText().toString());
        data.put("status",((SpinnerItem) orderStatus.getSelectedItem()).getValue());

        OkHttpUtils okHttpUtils = new OkHttpUtils();
        String url = Utils.makeURL("api/update_order_status.php");
        okHttpUtils.postRequest(url, data, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    int err = response.getInt("err");
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.alert(getContext(), msg, "OK", "");
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