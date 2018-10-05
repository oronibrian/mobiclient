package com.example.oronz.mobiclientapp;

import android.app.Application;
import android.widget.ArrayAdapter;

public class MobiClientApplication extends Application {


    String user_name,hash_key,api_key;
    String _Clerk_password,_Clerk_username;
    String travel_date,travel_too,travel_from;
    String twoway_return_date,twoway_travel_date,twoway_travel_too,twoway_travel_from;
    ArrayAdapter<String> buses;

    public MobiClientApplication(){}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setHash_key(String hash_key) {
        this.hash_key = hash_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getHash_key() {
        return hash_key;
    }

    public String getApi_key() {
        return api_key;
    }

    public void set_Clerk_password(String _Clerk_password) {
        this._Clerk_password = _Clerk_password;
    }

    public void set_Clerk_username(String _Clerk_username) {
        this._Clerk_username = _Clerk_username;
    }

    public String get_Clerk_password() {
        return _Clerk_password;
    }

    public String get_Clerk_username() {
        return _Clerk_username;
    }

    public void setTravel_date(String travel_date) {
        this.travel_date = travel_date;
    }

    public String getTravel_date() {
        return travel_date;
    }

    public void setTravel_too(String travel_too) {
        this.travel_too = travel_too;
    }

    public String getTravel_too() {
        return travel_too;
    }

    public void setTravel_from(String travel_from) {
        this.travel_from = travel_from;
    }

    public String getTravel_from() {
        return travel_from;
    }

    public ArrayAdapter<String> getBuses() {
        return buses;
    }


    public void setBuses(ArrayAdapter<String> buses) {
        this.buses = buses;
    }

    public void set_Twoway_return_date(String twoway_return_date) {
        this.twoway_return_date = twoway_return_date;
    }

    public String get_Twoway_return_date() {
        return twoway_return_date;
    }

    public void set_Twoway_Travel_date(String twoway_travel_date) {
        this.twoway_travel_date = twoway_travel_date;
    }

    public String get_Twoway_Travel_date() {
        return twoway_travel_date;
    }

    public void set_Twoway_Travel_too(String twoway_travel_too) {
        this.twoway_travel_too = twoway_travel_too;
    }

    public String get_Twoway_Travel_too() {
        return twoway_travel_too;
    }

    public void set_Twoway_Travel_from(String twoway_travel_from) {
        this.twoway_travel_from = twoway_travel_from;
    }

    public String get_Twoway_Travel_from() {
        return twoway_travel_from;
    }
}
