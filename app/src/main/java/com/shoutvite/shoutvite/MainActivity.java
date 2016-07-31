package com.shoutvite.shoutvite;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    RailsAPI API;
    Location lastLocation;
  //  CallbackManager callbackManager;    //should not be here but for now, convenience, also required for facebook
    FragmentTabHost tabHost;
    int lastTab = 0;
    int currentTab = 0;
    CustomMapFragment mapFrag;
    int MAPTABINDEX = 0;
    int LISTTABINDEX = 1;
    int PROFILETABINDEX = 2;

    ProfileTabFragment profileFrag;
    ShoutListFragment shoutFrag;

    ArrayList<Integer> tabQueue = new ArrayList<Integer>();
    final User user = new User(null, null, null);      //only modify object, don't replace

    List<String> shouts;
    public ArrayAdapter<String> shoutAdapter;
    public ArrayAdapter<String> joinedShoutAdapter;

    List<Shout> shoutsAsShouts;

    ImageView backArrow;
    boolean loaded = false;

  //  List<Shout> joinedShouts;
  //  List<String> joinedShoutAsString;

    public ArrayAdapter<String> chatAdapter;
    public List<String> chatMessages;

    public FayeConnector fayeConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        fayeConnector = new FayeConnector();
        fayeConnector.init(this);
        backArrow = (ImageView)findViewById(R.id.back_arrow);

        // set different sized tabs: tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 35; OR
        // tabHost.getTabWidget().getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        View logoView = LayoutInflater.from(this).inflate(R.layout.logo_view, null);
        tabHost.addTab(tabHost.newTabSpec("map frag").setIndicator("Map"), CustomMapFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("shout frag").setIndicator(logoView), ShoutListFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("profile frag").setIndicator("Profile"), ProfileTabFragment.class, null);
        //[TODO: different sizes for different devices]:
        changeTab(0);
        //  ImageView logoView = (ImageView) findViewById(R.id.placeholder_logo);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                lastTab = currentTab;
                currentTab = tabHost.getCurrentTab();
                tabQueue.add(lastTab);

                backArrow.setVisibility(View.GONE);
                if(loaded) {
                    shoutFrag.generalFrame.setVisibility(View.VISIBLE);
                    shoutFrag.shoutFrame.setVisibility(View.GONE);
                }

            }
        });
        shouts = new ArrayList<String>();
        shoutAdapter = new ArrayAdapter<String>(this, R.layout.shout, R.id.shout_text, shouts){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                //textView.setBackground(new BitmapDrawable(getResources(), bmap));
                return textView;
            }
        };
        //joinedShouts = new ArrayList<Shout>();
        //joinedShoutAsString = new ArrayList<String>();
        joinedShoutAdapter = new ArrayAdapter<String>(this, R.layout.shout, R.id.shout_text, user.getJoinedShoutsAsStrings()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                //textView.setBackground(new BitmapDrawable(getResources(), bmap));
                return textView;
            }
        };

        chatMessages = new ArrayList<String>();

        chatAdapter = new ArrayAdapter<String>(this, R.layout.shout, R.id.shout_text, chatMessages);

        //   mapFrag = (CustomMapFragment) getSupportFragmentManager().findFragmentBy;
        //   mapFrag.updateShoutsOnMap(null);
        //These two lines would activate gathering user data (for example demographics
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);    //depricated
    }


    public void testShoutButton(String shoutContent) {
        Log.v("postaaaattuuu", shoutContent);
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
        if(newUser.getNick() != null){
            user.setNick(newUser.getNick());
        }
        if(newUser.getAuthToken() != null){
            user.setAuthToken(newUser.getAuthToken());
        }
        if(newUser.getEmail() != null){
            user.setEmail(newUser.getEmail());
        }
        if(newUser.getPassword() != null){          //not needed?
            user.setPassword(newUser.getPassword());
        }
        profileFrag.hasUserUpdateUI(true);
        Log.v("uusi käyttäjääää:", user.getAuthToken());
    }

    //changes tab to map tab and focuses on a certain tab
    public void changeTab(Shout focusedShout){
        tabHost.setCurrentTab(MAPTABINDEX);
        mapFrag.zoomToShout(focusedShout);

    }


    //changes to profile tab and launce login dialog
    public void changeTab(){
        tabHost.setCurrentTab(PROFILETABINDEX);
        profileFrag.launchLoginDialog();

    }

    public void backToList(View view){
        shoutFrag.generalFrame.setVisibility(View.VISIBLE);
        shoutFrag.shoutFrame.setVisibility(View.GONE);
        backArrow.setVisibility(View.GONE);
    }

    public void changeTab(int tab){
        tabHost.setCurrentTab(tab);
    }

    public void changeTabToJoinedShout(Shout shout){
        tabHost.setCurrentTab(LISTTABINDEX);
        shoutFrag.openShout(shout);
        Log.v("tab changed", "yes it is");

    }


}



