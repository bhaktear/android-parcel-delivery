package com.example.android_parcel_delivery.libUtils;

public class OrderItem {
    private String invoice;
    private String merchantName;
    private String customerName;
    private String status;

    public OrderItem(String invoice,String merchantName,String customerName, String status){
        this.invoice = invoice;
        this.merchantName = merchantName;
        this.customerName = customerName;
        this.status = status;
    }

    public String getInvoice(){
        return invoice;
    }

    public String getMerchantName(){
        return merchantName;
    }

    public String getCustomerName(){
        return customerName;
    }

    public String getStatus(){
        return status;
    }

}
