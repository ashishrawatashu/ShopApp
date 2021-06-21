package com.example.shopapp.Models;

import java.util.ArrayList;

public class ShopsOrdersPojo {
    private String key;
    private ArrayList<OrdersPojo> ordersPojoArrayList ;

    public ArrayList<OrdersPojo> getOrdersPojoArrayList() {
        return ordersPojoArrayList;
    }

    public void setOrdersPojoArrayList(ArrayList<OrdersPojo> ordersPojoArrayList) {
        this.ordersPojoArrayList = ordersPojoArrayList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
