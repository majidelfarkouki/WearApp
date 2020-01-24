package com.app.wearapp;

import org.json.JSONObject;

public class Message {

    private int id;
    private int student_id;
    private double gps_lat;
    private double gps_long;
    private String student_message;

    public Message(int student_id, double gps_lat, double gps_long, String student_message) {
        this.student_id = student_id;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.student_message = student_message;
    }

    public Message(int id, int student_id, double gps_lat, double gps_long, String student_message) {
        this.id = id;
        this.student_id = student_id;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.student_message = student_message;
    }

    public int getId() {
        return id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public double getGps_lat() {
        return gps_lat;
    }

    public double getGps_long() {
        return gps_long;
    }

    public String getStudent_message() {
        return student_message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public void setGps_lat(double gps_lat) {
        this.gps_lat = gps_lat;
    }

    public void setGps_long(double gps_long) {
        this.gps_long = gps_long;
    }

    public void setStudent_message(String student_message) {
        this.student_message = student_message;
    }

    @Override
    public String toString() {
        return "MessageReceiver{" +
                "id=" + getId() +
                ", student_id=" + getStudent_id() +
                ", gps_lat=" + getGps_lat() +
                ", gps_long=" + getGps_long() +
                ", message='" + getStudent_message() + '\'' +
                '}';
    }

    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("student_id", getStudent_id());
            obj.put("gps_lat", getGps_lat());
            obj.put("gps_long", getGps_long());
            obj.put("student_message", getStudent_message());
        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }

}
