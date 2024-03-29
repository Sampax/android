package com.shoutvite.shoutvite;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lauri on 6/20/2016.
 */
public class CustomMapFragment extends Fragment implements OnMapReadyCallback {

    public View view = null;
    public ViewGroup previousContainer = null;
    private final int MY_LOCATION_ACCESS = 0;
    Location lastLocation = null;
    GoogleMap map = null;
    int DISTANCE_THRESHOLD = 500;
    int ZOOM_LEVEL = 15;
    BitmapDescriptor bitmap_sos = null;
    BitmapDescriptor bitmap_sv = null;
    Bitmap bmap_sos = null;
    Bitmap bmap_sv = null;
    MainActivity main;
    Map<Marker, Shout> markersHashMap;
    Map<Shout, Marker> shoutsHashMap;
    boolean cameFromShoutList = false;
    int scale = 120;
    boolean permissionAnswer = false;
    boolean firstmove = true;
    float LOCATION_UPDATE_MIN_DIST = 25; //meters
    int LOCATION_UPDATE_MIN_TIME = 45000; //milliseconds    //for networkprovider minimum is 45000 hardcoded in android, if less still counts ad 45 seconds

    int SOS_SHOUT_ID = 12;


    @Override
    public void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        main = (MainActivity) getActivity();
        main.mapFrag = this;   //to get a handle for this fragment, holy shit the hardest thing ever
//        ((MainActivity)getActivity()).mapFrag.updateShoutsOnMap(null);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_ACCESS);
        }
        Log.v("Here", "mapppp");
        main.changeTab(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {
     //   while (!permissionAnswer) {
            //lol
     //   }

        //need to get rid of the old view from the parent before inflating a new one
        if (view != null && this.previousContainer == container) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.map_layout, container, false);
            previousContainer = container;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
        //bmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        bmap_sos = BitmapFactory.decodeResource(getResources(), R.drawable.sos);
        bmap_sv = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        DisplayMetrics display = new DisplayMetrics();
        main.getWindowManager().getDefaultDisplay().getMetrics(display);
        int screenWidth = display.widthPixels;
        int screenHeight = display.heightPixels;
        Log.v("windowsize h", "" + screenHeight);
        Log.v("windowsize w", "" + screenWidth);
        //windowsize h: 1794
        //windowsize w: 1080
        double heightProportion = (double) screenHeight / 1794;
        double widthProportion = (double) screenWidth / 1080;
        int width = (int) Math.round(scale * widthProportion);
        int height = (int) Math.round(scale * heightProportion);
        bmap_sos = Bitmap.createScaledBitmap(bmap_sos, width, height, false);        //[TODO]should scale depending on screen size
        bmap_sv = Bitmap.createScaledBitmap(bmap_sv, width, height, false);        //[TODO]should scale depending on screen size
  //      bitmap = BitmapDescriptorFactory.fromResource(R.drawable.logo);
        bitmap_sos = BitmapDescriptorFactory.fromBitmap(bmap_sos);
        bitmap_sv = BitmapDescriptorFactory.fromBitmap(bmap_sv);
        //custom adapter to access textView because android is a piece of shit software that should be exterminated
        FrameLayout frame1 = (FrameLayout) view.findViewById(R.id.frame1);
        FrameLayout frame2 = (FrameLayout) view.findViewById(R.id.frame2);
        if (frame1.getVisibility() == View.VISIBLE) {
            frame2.setVisibility(View.GONE);
        } else {
            frame2.setVisibility(View.VISIBLE);
        }
        ArrayList<String> single_shouts = new ArrayList<String>();
        single_shouts.add("testi...");
        single_shouts.add("joo");
        single_shouts.add("näyttäilknkns");
        single_shouts.add("toimivan");
        ArrayAdapter<String> convoAdapter = new ArrayAdapter<String>(getActivity(), R.layout.single_shoutl, R.id.single_shout_text, single_shouts);
        ListView shoutConvo = (ListView) frame2.findViewById(R.id.shout_convo_list);
        shoutConvo.setAdapter(convoAdapter);
        // FrameLayout testi = (FrameLayout)view.findViewById(R.id.frame1);
        // testi.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //    googleMap.addMarker(new MarkerOptions().position(new LatLng(61, 40)).title("own_location"));
        LatLng initLocation = getInitialLocation();
        CameraPosition pos = null;
        double FIN_LAT = 64.9241;
        double FIN_LON = 25.7482;
        int zoom = 15;
        int permission = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (initLocation == null) {
            initLocation = new LatLng(FIN_LAT, FIN_LON);
            zoom = 4;
        }
        //TODO: crashes if there's not yet permission to use location information, move elsewhere or check for permission
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_ACCESS);
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.v("missing", "yes it isssss");
            } else {  //if already have permissions
                googleMap.setMyLocationEnabled(true);   //adds the marker for user position
            }

        }
        pos = new CameraPosition(initLocation, zoom, 0, 0);
        if (!cameFromShoutList) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
            cameFromShoutList = false;
        }
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
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                //TODO: crashes when re-launching the app (at least after modifications, apparently pointer to activity is null
//                ((MainActivity)getActivity()).API = new MockAPI(location);
                //TODO: check if safe, also consider refactoring location elsewhere (myLocation class?):
                if (main == null) {
                    Log.v("fug", "nöy");
                }
                main.lastLocation = location;

                LatLng latlon = new LatLng(lat, lon);
                CameraPosition pos = new CameraPosition(latlon, ZOOM_LEVEL, 0, 0);
                if(firstmove) {
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
                }
                Log.v("location update", "updated location 2");
                AsyncTaskPayload payload = AsyncTaskPayload.getShoutsPayload(lat, lon, DISTANCE_THRESHOLD);
                new RailsAPI(main).execute(payload);

                //               APIConnector api = ((MainActivity) getActivity()).API;
                //  List<Location> locations =  api.getNearbyShouts(location, DISTANCE_THRESHOLD);
                //[TODO: add Markeroptions.archor() if necessary to center markers (check if markers are centered]
                //     Log.v("markers", "locations koko: "+ locations.size());
                //     for(int i = 0; i < locations.size(); i++){
                //         Location aux = locations.get(i);


                //          map.addMarker(new MarkerOptions().position(new LatLng(aux.getLatitude(), aux.getLongitude())).icon(bitmap)); //adds these eeeeevery time
                //         Log.v("markers", "added " + i);
                //    }
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
            //requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_ACCESS);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.v("missing", "yes it is");
            return null;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DIST, listener);     //  test:  String provider = locationManager.getProviders(true).get(0);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DIST, listener);     //  test:  String provider = locationManager.getProviders(true).get(0);
        //get all providers to find one that works... I'm sure this is very unefficient
        boolean print = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (print) {
            Log.v("providers", "network");
        }
        print = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (print) {
            Log.v("providers", "gps");
        }
        List<String> providers = locationManager.getProviders(crit, true);
//        mock.shutdown();
        //[TESTING] !!!!!!!!!!!!!!!!!!!!!!!!!!!
        String provider = locationManager.getBestProvider(crit, true);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_ACCESS);
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
        if (loc == null) {
            for (int i = 0; i < providers.size(); i++) {  //takes only the first that works, could search for better
                loc = locationManager.getLastKnownLocation(providers.get(i));
                if (loc != null) {
                    Log.v("works", "yaaaay");
                    break;
                }
            }
        }
        if (loc == null) {
            return null;
        }
        double lati = loc.getLatitude();
        Log.v("latitude", "lati:" + lati);
        lon = loc.getLongitude();
        lat = loc.getLatitude();
        return new LatLng(lat, lon);
    }

    public void updateShoutsOnMap(List<Shout> shoutList) {
        Log.v("jeeeeeeeeeeeeeeeeee", "toimii");
        main.shouts.clear();
        map.clear();
        markersHashMap = new HashMap<Marker, Shout>();
        shoutsHashMap = new HashMap<Shout, Marker>();
        main.shoutsAsShouts = shoutList;
        Shout sos_Shout = User.getShoutByID(shoutList, SOS_SHOUT_ID);
        if(sos_Shout != null){
            shoutList.remove(sos_Shout);
            shoutList.add(sos_Shout); //should add it as the last one

        }
        for (int i = 0; i < shoutList.size(); i++) {
            Shout aux = shoutList.get(i);
            Log.v("Bug content all: ", aux.getContent());
            Log.v("Bug content all: ", "" + aux.getId());
            Log.v("Bug channel all: ", aux.getChannel());
            String clickText = "Click to join";
            if (User.containsShoutID(main.user.getJoinedShouts(), aux)) {
                clickText = "";
            }
            Marker newMarker = null;
            if(aux.getId() != SOS_SHOUT_ID) {
                newMarker = map.addMarker(new MarkerOptions().position(new LatLng(aux.getLat(), aux.getLon())).icon(bitmap_sv).title(aux.getContent()).snippet(clickText));
            }else{
                newMarker = map.addMarker(new MarkerOptions().position(new LatLng(aux.getLat(), aux.getLon())).icon(bitmap_sos).title(aux.getContent()).snippet(clickText));
            }
            markersHashMap.put(newMarker, aux);
            shoutsHashMap.put(aux, newMarker);
            main.shouts.add(aux.getContent());
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if (main.user.isNullified()) {
                        main.changeTabToProfileAndLaunchLogin();
                    } else {
                        marker.setSnippet("");
                        Shout shout = markersHashMap.get(marker);
                        if (!User.containsShoutID(main.user.getJoinedShouts(), shout)) {
                            main.user.addJoinedShout(shout);
                            Log.v("Bug content: ", shout.getContent());
                            Log.v("Bug channel: ", shout.getChannel());
                            main.shoutFrag.setCurrentShout(shout, true);
                            main.fayeConnector.subscribeToChannel(shout.getChannel());
                            main.shoutFrag.currentShout = shout;
                            main.joinedShoutAdapter.notifyDataSetChanged();
                            main.changeTabToJoinedShout(shout);
                        } else {
                            main.shoutFrag.currentShout = shout;
                            main.changeTabToJoinedShout(shout);
                        }
                        Log.v("info window", "clickeeed " + shout.getContent());
                    }
                }
            });
        }
        main.shoutAdapter.notifyDataSetChanged();

        //               APIConnector api = ((MainActivity) getActivity()).API;
        //  List<Location> locations =  api.getNearbyShouts(location, DISTANCE_THRESHOLD);
        //[TODO: add Markeroptions.archor() if necessary to center markers (check if markers are centered]
        //     Log.v("markers", "locations koko: "+ locations.size());
        //     for(int i = 0; i < locations.size(); i++){
        //         Location aux = locations.get(i);


        //          map.addMarker(new MarkerOptions().position(new LatLng(aux.getLatitude(), aux.getLongitude())).icon(bitmap)); //adds these eeeeevery time
        //         Log.v("markers", "added " + i);
        //    }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.v("permission", "here");
        switch (requestCode) {//TODO: actually handle both cases
            case MY_LOCATION_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //permission granted
                    Log.d("location", "lupa saatu");
                    if (map != null) {
                        try {
                            map.setMyLocationEnabled(true);
                        } catch (SecurityException e) {
                            Log.v("permission error", "no permission yet"); //should never occur
                        }
                    }
//                    LatLng initLocation = getInitialLocation();
//                    CameraPosition pos = new CameraPosition(initLocation, ZOOM_LEVEL, 0, 0);
//                    map.moveCamera(CameraUpdateFactory.newCameraPosition(pos));

                } else {
                    main.launchNotification(NotificationDialogFragment.PERMISSION_DENIED);
                }
                permissionAnswer = true;
                break;
            default:
                Log.d("loc wtfException", "different permission");
                main.launchNotification(NotificationDialogFragment.PERMISSION_DENIED);

        }

    }

    public void zoomToShout(Shout shout) {
        Marker mapMarker = shoutsHashMap.get(shout);
        LatLng position = mapMarker.getPosition();
        Log.v("marker position", "" + position.latitude);
        cameFromShoutList = true;
        CameraPosition pos = new CameraPosition(position, ZOOM_LEVEL, 0, 0);
        mapMarker.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }


    public void addNewShout(Shout shout) {
        main.user.addJoinedShout(shout);
        main.fayeConnector.subscribeToChannel(shout.getChannel());
        main.joinedShoutAdapter.notifyDataSetChanged();
        main.shouts.add(shout.getContent());
        main.shoutsAsShouts.add(shout);
        main.shoutFrag.currentShout = shout;
        main.shoutAdapter.notifyDataSetChanged();
        Marker newMarker = map.addMarker(new MarkerOptions().position(new LatLng(shout.getLat(), shout.getLon())).icon(bitmap_sv).title(shout.getContent()).snippet(""));
        shoutsHashMap.put(shout, newMarker);
        markersHashMap.put(newMarker, shout);

    }
}
