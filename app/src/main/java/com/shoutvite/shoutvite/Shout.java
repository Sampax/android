package com.shoutvite.shoutvite;

/**
 * Created by Jonatan on 13.7.2016.
 */
public class Shout {
    private String content;
    private double lat;
    private double lon;
    private String owner;
    private String channel;
    private int id;

    public Shout(String cont, String creat, double latit, double longi){
        content = cont;
        lat = latit;
        lon = longi;
        owner = creat;
    }
    public Shout(String cont, double latit, double longi){
        content = cont;
        lat = latit;
        lon = longi;
    }

    public Shout(int id, String cont, String channel, String own, double latit, double longi){
        this.id = id;
        content = cont;
        this.channel = channel;
        owner = own;
        lat = latit;
        lon = longi;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getOwner() {
        return owner;
    }

    public void setOwner(String creator) {
        this.owner = creator;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
