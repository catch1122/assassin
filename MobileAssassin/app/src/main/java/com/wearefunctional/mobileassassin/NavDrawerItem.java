package com.wearefunctional.mobileassassin;

/**
 * Created by Sondhayni on 11/21/15.
 */
public class NavDrawerItem {

    private String title;
    private int icon;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(){

    }

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }
}
