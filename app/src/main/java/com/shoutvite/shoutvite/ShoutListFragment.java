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
import android.widget.ListView;

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
                    ShoutDialogFragment newShoutDialog = new ShoutDialogFragment();
                    newShoutDialog.show(getActivity().getSupportFragmentManager(), "tag_to_find_this_fragment");
                }else{
                    main.changeTab();
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
                //currentShout = shout;
                Log.v("shouttt", shout.getContent() + " " + shout.getId());
                main.changeTab(shout);

            }
        });
        final ListView joinedListView = (ListView)view.findViewById(R.id.user_shout_list);
        joinedListView.setAdapter(main.joinedShoutAdapter);
        generalFrame = (FrameLayout) view.findViewById(R.id.generalFrame);
        shoutFrame = (FrameLayout) view.findViewById(R.id.shoutFrame);
        Button chatMessageSendButton = (Button) view.findViewById(R.id.chat_message_send_button);
        chatMessageSendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                view = (View)view.getParent();
                EditText composedMessage = (EditText)view.findViewById(R.id.chat_message_send);
                if(!main.user.isNullified()) {
                    String username = "";
                    if(main.user.getNick() != null){
                        username = main.user.getNick();
                    }
                    if(currentShout == null){
                        Log.v("currentShout", "is null");
                    }else if (currentShout.getChannel() == null){
                        Log.v("channel", "is null");
                    }
                    // main.fayeConnector.subscribeToChannel("/testi");
                    main.fayeConnector.publishToChannel(currentShout.getChannel(), composedMessage.getText().toString(), username);
                }
            }
        });
        if(swapVisibility){
            if(shoutFrame.getVisibility() == View.VISIBLE){
                shoutFrame.setVisibility(View.GONE);
                generalFrame.setVisibility(View.VISIBLE);

            }else{
                shoutFrame.setVisibility(View.VISIBLE);
                generalFrame.setVisibility(View.GONE);
            }
        }
  //      final View chatView = view;

        joinedListView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int position, long arg4){
                Shout shout = main.shoutsAsShouts.get(position);
                String channel = shout.getChannel();
                currentShout = shout;
                //channel = "/fug";
                //main.fayeConnector.subscribeToChannel(channel);
               // Log.v("shout", shout.getContent() + " " + shout.getId());
               // main.changeTab(shout);
                Log.v("joined shout", "clickeddd");
                if(generalFrame.getVisibility() == View.VISIBLE) {
                    shoutFrame.setVisibility(View.VISIBLE);
                    generalFrame.setVisibility(View.GONE);
              //      String chosenShoutString = main.joinedShoutAsString.get(position);
  //                  ListView chatListView = (ListView)chatView.findViewById(R.id.chat_list);
   //                 chatListView.setAdapter(main.chatAdapter);
                }

            }
        });
        Log.v("should only come here", "at the start");
        return view;
    }

    public void openShout(Shout shout){
//        shoutFrame.setVisibility(View.VISIBLE);
  //      generalFrame.setVisibility(View.GONE);
        swapVisibility = true;
        main.chatAdapter.notifyDataSetChanged();
        Log.v("hear", "hear");
    }


    public void updateChatIfNecessary(String channel, String message, String user){
        Log.v("current channel: ", currentShout.getChannel());
        Log.v("message channel: ", channel);
        if(currentShout.getChannel().equals(channel)){
            Log.v("what", "the hell?");
            main.chatMessages.add("what the hell?");
            main.chatMessages.add("what the hell?");
            main.chatMessages.add("what the hell?");
            main.chatMessages.add("what the hell?");
            main.chatMessages.add(user + "says: \n" + message);
            //main.chatAdapter.notifyDataSetChanged();
            main.fayeConnector.doShit();
        }
    }

}
