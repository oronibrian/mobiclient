package com.example.oronz.mobiclientapp.Models;

public class ManifestDetails {
    String route,total_seats,seats_available;


    public ManifestDetails(String route, String total_seats, String seats_available) {
        this.route = route;
        this.total_seats = total_seats;
        this.seats_available = seats_available;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(String total_seats) {
        this.total_seats = total_seats;
    }

    public String getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(String seats_available) {
        this.seats_available = seats_available;
    }
}
