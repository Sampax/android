package com.shoutvite.shoutvite;

/**
 * Created by Jonatan on 15.7.2016.
 */
public class User {
    String email;
    String nick;
    String authToken;
    String password; //TODO: NOT the way to do this
    //TODO: I repeat, not the way


    public User(String em, String name, String auth, String psw){
        email = em;
        nick = name;
        authToken = auth;
        password = psw;
    }

}
