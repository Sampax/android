package com.shoutvite.shoutvite;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;


import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    RailsAPI API;
    Location lastLocation;
    CallbackManager callbackManager;    //should not be here but for now, convenience
    FragmentTabHost tabHost;
    int lastTab = 0;
    int currentTab = 0;
    CustomMapFragment mapFrag;
    ArrayList<Integer> tabQueue = new ArrayList<Integer>();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        // set different sized tabs: tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 35; OR
        // tabHost.getTabWidget().getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab 1").setIndicator("eka"), PlaceholderTabFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("map frag").setIndicator("toka"), CustomMapFragment.class, null);
        //[TODO: different sizes for different devices]:
        //  ImageView logoView = (ImageView) findViewById(R.id.placeholder_logo);
        View logoView = LayoutInflater.from(this).inflate(R.layout.logo_view, null);
        tabHost.addTab(tabHost.newTabSpec("tab 3").setIndicator(logoView), DialogLaunchFragment.class, null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                lastTab = currentTab;
                currentTab = tabHost.getCurrentTab();
                tabQueue.add(lastTab);

            }
        });

        //   mapFrag = (CustomMapFragment) getSupportFragmentManager().findFragmentBy;
        //   mapFrag.updateShoutsOnMap(null);
        //These two lines would activate gathering user data (for example demographics
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);    //depricated
    }

    public void shoutClicked(View view) {
        User user = new User("jorma2@jormail.com", "unf", null, "salasana");
        AsyncTaskPayload payload = AsyncTaskPayload.createUserPayload(user);
        new RailsAPI(this).execute(payload);

        Log.v("clickattu", "jee");
        View rootView = view.getRootView();
        ((FrameLayout) rootView.findViewById(R.id.frame1)).setVisibility(View.GONE);
        ((FrameLayout) rootView.findViewById(R.id.frame2)).setVisibility(View.VISIBLE);
    }

    public void testShoutButton(String shoutContent) {
        Log.v("postattuuu", shoutContent);
    }


    public double getLat() {
        return lastLocation.getLatitude();
    }

    public double getLon() {
        return lastLocation.getLongitude();
    }


    @Override
    public void onBackPressed() {
        //TODO: currently only handles fragments, not overlapping views
        if (tabQueue.size() > 0) {
            int tabIndex = tabQueue.get(tabQueue.size() - 1);
            if (tabIndex < 0) {
                //for overlapping views
            } else {
                tabHost.setCurrentTab(tabIndex);
            }
            tabQueue.remove(tabQueue.size() - 1);
            if (tabQueue.size() > 0) {       //just in case
                tabQueue.remove(tabQueue.size() - 1);      //second time because onTabChanged launches also on onBackPressed
            }
        } else {
            this.finish();
        }
    }

    public void setUser(User newUser) {
        user = newUser;
        Log.v("uusi käyttäjä:", user.getAuthToken());
    }
}



