package com.shoutvite.shoutvite;

import android.util.Log;

import com.elirex.fayeclient.FayeClient;
import com.elirex.fayeclient.FayeClientListener;
import com.elirex.fayeclient.MetaMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jonatan on 29.7.2016.
 */
public class FayeConnector{  //this or asynctask necessary?

    MainActivity main;
    MetaMessage metaMessage;
    FayeClient client;
    String fayeURL_DEV = "ws://52.178.223.135/shout/";
    String fayeURL_launch = "wss://socket.shoutvite.me/shout/"; //"ws://52.164.249.127/shout/";
    String fayeURL = fayeURL_launch;
    String toimiva_testi_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxMiwiY2hhbm5lbCI6Ii9rYW5hdmEiLCJleHAiOjE0NzIxNjY1MDZ9.LsgMajIckfQ6vNvFT71Fwwv4MulMdh1gD8p7lHfmUqY";

    public void init(MainActivity mainActivity){
        main = mainActivity;
        metaMessage = new MetaMessage();
        JSONObject jsonExt = new JSONObject();

        try {
            Log.v("User auth token", main.user.getAuthToken());
            jsonExt.put("authToken", main.user.getAuthToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        metaMessage.setAllExt(jsonExt.toString());
        client = new FayeClient(fayeURL, metaMessage);
        client.setListener(new FayeClientListener() {
            @Override
            public void onConnectedServer(FayeClient fc) {
                Log.v("Faye", "connected nowwwwwwwwwwwwww");
//                client.subscribeChannel("/testi");
//                client.publish("/testi", "jee");
                //user, channel, message


            }

            @Override
            public void onDisconnectedServer(FayeClient fc) {
                Log.v("Faye", "disconnected now");

            }

            @Override
            public void onReceivedMessage(FayeClient fc, String msg) {
                Log.v("Faye", "Message received: "+ msg);
                try {
                    JSONObject JSONMessage = new JSONObject(msg);
                    String channel = JSONMessage.getString("channel");
                    String user = JSONMessage.getString("user");
                    String message = JSONMessage.getString("message");
                    Log.v("channel: ", channel);
                    Log.v("user: ", user);
                    Log.v("message: ", message);
                    HashMap<String, FayeChannel> map = main.user.getChannelnamesToChannels();
                    Object[] keyArray =  map.keySet().toArray();
                    for(int i = 0; i < keyArray.length; i++){
                        Log.v("key value: " , ((String)keyArray[i]));
                    }

                    if(map.containsKey(channel)){
                        map.get(channel).messages.add(new FayeMessage(message, user));
                        main.shoutFrag.updateChatIfNecessary(channel, message, user);
                        //should launch a method that updates chatmessages if necessary
                    }else{
                        Log.v("Unknown channel ", "Message for unknown channel");
                    }

                }catch (JSONException e){
                    Log.v("onreceived messageeeee", e.toString());
                    Log.v("onreceived messageeee", msg);
                }

            }
        });

        client.connectServer();

    }

    public void unsubscribeAllChannels(){
        client.unsubscribeAll();
    }

    public void subscribeToChannel(String channelname){
        Log.v("subscribed to: ", channelname);
        if(client.isConnectedServer()){
            client.subscribeChannel(channelname);
            //shout.setChannel(channelname);

        }
    }

    public void publishToChannel(String channel, String message, String user, MainActivity main){
        Log.v("publishing to: ", channel);
        if(client.isConnectedServer() & !message.equals("")){
            this.subscribeToChannel(channel);
            JSONObject JSONMessage = new JSONObject();
            try {
                JSONMessage.put("channel", channel);
                JSONMessage.put("message", message);
                JSONMessage.put("user", user);
            }catch (JSONException e){
                Log.v("Faye publish ", e.toString());
            }
            //client.publish(channel, JSONMessage.toString());
            client.publish(channel, JSONMessage.toString());
        }else{
            this.init(main);    //try to reconnect once, then the same if already connected
            if(client.isConnectedServer() & !message.equals("")) {
                this.subscribeToChannel(channel);
                JSONObject JSONMessage = new JSONObject();
                try {
                    JSONMessage.put("channel", channel);
                    JSONMessage.put("message", message);
                    JSONMessage.put("user", user);
                } catch (JSONException e) {
                    Log.v("Faye publish ", e.toString());
                }
                //client.publish(channel, JSONMessage.toString());
                client.publish(channel, JSONMessage.toString());
            }
        }
    }

    public void doShit(){
        new Thread(){
            public void run(){
                main.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            main.chatAdapter.notifyDataSetChanged();
                        }catch(IllegalStateException e){
                            Log.v("voimuna", "Illegal exception");
                        }
                    }
                });
            }
        }.start();
    }
}
