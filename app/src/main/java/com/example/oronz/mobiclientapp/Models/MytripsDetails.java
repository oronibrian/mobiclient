package com.example.oronz.mobiclientapp.Models;

public class MytripsDetails {

    String travel_from,
            travel_to,
            travel_date,
            reference_number,
            amount,
            transport_company,
            vehicle_name;

    public MytripsDetails(String travel_from, String travel_to, String travel_date, String reference_number, String amount, String transport_company, String vehicle_name) {
        this.travel_from = travel_from;
        this.travel_to = travel_to;
        this.travel_date = travel_date;
        this.reference_number = reference_number;
        this.amount = amount;
        this.transport_company = transport_company;
        this.vehicle_name = vehicle_name;
    }

    public String getTravel_from() {
        return travel_from;
    }

    public void setTravel_from(String travel_from) {
        this.travel_from = travel_from;
    }

    public String getTravel_to() {
        return travel_to;
    }

    public void setTravel_to(String travel_to) {
        this.travel_to = travel_to;
    }

    public String getTravel_date() {
        return travel_date;
    }

    public void setTravel_date(String travel_date) {
        this.travel_date = travel_date;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransport_company() {
        return transport_company;
    }

    public void setTransport_company(String transport_company) {
        this.transport_company = transport_company;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }
}
