package com.shoutvite.shoutvite;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mari on 6/29/2016.
 */
public interface APIConnector {

    public Shout pushShout(Shout shout);
    public boolean updateShout(int id, Location location, String shout, String creator, String moderator);
    public List<Location> getNearbyShouts(Location location, double threshold);
    public Shout getShout(int id);
    public List<Shout> getShouts(Location location, int threshold);
    public boolean destroyShout(int id);

}
