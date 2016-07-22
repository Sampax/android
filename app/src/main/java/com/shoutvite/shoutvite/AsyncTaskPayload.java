package com.shoutvite.shoutvite;

import java.util.List;

/**
 * Created by Lauri on 16.7.2016.
 * Used to identify asynctasks and transfer the parameters and return values
 */
public class AsyncTaskPayload {
    public static final int PUSH_SHOUT = 1;
    public static final int GET_SINGLE_SHOUT = 2;
    public static final int GET_SHOUTS = 3;
    public static final int UPDATE_SHOUT = 4;
    public static final int DESTROY_SHOUT = 5;
    public static final int CREATE_USER = 6;
    public static final int LOGIN = 7;

    public int task;

    public double lat;
    public double lon;

    public Shout shout;
    public List<Shout> shoutList;

    public User user;

    public int radius;
    public int id;

    public static AsyncTaskPayload createShoutPayload(Shout shout, User user){
        AsyncTaskPayload payload = new AsyncTaskPayload();
        payload.task = PUSH_SHOUT;
        payload.user = user;
        payload.shout = shout;
        return payload;
    }

    public static AsyncTaskPayload createShoutPayload(Shout shout){
        AsyncTaskPayload payload = new AsyncTaskPayload();
        payload.task = PUSH_SHOUT;
        payload.shout = shout;
        return payload;
    }

    public static AsyncTaskPayload getShoutsPayload(double lat, double lon, int radius){
        AsyncTaskPayload payload = new AsyncTaskPayload();
        payload.task = GET_SHOUTS;
        payload.lat = lat;
        payload.lon = lon;
        payload.radius = radius;
        return payload;
    }

    public static AsyncTaskPayload createUserPayload(User user){
        AsyncTaskPayload payload = new AsyncTaskPayload();
        payload.task = CREATE_USER;
        payload.user = user;
        return payload;
    }

    public static AsyncTaskPayload createLoginPayload(User user){
        AsyncTaskPayload payload = new AsyncTaskPayload();
        payload.task = LOGIN;
        payload.user = user;
        return payload;
    }


}
