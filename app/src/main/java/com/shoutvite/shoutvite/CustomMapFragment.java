package com.shoutvite.shoutvite;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lauri on 6/20/2016.
 */
public class CustomMapFragment extends Fragment implements OnMapReadyCallback {

    public View view = null;
    public ViewGroup previousContainer = null;
    private final int MY_LOCATION_ACCESS = 0;
    Location lastLocation = null;
    GoogleMap map = null;
    int DISTANCE_THRESHOLD = 500000;
    int ZOOM_LEVEL = 15;
    BitmapDescriptor bitmap = null;
    Bitmap bmap = null;


    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance){
        //need to get rid of the old view from the parent before inflating a new one
        if(view != null && this.previousContainer == container ){
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }else{
            view = inflater.inflate(R.layout.map_layout, container, false);
            previousContainer = container;
        }
        ArrayList<String> shouts = new ArrayList<String>();
        shouts.add("fug");
        shouts.add("jees");
        shouts.add("Android");
        shouts.add("on");
        shouts.add("perseestä");
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
        bmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        bmap = Bitmap.createScaledBitmap(bmap, 100, 100, false);        //[TODO]should scale depending on screen size

        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.logo);
        bitmap = BitmapDescriptorFactory.fromBitmap(bmap);
        //custom adapter to access textView because android is a piece of shit software that should be exterminated
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.shout, R.id.shout_text, shouts){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setBackground(new BitmapDrawable(getResources(), bmap));
                return textView;
            }
        };
        ListView listView = (ListView)view.findViewById(R.id.map_shout_list);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.addMarker(new MarkerOptions().position(new LatLng(61, 40)).title("own_location"));
        LatLng initLocation = getInitialLocation();
        CameraPosition pos = null;
        double FIN_LAT = 64.9241;
        double FIN_LON = 25.7482;
        int zoom = 15;
        int permission = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (initLocation == null) {
            //new LatLng(70,80)
            initLocation = new LatLng(FIN_LAT, FIN_LON);
            zoom = 4;
        }
        googleMap.setMyLocationEnabled(true);   //adds the marker for user position
        pos = new CameraPosition(initLocation, zoom, 0, 0);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }
    public LatLng getInitialLocation() {
        double lat = 0;
        double lon = 0;
        Criteria crit = new Criteria();
        //    MockLocationProvider mock = new MockLocationProvider(LocationManager.GPS_PROVIDER, this);
        //    mock.pushLocation(64, 14);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
                //TODO: crashes when re-launching the app (at least after modifications, apparently pointer to activity is null
                ((MainActivity)getActivity()).API = new MockAPI(location);
                //TODO: check if safe, also consider refactoring location elsewhere (myLocation class?):
                ((MainActivity)getActivity()).lastLocation = location;

                LatLng latlon = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition pos = new CameraPosition(latlon, ZOOM_LEVEL, 0,0);
                map.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
                Log.v("location update", "updated location 2");
                APIConnector api = ((MainActivity) getActivity()).API;
//                Log.v("markers", "entryjä" + api.locations.size());
                List<Location> locations =  api.getNearbyShouts(location, DISTANCE_THRESHOLD);
                //[TODO: add Markeroptions.archor() if necessary to center markers (check if markers are centered]
                Log.v("markers", "locations koko: "+ locations.size());
                for(int i = 0; i < locations.size(); i++){
                    Location aux = locations.get(i);


                   map.addMarker(new MarkerOptions().position(new LatLng(aux.getLatitude(), aux.getLongitude())).icon(bitmap)); //adds these eeeeevery time
                    Log.v("markers", "added " + i);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.v("provider status", provider);

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.v("provider enabled", provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.v("provider disabled", provider);
            }
        };
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_ACCESS);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.v("missing", "yes it is");
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);     //  test:  String provider = locationManager.getProviders(true).get(0);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);     //  test:  String provider = locationManager.getProviders(true).get(0);
        //get all providers to find one that works... I'm sure this is very unefficient
        boolean print = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(print) {
            Log.v("providers","network");
        }
        print = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(print) {
            Log.v("providers","gps");
        }
        List<String> providers = locationManager.getProviders(crit, true);
//        mock.shutdown();
        //[TESTING] !!!!!!!!!!!!!!!!!!!!!!!!!!!
        String provider = locationManager.getBestProvider(crit, true);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_ACCESS);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.v("missing", "yes it is");
            return null;
        }
        Location loc = locationManager.getLastKnownLocation(provider);
        if(loc == null){
            for(int i = 0; i < providers.size(); i++){  //takes only the first that works, could search for better
                loc = locationManager.getLastKnownLocation(providers.get(i));
                if(loc != null){
                    Log.v("works", "yay");
                    break;
                }
            }
        }
        if(loc == null){
            return null;
        }
        double lati = loc.getLatitude();
        Log.v("latitude", "lati:" + lati);
        lon = loc.getLongitude();
        lat = loc.getLatitude();
        return new LatLng(lat, lon);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch(requestCode) {//TODO: actually handle both cases
            case MY_LOCATION_ACCESS:
                Log.d("location", "lupa saatu");
            default:
                Log.d("location", "lupa evätty");

        }

    }

}
