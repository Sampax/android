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
        content = cont;
        location = loc;
        creator = creat;
    }
}
