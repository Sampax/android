package com.shoutvite.shoutvite;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends FragmentActivity {
    APIConnector API;
    Location lastLocation;
    CallbackManager callbackManager;    //should not be here but for now, convenience

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        // set different sized tabs: tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 35; OR
        // tabHost.getTabWidget().getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab 1").setIndicator("eka"), PlaceholderTabFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab 2").setIndicator("toka"), CustomMapFragment.class, null);
        //[TODO: different sizes for different devices]:
      //  ImageView logoView = (ImageView) findViewById(R.id.placeholder_logo);
        View logoView = LayoutInflater.from(this).inflate(R.layout.logo_view, null);
        tabHost.addTab(tabHost.newTabSpec("tab 3").setIndicator(logoView), DialogLaunchFragment.class, null);




        //These two lines would activate gathering user data (for example demographics
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);    //depricated
    }

    public void crash(View view){
        //ShoutDialogFragment.
    }

    //adds shout to db
    public void postShout(String shoutContent) {
        Log.v("postattu", shoutContent);
    }

}
