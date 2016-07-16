package com.shoutvite.shoutvite;

import android.location.Location;

/**
 * Created by Jonatan on 13.7.2016.
 */
public class Shout {
    private String content;
    private Location location;
    private String creator;
    private String channel;
    private int id;

    public Shout(String cont, String creat, Location loc){
        setContent(cont);
        setLocation(loc);
        setCreator(creat);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
}
