package com.example.oronz.mobiclientapp.Models;

import java.util.Comparator;

public class PassengerManifestDetails {

    String seat,refno,reg_no,name,phone,route,route_from;

    public PassengerManifestDetails(String seat, String refno, String reg_no, String name, String phone, String route, String route_from) {
        this.seat = seat;
        this.refno = refno;
        this.reg_no = reg_no;
        this.name = name;
        this.phone = phone;
        this.route = route;
        this.route_from = route_from;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoute_from() {
        return route_from;
    }

    public void setRoute_from(String route_from) {
        this.route_from = route_from;
    }

    public static Comparator<PassengerManifestDetails> Seatno = new Comparator<PassengerManifestDetails>() {

        public int compare(PassengerManifestDetails s1, PassengerManifestDetails s2) {

            String seatno1 =s1.getRefno();
            String seatno2 = s2.getRefno();

            int value = Integer.parseInt(seatno1.replaceAll("[^0-9]", ""));
            int value2 = Integer.parseInt(seatno2.replaceAll("[^0-9]", ""));

            /*For ascending order*/
            return value - value2;

            /*For descending order*/
            //rollno2-rollno1;
        }};

}
