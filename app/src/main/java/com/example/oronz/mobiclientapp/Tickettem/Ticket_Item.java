package com.example.oronz.mobiclientapp.Tickettem;

import org.json.JSONException;
import org.json.JSONObject;

public class Ticket_Item {


    String
            passenger_name,
            phone_number,
            id_number,
            from_city,to_city,
            travel_date,selected_vehicle,seater,
            selected_seat,selected_ticket_type,
            payment_method,email_address,insurance_charge,
            served_by,amount_charged,reference_number;


    public Ticket_Item(String passenger_name, String phone_number, String id_number,
                       String from_city, String to_city, String travel_date, String selected_vehicle,
                       String seater, String selected_seat, String selected_ticket_type,
                       String payment_method, String email_address, String insurance_charge,
                       String served_by, String amount_charged, String reference_number) {

        this.passenger_name = passenger_name;
        this.phone_number = phone_number;
        this.id_number = id_number;
        this.from_city = from_city;
        this.to_city = to_city;
        this.travel_date = travel_date;
        this.selected_vehicle = selected_vehicle;
        this.seater = seater;
        this.selected_seat = selected_seat;
        this.selected_ticket_type = selected_ticket_type;
        this.payment_method = payment_method;
        this.email_address = email_address;
        this.insurance_charge = insurance_charge;
        this.served_by = served_by;
        this.amount_charged = amount_charged;
        this.reference_number = reference_number;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("passenger_name", passenger_name);
            obj.put("phone_number", phone_number);
            obj.put("id_number", id_number);
            obj.put("from_city", from_city);
            obj.put("to_city", to_city);
            obj.put("travel_date", travel_date);
            obj.put("selected_vehicle", selected_vehicle);
            obj.put("seater", seater);
            obj.put("selected_seat", selected_seat);
            obj.put("selected_ticket_type", selected_ticket_type);
            obj.put("payment_method", payment_method);
            obj.put("email_address", email_address);
            obj.put("insurance_charge", insurance_charge);
            obj.put("served_by", served_by);
            obj.put("amount_charged", amount_charged);
            obj.put("reference_number", reference_number);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


}
