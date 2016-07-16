package com.shoutvite.shoutvite;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Jonatan on 13.7.2016.
 */
public class RailsAPI extends AsyncTask<Object, Void, Object>{

    String API_URL = "http://10.0.2.2:80/v1/";  // "http://api.shoutvite.dev/v1/";
    String actual_API_URL = "http://api.shoutvite.com/v1/";

    public String readHttpResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        int input = reader.read();
        String response = "";
        while (input != -1) {
            char letter = (char) input;
            input = reader.read();
            response = response + letter;
        }
        reader.close();
        return response;
    }

    public String POST(JSONObject json, String APIURL) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(APIURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); //makes it do POST
            connection.setDoInput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(json.toString());
            writer.flush();
            writer.close();
            os.close();
            if (connection.getResponseCode() == 200) {
                return readHttpResponse(connection);
            } else {
                Log.v("POST response code", ""+ connection.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            Log.v("POST error", e.toString());
        }finally{
            if(connection != null){
                connection.disconnect();
            }else{
                Log.v("POST connection", "already closed");
            }
        }

        return null;
    }

    public String GET(String APIURL) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(APIURL);
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            int data = reader.read();
            String message = "";
            while (data != -1) {
                char letter = (char) data;
                data = reader.read();
                message = message + letter;
            }
            return message;
        } catch (Exception e) {
            Log.v("Get", e.toString());
            return null;
        }finally{
            if(connection != null){
                connection.disconnect();
            }else{
                Log.v("GET connection", "already closed");
            }
        }
    }


    public Shout pushShout(Shout shout, User user) {
        double lat = shout.getLocation().getLatitude();
        double lon = shout.getLocation().getLongitude();
        try {
            JSONObject query = new JSONObject();
            query.put("name", shout.getContent());
            query.put("lat", lat);
            query.put("lat", lon);
            query.put("creator", shout.getCreator());
            String url = actual_API_URL + "shouts";
            String response = POST(query, url);
        }catch(Exception e){
            Log.v("error", e.toString());
        }
        return null;
    }

    public boolean updateShout(int id, double lat, double lon, String shout, String creator, String moderator) {
        return false;
    }


    public Shout getShout(int id) {
        return null;
    }

    public List<Shout> getShouts(double lat, double lon, int threshold) {
        String response = GET(actual_API_URL + "shouts?lon=" + lat + "&lat=" + lon + "&radius=" + threshold);
        try {
            JSONObject JSONResponse = new JSONObject(response);
            JSONArray shouts = JSONResponse.getJSONArray("shouts");
            Log.v("shout array size", ""+ shouts.length());

        }catch(Exception e){
            Log.v("JSON", "Something wrong with JSON");
        }
        return null;
    }

    public boolean destroyShout(int id) {
        return false;
    }

    public User createUser(String name, String email, String password) {
        JSONObject user = new JSONObject();
        try {
            user.put("username", name);
            user.put("email", email);
            user.put("password", password);
            String url = actual_API_URL + "users";
            String response = POST(user, url);
//            Log.v("POST response", response);
            JSONObject JResponse = new JSONObject(response);
            String authToken = JResponse.getString("auth_token");
            Log.v("auth token", authToken);
            User newUser = new User(email, name, authToken, password);
            return newUser;
        }catch(Exception e){
            Log.v("JSON", "JSON error" + e.toString());
            return null;

        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        AsyncTaskPayload payload = (AsyncTaskPayload)objects[0];
        try {
            List<Shout> shouts = getShouts(10, 10, 550);
            User user = createUser("Derpington", "derp@derpmail22222222.com", "salasana");

        } catch (Exception e) {
            //TODO: some sensible way of handling Exceptions
            Log.v("voi muna", e.toString());
        }

        return objects;
    }
}
