package com.shoutvite.shoutvite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jonatan on 29.7.2016.
 */
public class FayeChannel {
    List<FayeMessage> messages;
    String channel;

    public FayeChannel(String fayeChannel){
        channel = fayeChannel;
        messages = new ArrayList<FayeMessage>();
    }

}
