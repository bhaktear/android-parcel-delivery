package com.example.android_parcel_delivery.libUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_parcel_delivery.R;

import java.util.List;



public class OrderListAdapter extends ArrayAdapter<OrderItem>{
    OrderItem orderItem;
    TextView textInvoice,textMerchantName,textCustomerName,textStatus;
    //Button addStatus;
    public OrderListAdapter(Context context, List<OrderItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_list_item, parent, false);
        }

        orderItem =getItem(position);
        textInvoice = convertView.findViewById(R.id.textInvoice);
        textCustomerName = convertView.findViewById(R.id.ListCustomerName);
        textMerchantName = convertView.findViewById(R.id.ListMerchantName);
        textStatus = convertView.findViewById(R.id.ListStatus);
        //addStatus = convertView.findViewById(R.id.addStatus);

        textInvoice.setText("Invoice: " + orderItem.getInvoice());
        textCustomerName.setText("Customer Name: " + orderItem.getCustomerName());
        textMerchantName.setText("Merchant Name: " + orderItem.getMerchantName());
        textStatus.setText("Status: " + orderItem.getStatus());
        //addStatus.setTag(orderItem.getInvoice());

        /*
        addStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invoiceId = (String) view.getTag();
                Log.d("TAG","invoice-" + invoiceId);
            }
        });

         */

        return convertView;
    }
}


