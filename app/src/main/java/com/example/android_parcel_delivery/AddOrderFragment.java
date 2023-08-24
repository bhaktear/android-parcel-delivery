package com.example.android_parcel_delivery;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android_parcel_delivery.libUtils.OkHttpUtils;
import com.example.android_parcel_delivery.libUtils.SpinnerItem;
import com.example.android_parcel_delivery.libUtils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOrderFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText deliveryDiscount,total,merchantName,merchantMobile,customerName,customerMobile,customerAddress,productType;
    private EditText productDetails,productPrice,deliveryCharge;
    private Spinner custDist,custThana,orderType,weight;
    private Button submitBtn;

    public AddOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_order, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deliveryDiscount = view.findViewById(R.id.deliveryDiscount);
        total = view.findViewById(R.id.total);
        merchantName = view.findViewById(R.id.merchantName);
        merchantMobile = view.findViewById(R.id.merchantMobile);
        customerName = view.findViewById(R.id.customerName);
        customerMobile = view.findViewById(R.id.customerMobile);
        customerAddress = view.findViewById(R.id.customerAddress);
        productType = view.findViewById(R.id.productType);
        productDetails = view.findViewById(R.id.productDetails);
        productPrice = view.findViewById(R.id.productPrice);
        deliveryCharge = view.findViewById(R.id.deliveryCharge);
        custDist = view.findViewById(R.id.customerDistrict);
        custThana = view.findViewById(R.id.customerThana);
        orderType = view.findViewById(R.id.orderType);
        weight = view.findViewById(R.id.weight);
        submitBtn = view.findViewById(R.id.addOrderBtn);

        //readonly
        deliveryCharge.setEnabled(false);
        deliveryDiscount.setEnabled(false);
        total.setEnabled(false);
        merchantMobile.setEnabled(false);
        merchantName.setEnabled(false);

        //check login
        addOrderUI();
        custDist.setOnItemSelectedListener(this);
        weight.setOnItemSelectedListener(this);
        submitBtn.setOnClickListener(this);

        productPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String weightVal = ((SpinnerItem) weight.getSelectedItem()).getValue();
                if(weightVal != ""){
                    getTotalPrice();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addOrderUI() {
        Map<String, String> authData = Utils.getAuth(requireContext());
        String url = Utils.makeURL("api/add_order_ui.php");
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        okHttpUtils.postRequest(url, authData, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG","reg" + response);
                try {
                    int err = response.getInt("err");
                    String msg = response.getString("msg");
                    if(err == 1){
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alert(requireContext(),msg,"OK","");
                                Intent intent = new Intent(requireContext(),LoginActivity.class);
                                startActivity(intent);
                                //finish();
                                //return;
                            }
                        });

                    }
                    JSONObject data = response.getJSONObject("data");
                    JSONArray orderTypeArr = data.getJSONArray("order_type");
                    List<Map<String, String>> orderTypeData = Utils.jsonArrayToMapList(orderTypeArr);

                    JSONArray distArr = data.getJSONArray("dist");
                    List<Map<String, String>> distData = Utils.jsonArrayToMapList(distArr);

                    JSONArray weightArr = data.getJSONArray("weight");
                    List<Map<String, String>> weightData = Utils.jsonArrayToMapList(weightArr);
                    merchantName.setText(data.getString("marchant_name"));
                    merchantMobile.setText(data.getString("marchant_mobile"));

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.populateSpinner(orderType,orderTypeData);
                            Utils.populateSpinner(custDist,distData);
                            Utils.populateSpinner(weight,weightData);
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

    private void addOrder(){
        Map<String, String> orderData = Utils.getAuth(getContext());
        orderData.put("marchant_name",merchantName.getText().toString());
        orderData.put("marchant_mobile", merchantMobile.getText().toString());
        orderData.put("customer_name", customerName.getText().toString());
        orderData.put("customer_mobile", customerMobile.getText().toString());
        orderData.put("customer_address", customerAddress.getText().toString());
        orderData.put("product_type", productType.getText().toString());
        orderData.put("product_details", productDetails.getText().toString());
        orderData.put("amount", productPrice.getText().toString());
        orderData.put("delivery_charge", deliveryCharge.getText().toString());
        orderData.put("discount", deliveryDiscount.getText().toString());
        orderData.put("total_amount", total.getText().toString());
        orderData.put("order_type",((SpinnerItem) orderType.getSelectedItem()).getValue());
        orderData.put("customer_district",((SpinnerItem) custDist.getSelectedItem()).getValue());
        orderData.put("customer_thana",((SpinnerItem) custThana.getSelectedItem()).getValue());
        orderData.put("weight",((SpinnerItem) weight.getSelectedItem()).getValue());
        Log.d("TAG","oData-" + orderData);

        OkHttpUtils okHttpUtils = new OkHttpUtils();
        String url = Utils.makeURL("api/add_order.php");
        okHttpUtils.postRequest(url, orderData, new OkHttpUtils.ResponseCallback() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    int err = response.getInt("err");
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.alert(getContext(),msg,"OK","");
                        }
                    });

                }catch (JSONException e){

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        SpinnerItem selected = (SpinnerItem) adapterView.getItemAtPosition(pos);
        String name = selected.getName();
        String value = selected.getValue();
        String finalSelected = "Name-" + name + " value-" + value;
        if (adapterView == custDist){
            Toast.makeText(getContext(),finalSelected,Toast.LENGTH_SHORT).show();
            if(value != ""){
                Utils.getThana(value, new Utils.ResponseCallback() {
                    @Override
                    public void onResponse(List<Map<String, String>> data) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.populateSpinner(custThana,data);
                            }
                        });
                    }
                });

            }
        }
        if(adapterView == weight){
            if(value != ""){
                //Utils.alert(getContext(),value,"Ok","");
                Utils.getDeliveryCharge(value, getContext(), new Utils.JSONRespCallback() {
                    @Override
                    public void onResponse(JSONObject obj) throws JSONException {
                        Log.d("TAG","de-" + obj.toString());
                        String delivery_charge = obj.getString("delivery_charge");
                        String discount = obj.getString("discount");
                        String total_delivery_charge = obj.getString("total_delivery_charge");

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deliveryCharge.setText(delivery_charge);
                                deliveryDiscount.setText(discount);
                                //String price = productPrice.getText().toString();
                                //int total = calculateTotal(price,total_delivery_charge);
                                getTotalPrice();
                            }
                        });

                    }
                    @Override
                    public void onError(JSONObject resp) throws JSONException{
                        String msg = resp.getString("msg");
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.alert(getContext(),msg,"ok","");
                            }
                        });
                    }
                });
            }
        }

    }

    private int calculateTotal(String price, String delivery_chage, String discount) {
        //int total = Integer.parseInt(price) + Integer.parseInt(delivery_chage) - Integer.parseInt(discount);
        int priceInt = (!price.isEmpty()) ? Integer.parseInt(price) : 0;
        int deliveryChargeInt = (!delivery_chage.isEmpty()) ? Integer.parseInt(delivery_chage) : 0;
        int discountInt = (!discount.isEmpty()) ? Integer.parseInt(discount) : 0;
        int total = priceInt + deliveryChargeInt -discountInt;
        return total;
    }

    private void getTotalPrice(){
        String price = productPrice.getText().toString();
        String delivery_charge = deliveryCharge.getText().toString();
        String discount = deliveryDiscount.getText().toString();
        int totalPrice = calculateTotal(price,delivery_charge,discount);
        Log.d("TAG","total-" + totalPrice);
        total.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(view == submitBtn){
            addOrder();
        }

    }



}