package com.example.bloodbank;

public class donor_history_listitem {

    private String donor_id;
    private String blood_type;
    private String date_received;
    private String used;
    private String used_date;
    private String used_by;

    public donor_history_listitem(String donor_id, String blood_type, String date_received, String used, String used_date, String used_by) {
        this.donor_id = donor_id;
        this.blood_type = blood_type;
        this.date_received = date_received;
        this.used = used;
        this.used_date = used_date;
        this.used_by = used_by;
    }

    public String getDonor_id() {
        return donor_id;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public String getDate_received() {
        return date_received;
    }

    public String getUsed() {
        return used;
    }

    public String getUsed_date() {
        return used_date;
    }

    public String getUsed_by() {
        return used_by;
    }
}
