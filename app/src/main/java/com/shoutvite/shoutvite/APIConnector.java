package com.shoutvite.shoutvite;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mari on 6/29/2016.
 */
public interface APIConnector {

    public boolean pushShout(Location location, String shout);
    public List<Location> getNearbyShouts(Location location, double threshold);
}
