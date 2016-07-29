package com.shoutvite.shoutvite;

import java.util.Date;

/**
 * Created by Jonatan on 29.7.2016.
 */
public class FayeMessage {
    String message;
    String user;
    Date date;

    public FayeMessage(String message, String user){
        this.message = message;
        this.user = user;
    }
}
