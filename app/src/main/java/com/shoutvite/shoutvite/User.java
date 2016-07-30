package com.shoutvite.shoutvite;

import java.util.ArrayList;
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
    private List<Shout> joinedShouts;
    private List<String> joinedShoutsAsStrings;
    private HashMap<String, FayeChannel> channelnamesToChannels;
    private boolean nullified = true;


    public boolean isNullified(){
        return nullified;
    }

    public void nullyfyUser(){
        this.email = null;
        this.nick = null;
        this.authToken = null;
        this.password = null;
        joinedShouts = new ArrayList<Shout>();
        joinedShoutsAsStrings = new ArrayList<String>();
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
}
