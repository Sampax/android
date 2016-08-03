package com.shoutvite.shoutvite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Lauri on 6/21/2016.
 */
public class ShoutListFragment extends Fragment {
    public MainActivity main;
    public FrameLayout generalFrame;
    public FrameLayout shoutFrame;
    public boolean swapVisibility;
    public Shout currentShout = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity();
        main.shoutFrag = this;
        Log.v("Hereeee", "shout list");
        main.changeTab(2);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.shout_list_layout, container, false);
        Button launchButton = (Button) view.findViewById(R.id.launch_button);

        ListView chatMessageView = (ListView) view.findViewById(R.id.chat_list);
        chatMessageView.setAdapter(main.chatAdapter);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Button button = (Button) v.findViewById(R.id.launch_button);
              //  button.setText("jeeeeeee");
                if(!main.user.isNullified()) {
                    if(main.lastLocation != null) {
                        ShoutDialogFragment newShoutDialog = new ShoutDialogFragment();
                        newShoutDialog.show(getActivity().getSupportFragmentManager(), "tag_to_find_this_fragment");
                    }else{
                        main.launchNotification(NotificationDialogFragment.UNKNOWN_LOCATION);
                    }
                }else{
                    main.changeTabToProfileAndLaunchLogin();
                    //LoginDialogFragment loginDialog = new LoginDialogFragment();
                    //loginDialog.show(getActivity().getSupportFragmentManager(), "login dialog");
                }
            }
        });
        final ListView listView = (ListView)view.findViewById(R.id.map_shout_list);
        listView.setAdapter(main.shoutAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int position, long arg4){
                Shout shout = main.shoutsAsShouts.get(position);
                if(User.containsShoutID(main.user.getJoinedShouts(), shout)){

                    setCurrentShout(shout, false);
                    showGeneralView(false);
                }else{
                    if(main.user.isNullified()){
                        main.changeTab(shout);
                    }else {
                        main.user.addJoinedShout(shout);
                        setCurrentShout(shout, true);
                        //main.fayeConnector.subscribeToChannel(shout.getChannel());
                        main.joinedShoutAdapter.notifyDataSetChanged();
                        showGeneralView(false);
//                    main.changeTabToJoinedShout(shout);
                    }

                    Log.v("shouttt", shout.getContent() + " " + shout.getId());
//this is what it was                    main.changeTab(shout);
                }
                //currentShout = shout;
            }
        });
        final ListView joinedListView = (ListView)view.findViewById(R.id.user_shout_list);
        joinedListView.setAdapter(main.joinedShoutAdapter);
        generalFrame = (FrameLayout) view.findViewById(R.id.generalFrame);
        shoutFrame = (FrameLayout) view.findViewById(R.id.shoutFrame);
        main.loaded = true;
        Button chatMessageSendButton = (Button) view.findViewById(R.id.chat_message_send_button);
        chatMessageSendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {        //should set a new one each time currentShout changes
                if(!main.user.isNullified()) {
                    view = (View)view.getParent().getParent();
                    EditText composedMessage = (EditText)view.findViewById(R.id.chat_message_send);
                    if(composedMessage == null){
                        Log.v("Edit text", "on null, fug");
                    }
                    String username = "";
                    if(main.user.getNick() != null){
                        username = main.user.getNick();
                    }
                    Log.v("shout channel", currentShout.getChannel());
                    // main.fayeConnector.subscribeToChannel("/testi");
                    main.fayeConnector.publishToChannel(currentShout.getChannel(), composedMessage.getText().toString(), username);
                    composedMessage.setText("");
                    main.hideKeyboard(main);
                }
            }
        });

//        ImageView backArrowPic = (ImageView) ((View)view.getParent()).findViewById(R.id.back_arrow);

        if(swapVisibility){
            if(shoutFrame.getVisibility() == View.VISIBLE){
                shoutFrame.setVisibility(View.GONE);
                generalFrame.setVisibility(View.VISIBLE);
                main.backArrow.setVisibility(View.GONE);
                swapVisibility = false;

            }else{
                shoutFrame.setVisibility(View.VISIBLE);
                generalFrame.setVisibility(View.GONE);
                main.backArrow.setVisibility(View.VISIBLE);
                swapVisibility = false;
            }
        }
  //      final View chatView = view;

        joinedListView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int position, long arg4){
                Shout shout = main.user.getJoinedShouts().get(position);
                String channel = shout.getChannel();
                setCurrentShout(shout, false);
                //TODO: need to get show previous messages if any

                //channel = "/fug";
                //main.fayeConnector.subscribeToChannel(channel);
               // Log.v("shout", shout.getContent() + " " + shout.getId());
               // main.changeTab(shout);
                Log.v("joined shout", shout.getChannel());
                Log.v("joined shout", shout.getContent());
                if(generalFrame.getVisibility() == View.VISIBLE) {
                    shoutFrame.setVisibility(View.VISIBLE);
                    generalFrame.setVisibility(View.GONE);
                    main.backArrow.setVisibility(View.VISIBLE);
              //      String chosenShoutString = main.joinedShoutAsString.get(position);
  //                  ListView chatListView = (ListView)chatView.findViewById(R.id.chat_list);
   //                 chatListView.setAdapter(main.chatAdapter);
                }

            }
        });
        Log.v("should only come here", "at the startt");
        return view;
    }

    public void openShout(Shout shout){
//        shoutFrame.setVisibility(View.VISIBLE);
  //      generalFrame.setVisibility(View.GONE);
        swapVisibility = true;
        main.chatAdapter.notifyDataSetChanged();
        Log.v("hear", "hear");
    }

//IMPORTANT: My brilliant hack adds a letter, either u(ser) or n(ot the user) after the message printed. The last letter need to be shaved off before printing
    public void updateChatIfNecessary(String channel, String message, String user){
        Log.v("current channel: ", currentShout.getChannel());
        Log.v("message channel: ", channel);
        if(currentShout.getChannel().equals(channel)){
            Log.v("what", "the hell?");
            if(user.equals(main.user.getNick())) {
                main.chatMessages.add(user + " says: \n" + message + "u");
            }else{
                main.chatMessages.add(user + " says: \n" + message + "n");
            }
            //main.chatAdapter.notifyDataSetChanged();
            main.fayeConnector.doShit();
        }
    }

//IMPORTANT: My brilliant hack adds a letter, either u(ser) or n(ot the user) after the message printed. The last letter need to be shaved off before printing
    public void setCurrentShout(Shout shout, boolean justJoined){
        currentShout = shout;
        main.chatMessages.clear();
        if(!justJoined){
            Log.v("channel searched", shout.getChannel());
            if(main.user.getChannelnamesToChannels().containsKey(shout.getChannel())) {
                FayeChannel channel = main.user.getChannelnamesToChannels().get(shout.getChannel());
                for(int i = 0; i < channel.messages.size(); i++){
                    if(channel.messages.get(i).user.equals(main.user.getNick())) {
                        main.chatMessages.add(channel.messages.get(i).user + " says: \n" + channel.messages.get(i).message + "u");
                    }else{
                        main.chatMessages.add(channel.messages.get(i).user + " says: \n" + channel.messages.get(i).message + "n");
                    }
                }
            }else{
                Log.v("Shit", "Does not contain channel");
            }
        }
        main.chatAdapter.notifyDataSetChanged();

    }

    public void showGeneralView(boolean show){
        if(show){
            shoutFrame.setVisibility(View.GONE);
            generalFrame.setVisibility(View.VISIBLE);
            main.backArrow.setVisibility(View.GONE);

        }else{
            shoutFrame.setVisibility(View.VISIBLE);
            generalFrame.setVisibility(View.GONE);
            main.backArrow.setVisibility(View.VISIBLE);

        }
    }


}
