package com.daslab.das.eathquekefollowerapps.Model;

/**
 * Created by User on 12/18/2017.
 */

public class EarthQUeke {
    private String place;
    private double magnitude;
    private String time;
    private String detailLink;
    private String type;
    private double lan;
    private double lon;

    public EarthQUeke() {
    }


    public EarthQUeke(String place, double magnitude, String time, String detailLink, String type, double lan, double lon) {
        this.place = place;
        this.magnitude = magnitude;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
        this.lan = lan;
        this.lon = lon;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}