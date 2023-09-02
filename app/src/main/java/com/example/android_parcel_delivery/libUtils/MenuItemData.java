package com.example.android_parcel_delivery.libUtils;

public class MenuItemData {
    private String id;
    private String title;
    private String icon;
    private String className;
    private int resourceId;

    public MenuItemData(int resourceId,String id, String title, String icon,String className) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.className = className;
        this.resourceId = resourceId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getClassName(){
        return className;
    }
    public int getResourceId() {
        return resourceId;
    }

}




