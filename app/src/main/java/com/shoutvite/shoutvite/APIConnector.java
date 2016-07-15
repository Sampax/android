package com.shoutvite.shoutvite;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mari on 6/29/2016.
 */
public interface APIConnector {

    public Shout pushShout(Shout shout);
    public boolean updateShout(int id, double lat, double lon, String shout, String creator, String moderator);
    public Shout getShout(int id);
    public List<Shout> getShouts(double lat, double lon, int threshold);
    public boolean destroyShout(int id);
    public User createUser(String name, String email, String password);

}
