package com.example.crowdhubharmony.model;

public class UpdateStatusModel {
    private Double lat;
    private Double lon;
    private String battery;
    private Integer signal;
    private String serial_number;
    private String fcm_token;

    public UpdateStatusModel(Double lat, Double lon, String battery, Integer signal, String serial_number, String fcm_token) {
        this.lat = lat;
        this.lon = lon;
        this.battery = battery;
        this.signal = signal;
        this.serial_number = serial_number;
        this.fcm_token = fcm_token;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public Integer getSignal() {
        return signal;
    }

    public void setSignal(Integer signal) {
        this.signal = signal;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}
