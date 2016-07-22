package com.shoutvite.shoutvite;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Lauri on 6/21/2016.
 */
public class ShoutListFragment extends Fragment {
    public MainActivity main;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.dialog_launch_layout, container, false);
        Button launchButton = (Button) view.findViewById(R.id.launch_button);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v.findViewById(R.id.launch_button);
                button.setText("jee");
                ShoutDialogFragment newShoutDialog = new ShoutDialogFragment();
                newShoutDialog.show(getActivity().getSupportFragmentManager(), "tag_to_find_this_fragment");

            }
        });
        ListView listView = (ListView)view.findViewById(R.id.map_shout_list);
        listView.setAdapter(main.shoutAdapter);


        FrameLayout generalFrame = (FrameLayout) view.findViewById(R.id.generalFrame);
        FrameLayout shoutFrame = (FrameLayout) view.findViewById(R.id.shoutFrame);
        if(generalFrame.getVisibility() == View.VISIBLE) {
            shoutFrame.setVisibility(View.GONE);
        }else{
            shoutFrame.setVisibility(View.VISIBLE);
        }
        return view;
    }

}
