package com.example.nora.bcarebabymonitor.model;

/**
 * Created by nora on 12/09/16.
 */
public class HistoryObject {
    private String date;
    private String status;
    private String popok;
    private String cry;
    private String temp;
    private String hum;

    public HistoryObject(){

    }

    public HistoryObject(String date, String status, String popok, String cry, String temp, String hum) {
        this.date = date;
        this.status = status;
        this.popok = popok;
        this.cry = cry;
        this.temp = temp;
        this.hum = hum;
    }
    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getPopok() {
        return popok;
    }

    public String getCry() {
        return cry;
    }

    public String getTemp() {
        return temp;
    }

    public String getHum() {
        return hum;
    }

}
