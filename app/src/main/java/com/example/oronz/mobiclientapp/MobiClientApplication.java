package com.example.oronz.mobiclientapp;

import android.app.Application;
import android.widget.ArrayAdapter;

public class MobiClientApplication extends Application {


    String user_name,hash_key,api_key;
    String _Clerk_password,_Clerk_username,seatNo;
    String travel_date,travel_too,travel_from;
    String twoway_return_date,twoway_travel_date,twoway_travel_too,twoway_travel_from;
    ArrayAdapter<String> buses;
    private String payment_type,selected_vehicle;


    private String name;
    private String phone,car_name,remaining_seats;
    private String ID;
    int no_passenges;


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

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getTwoway_return_date() {
        return twoway_return_date;
    }

    public void setTwoway_return_date(String twoway_return_date) {
        this.twoway_return_date = twoway_return_date;
    }

    public String getTwoway_travel_date() {
        return twoway_travel_date;
    }

    public void setTwoway_travel_date(String twoway_travel_date) {
        this.twoway_travel_date = twoway_travel_date;
    }

    public String getTwoway_travel_too() {
        return twoway_travel_too;
    }

    public void setTwoway_travel_too(String twoway_travel_too) {
        this.twoway_travel_too = twoway_travel_too;
    }

    public String getTwoway_travel_from() {
        return twoway_travel_from;
    }

    public void setTwoway_travel_from(String twoway_travel_from) {
        this.twoway_travel_from = twoway_travel_from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void set_selected_vehicle(String selected_vehicle) {
        this.selected_vehicle = selected_vehicle;
    }

    public String get_selected_vehicle() {
        return selected_vehicle;
    }

    public void set_no_passenges(int no_passenges) {
        this.no_passenges = no_passenges;
    }

    public int get_no_passenges() {
        return no_passenges;
    }

    public void set_car_name(String car_name) {
        this.car_name = car_name;
    }

    public String get_car_name() {
        return car_name;
    }

    public void set_remaining_seats(String remaining_seats) {
        this.remaining_seats = remaining_seats;
    }

    public String get_remaining_seats() {
        return remaining_seats;
    }
}
