package com.example.oronz.mobiclientapp;

public class AvailableVehicles {

   String seater,name,seats_available,departure_time,id;

    public AvailableVehicles(String seater, String name, String seats_available, String departure_time, String car_id) {
        this.seater = seater;
        this.name = name;
        this.seats_available = seats_available;
        this.departure_time = departure_time;
        this.id = car_id;


    }

    public String getSeater() {
        return seater;
    }

    public void setSeater(String seater) {
        this.seater = seater;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(String seats_available) {
        this.seats_available = seats_available;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String get_Id() {
        return id;
    }

    public void set_Id(String id) {
        this.id = id;
    }
}
