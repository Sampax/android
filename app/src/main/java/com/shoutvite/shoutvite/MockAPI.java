package com.shoutvite.shoutvite;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mari on 6/29/2016.
 */
public class MockAPI implements APIConnector {
    List<Location> locations = null;
    List<String> messages = null;

    public MockAPI() {
        messages = new ArrayList<String>();
        locations = new ArrayList<Location>();
    }


    public MockAPI(Location base){
        double lat = base.getLatitude();
        double lon = base.getLongitude();
        double[] latOffsets = {0.001, 0.03, 0.002, 0.025, 0.1};
        double[] lonOffsets = {0.001, 0.027, 0.0007, 0.022, 0.1};
        String[] mockMessages = {"jee1", "jee2", "jee3", "jee4", "jee5", "jee6"};
        messages = new ArrayList<String>();
        Location AuxLocation = null;
        locations = new ArrayList<Location>();
        for(int i = 0; i < latOffsets.length; i++) {
            AuxLocation = new Location(base);
            AuxLocation.setLatitude(lat + latOffsets[i]);
            AuxLocation.setLongitude(lon + lonOffsets[i]);
            locations.add(AuxLocation);
            messages.add(mockMessages[i]);
        }
    }


    public boolean pushShout(Location location, String shout) {
        locations.add(location);
        messages.add(shout);
        return true;
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
    public List<Location> getNearbyShouts(Location location, double distThreshold){
        List<Location> nbLocations = new ArrayList<Location>();
        for(int i = 0; i < locations.size(); i++){
            if(location.distanceTo(locations.get(i)) < distThreshold){
                nbLocations.add(locations.get(i));

            }
        }
        return nbLocations;
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
}
