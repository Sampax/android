package com.shoutvite.shoutvite;

import android.content.Context;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
Issues:
- Faye server only connects at startup: should probably try to reconnect on disconnect
- Faye can't handle expired auth tokens -->  messages don't go through and no notification
- pictures don't scale properly on multiple devices with different resolutions
- wrong pictures in several places
- asks for permission multiple times
- doesn't handle negative permission result correctly



 */


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
    private final String FILENAME = "user_profile";

    List<String> shouts;
    public ArrayAdapter<String> shoutAdapter;
    public ArrayAdapter<String> joinedShoutAdapter;

    List<Shout> shoutsAsShouts;

    ImageView backArrow;
    boolean loaded = false;

    boolean possibleConnectionFailureFlag = false;
  //  List<Shout> joinedShouts;
  //  List<String> joinedShoutAsString;

    public ChatAdapter<String> chatAdapter;
    public ArrayList<String> chatMessages;


    public FayeConnector fayeConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        fayeConnector = new FayeConnector();
        user.main = this;
        backArrow = (ImageView)findViewById(R.id.back_arrow);
        if(loadUser(user)){
            Log.v("load", "success");
            fayeConnector.init(this);
        }else{
            Log.v("load", "failure");
        }
        // set different sized tabs: tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 35; OR
        // tabHost.getTabWidget().getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(width,height));
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        View logoView = LayoutInflater.from(this).inflate(R.layout.logo_view, null);
        tabHost.addTab(tabHost.newTabSpec("map frag").setIndicator("Map"), CustomMapFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("shout frag").setIndicator("Shouts"), ShoutListFragment.class, null);
//  this instead to get the logo into tab       tabHost.addTab(tabHost.newTabSpec("shout frag").setIndicator(logoView), ShoutListFragment.class, null);
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

        chatAdapter = new ChatAdapter<String>(this, R.layout.chat_layout, R.id.chat_text, chatMessages);

        //   mapFrag = (CustomMapFragment) getSupportFragmentManager().findFragmentBy;
        //   mapFrag.updateShoutsOnMap(null);
        //These two lines would activate gathering user data (for example demographics
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);    //depricated
    }


    public void testShoutButton(String shoutContent) {
        Log.v("postaaaattuuuu", shoutContent);
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
        fayeConnector.init(this);
    }

    //changes tab to map tab and focuses on a certain tab
    public void changeTab(Shout focusedShout){
        tabHost.setCurrentTab(MAPTABINDEX);
        mapFrag.zoomToShout(focusedShout);

    }


    //changes to profile tab and launce login dialog
    public void changeTabToProfileAndLaunchLogin(){
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

    public void createFile(User userToSave, List<String> channels){
        FileOutputStream fos = null;
        if(channels == null){
            channels = new ArrayList<String>();
        }
        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            JSONObject userJSON = new JSONObject();
            userJSON.put("username", userToSave.getNick());
            userJSON.put("email", userToSave.getEmail());
            userJSON.put("auth_token", userToSave.getAuthToken());
            JSONArray channelsJSON = new JSONArray(channels);
            userJSON.put("channels", channelsJSON);

            Log.v("userJSONnnnn: ", userJSON.toString());
            fos.write(userJSON.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public String readUserFile(){
        FileInputStream fis = null;
        try {
            String content = "";
            int character;
            fis = openFileInput(FILENAME);
            character = fis.read();
            while(character != -1){
                content = content + (char)character;
                character = fis.read();
            }
            Log.v("file", "content");
            return content;
        } catch (FileNotFoundException e) {
            Log.v("file", "not found");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.v("file", "io exception");
            e.printStackTrace();
            return null;
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //needs to modify since main.user is final
    public boolean loadUser(User newUser){
        String fileContent = readUserFile();
        if(fileContent != null){
            Log.v("file content", fileContent);
            try {
                JSONObject userJSON = new JSONObject(fileContent);
                newUser.setAuthToken(userJSON.getString("auth_token"));
                newUser.setNick(userJSON.getString("username"));
                newUser.setEmail(userJSON.getString("email"));
                JSONArray channelJSON = userJSON.getJSONArray("channels");
                for(int i = 0; i < channelJSON.length(); i++){
                    String channelString = (String)channelJSON.get(i);
                    Log.v("loaded channel: ",channelString );

                }
                return true;

            } catch (JSONException e) {
                Log.v("couldn not create", "user from JSON");
                Log.v("JSON is: ", fileContent);
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void launchNotification(int notificationCode){
        NotificationDialogFragment.notification = notificationCode;
        NotificationDialogFragment notificationDialog = new NotificationDialogFragment();
        notificationDialog.show(getFragmentManager(), "login_failed_tag");
        if(NotificationDialogFragment.LOGIN_EXPIRED == notificationCode){
            user.nullyfyUser();
            joinedShoutAdapter.notifyDataSetChanged();

        }
    }

}



