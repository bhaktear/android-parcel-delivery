package com.example.android_parcel_delivery.libUtils;

import java.util.List;

public class ApiResponse<T> {
    private int err;
    private String msg;
    private List<T> data;

    public int getErr(){
        return err;
    }

    public String getMsg(){
        return msg;
    }

    public List<T> getData(){
        return data;
    }

}
