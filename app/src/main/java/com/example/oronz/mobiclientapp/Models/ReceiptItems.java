package com.example.oronz.mobiclientapp.Models;

public class ReceiptItems {

    String name,phone,seat,travel_date,travel_time;

    public ReceiptItems(String name, String phone, String seat, String date, String time) {

        this.name=name;
        this.phone=phone;
        this.seat=seat;
        this.travel_date=date;
        this.travel_time=time;


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

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getTravel_date() {
        return travel_date;
    }

    public void setTravel_date(String travel_date) {
        this.travel_date = travel_date;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(String travel_time) {
        this.travel_time = travel_time;
    }
}
