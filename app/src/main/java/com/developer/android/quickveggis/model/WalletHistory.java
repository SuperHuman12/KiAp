package com.developer.android.quickveggis.model;

/**
 * Created by Mian Azhar on 1/29/2018.
 */

public class WalletHistory {
    String activity;
    String price;
    String user;
    String date;
    boolean isSent;

    boolean isDateSection;

    public WalletHistory(String activity, String price, String user, String date, boolean isSent, boolean isDateSection) {
        this.activity = activity;
        this.price = price;
        this.user = user;
        this.date = date;
        this.isSent = isSent;
        this.isDateSection = isDateSection;
    }

    public String getActivity() {
        return activity;
    }

    public boolean isSent() {
        return isSent;
    }

    public boolean isDateSection() {
        return isDateSection;
    }

    public void setDateSection(boolean dateSection) {
        isDateSection = dateSection;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
