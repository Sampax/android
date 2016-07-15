package com.shoutvite.shoutvite;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

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
public class RailsAPI extends AsyncTask<Object, Object, Object> implements APIConnector {

    String API_URL = "http://10.0.2.2:80/v1/";  // "http://api.shoutvite.dev/v1/";
    String actual_API_URL = "http://api.shoutvite.com/v1/";

    //for example:
    // http://api.shoutvite.dev/v1/shouts?lat=10&lon=10

    //http://stackoverflow.com/questions/9767952/how-to-add-parameters-to-httpurlconnection-using-post

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
            InputStreamReader inReader = new InputStreamReader(connection.getInputStream());
            int data = inReader.read();
            String message = "";
            while (data != -1) {
                char letter = (char) data;
                data = inReader.read();
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


    @Override
    public Shout pushShout(Shout shout) {
        return null;
    }

    @Override
    public boolean updateShout(int id, Location location, String shout, String creator, String moderator) {
        return false;
    }

    @Override
    public List<Location> getNearbyShouts(Location location, double threshold) {
        return null;
    }

    @Override
    public Shout getShout(int id) {
        return null;
    }

    @Override
    public List<Shout> getShouts(Location location, int threshold) {
        return null;
    }

    @Override
    public boolean destroyShout(int id) {
        return false;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            JSONObject user = new JSONObject();
            user.put("username", "Derpington");
            user.put("email", "derp@derp5.com");
            user.put("password", "asdfgh");
            String url = actual_API_URL + "users";
            String response = POST(user, url);
            Log.v("POST response", response);
            JSONObject JResponse = new JSONObject(response);
            String authToken = JResponse.getString("auth_token");
            Log.v("auth token", authToken);

            url = actual_API_URL + "shouts?" + "lat=10&lon=10";
            response = GET(url);
            Log.v("GET response", response);

        } catch (Exception e) {
            //TODO: some sensible way of handling Exceptions
            Log.v("voimuna", e.toString());
        }
        return null;
    }

}
