package com.example.shopapp.Models;

public class ShopsPojo {
    private String shopName;
    private String lat;
    private String log;
    private String id;
    private String shopPhone;
    private String shopMail;
    private String shopProfileImageUrl;

    public String getShopProfileImageUrl() {
        return shopProfileImageUrl;
    }

    public void setShopProfileImageUrl(String shopProfileImageUrl) {
        this.shopProfileImageUrl = shopProfileImageUrl;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopMail() {
        return shopMail;
    }

    public void setShopMail(String shopMail) {
        this.shopMail = shopMail;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
