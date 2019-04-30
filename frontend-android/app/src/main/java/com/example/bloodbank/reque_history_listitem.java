package com.example.bloodbank;

public class reque_history_listitem {

    private  String date_requested;
    private  String accepted;
    private  String date_accepted;

    public reque_history_listitem(String date_requested, String accepted, String date_accepted) {
        this.date_requested = date_requested;
        this.accepted = accepted;
        this.date_accepted = date_accepted;
    }

    public String getDate_requested() {
        return date_requested;
    }

    public String getAccepted() {
        return accepted;
    }

    public String getDate_accepted() {
        return date_accepted;
    }
}
