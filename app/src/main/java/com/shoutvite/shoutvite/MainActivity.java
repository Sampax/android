package com.shoutvite.shoutvite;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
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
    APIConnector API;
    Location lastLocation;
    CallbackManager callbackManager;    //should not be here but for now, convenience
    FragmentTabHost tabHost;
    int lastTab = 0;
    int currentTab = 0;
    ArrayList<Integer> tabQueue = new ArrayList<Integer>();

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
        tabHost.addTab(tabHost.newTabSpec("tab 2").setIndicator("toka"), CustomMapFragment.class, null);
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




        //These two lines would activate gathering user data (for example demographics
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);    //depricated
    }

    public void crash(View view){
        //ShoutDialogFragment.
    }

    public void shoutClicked(View view){

        Log.v("clickattu", "jee");
        ArrayList<String> single_shouts = new ArrayList<String>();
        single_shouts.add("testi...");
        single_shouts.add("joo");
        single_shouts.add("näyttäis");
        single_shouts.add("toimivan");
        ArrayAdapter<String> convoAdapter = new ArrayAdapter<String>(this, R.layout.single_shoutl, R.id.single_shout_text);
        View rootView = view.getRootView();
        ((FrameLayout)rootView.findViewById(R.id.frame1)).setVisibility(View.GONE);
        ((FrameLayout)rootView.findViewById(R.id.frame2)).setVisibility(View.VISIBLE);
        //view.setVisibility(View.INVISIBLE);

    }

    //adds shout to db
    public void postShout(String shoutContent) {
        Log.v("postattu", shoutContent);
    }

    @Override
    public void onBackPressed(){
        if(tabQueue.size() > 0) {
            tabHost.setCurrentTab(tabQueue.get(tabQueue.size()-1));
            tabQueue.remove(tabQueue.size()-1);
            if(tabQueue.size() > 0) {       //just in case
                tabQueue.remove(tabQueue.size() - 1);      //second time because onTabChanged launches also on onBackPressed
            }
        }else{
            this.finish();
        }
    }
}
