package com.example.nora.bcarebabymonitor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nora on 06/09/16.
 */
public class ItemObject {
    private String temp;
    private String hum;
    private String popok;
    private String cry;
    private String face;

    public ItemObject(){

    }

    public ItemObject(String temp, String hum, String popok, String cry, String face) {
        this.temp = temp;
        this.hum = hum;
        this.popok = popok;
        this.cry = cry;
        this.face = face;
    }
    public String getTemp() {
        return temp;
    }

    public String getHum() {
        return hum;
    }

    public String getPopok() {
        return popok;
    }
    public String getCry() {
        return cry;
    }
    public String getFace() { return face;}




}
