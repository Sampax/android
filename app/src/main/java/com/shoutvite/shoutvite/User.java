package com.shoutvite.shoutvite;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jonatan on 15.7.2016.
 */
public class User {
    private String email;
    private String nick;
    private String authToken;
    private String password; //TODO: NOT the way to do this
    private int user_id;
    private List<Shout> joinedShouts;
    private List<String> joinedShoutsAsStrings;
    private HashMap<String, FayeChannel> channelnamesToChannels;
    private boolean nullified = true;
    MainActivity main;



    public boolean isNullified(){
        return nullified;
    }

    public void nullyfyUser(){
        this.email = null;
        this.nick = null;
        this.authToken = null;
        this.password = null;
        joinedShouts.clear();
        joinedShoutsAsStrings.clear();
        Log.v("nullified", "user");
        channelnamesToChannels = new HashMap<String, FayeChannel>();
        this.nullified = true;
    }

    public User(String mail, String name, String auth, String psw){
        setEmail(mail);
        setNick(name);
        setAuthToken(auth);
        setPassword(psw);
        joinedShouts = new ArrayList<Shout>();
        joinedShoutsAsStrings = new ArrayList<String>();
        channelnamesToChannels = new HashMap<String, FayeChannel>();
        nullified = true;
    }

    public User(String mail, String name, String auth){
        setEmail(mail);
        setNick(name);
        setAuthToken(auth);
        joinedShouts = new ArrayList<Shout>();
        joinedShoutsAsStrings = new ArrayList<String>();
        channelnamesToChannels = new HashMap<String, FayeChannel>();
        nullified = true;
    }

    public void addJoinedShout(Shout shout){
        joinedShouts.add(shout);
        joinedShoutsAsStrings.add(shout.getContent());
        FayeChannel fayeChannel = new FayeChannel(shout.getChannel());
        channelnamesToChannels.put(shout.getChannel(), fayeChannel);
        //ArrayList<String> channelsAsList = new ArrayList<String>();
        main.createFile(this, keysFromHashMap(channelnamesToChannels));

    }

    public List<String> keysFromHashMap(HashMap<String, FayeChannel> hashMap){
        ArrayList<String> keys = new ArrayList<String>();
        Object[] keysAsObjects = hashMap.keySet().toArray();
        for(int i = 0; i < keysAsObjects.length; i++){
            keys.add((String)keysAsObjects[i]);
        }
        return keys;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.nullified = false;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
        this.nullified = false;

    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        this.nullified = false;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.nullified = false;

    }

    public List<Shout> getJoinedShouts() {
        return joinedShouts;
    }

    public void setJoinedShouts(List<Shout> joinedShouts) {
        this.joinedShouts = joinedShouts;
    }

    public List<String> getJoinedShoutsAsStrings() {
        return joinedShoutsAsStrings;
    }

    public void setJoinedShoutsAsStrings(List<String> joinedShoutsAsStrings) {
        this.joinedShoutsAsStrings = joinedShoutsAsStrings;
    }

    public HashMap<String, FayeChannel> getChannelnamesToChannels() {
        return channelnamesToChannels;
    }

    public static boolean containsShoutID(List<Shout> list, Shout shout){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getId() == shout.getId()){
                return true;
            }
        }
        return false;
    }

    public static Shout getShoutByID(List<Shout> list, int shoutId){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getId() == shoutId){
                return list.get(i);
            }
        }
        return null;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}


