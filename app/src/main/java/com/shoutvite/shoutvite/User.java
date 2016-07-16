package com.shoutvite.shoutvite;

/**
 * Created by Jonatan on 15.7.2016.
 */
public class User {
    private String email;
    private String nick;
    private String authToken;
    private String password; //TODO: NOT the way to do this
    //TODO: I repeat, not the way


    public User(String em, String name, String auth, String psw){
        setEmail(em);
        setNick(name);
        setAuthToken(auth);
        setPassword(psw);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
