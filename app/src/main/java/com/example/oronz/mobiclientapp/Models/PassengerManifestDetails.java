package com.example.oronz.mobiclientapp.Models;

public class PassengerManifestDetails {

    String seat,refno,reg_no,name;

    public PassengerManifestDetails(String seat, String refno, String reg_no, String name) {
        this.seat = seat;
        this.refno = refno;
        this.reg_no = reg_no;
        this.name = name;
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
}
