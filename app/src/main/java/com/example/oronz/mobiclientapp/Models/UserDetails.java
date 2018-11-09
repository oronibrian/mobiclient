package com.example.oronz.mobiclientapp.Models;

public class UserDetails {

    String name,
            phone,
            is,
    seat;

    public UserDetails() {

    }

    public UserDetails(String name, String phone, String is, String seat) {
        this.name = name;
        this.phone = phone;
        this.is = is;
        this.seat = seat;
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

    public String getIs() {
        return is;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public void setIs(String is) {
        this.is = is;
    }
}
