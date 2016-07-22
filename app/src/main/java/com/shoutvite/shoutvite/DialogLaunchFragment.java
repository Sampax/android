package com.shoutvite.shoutvite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by Lauri on 6/21/2016.
 */
public class DialogLaunchFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Button createUserButton = (Button) view.findViewById(R.id.username_launch_button);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Button button = (Button) v.findViewById(R.id.launch_button);
                //button.setText("jee");
                CreateUserDialogFragment newUserDialog = new CreateUserDialogFragment();
                newUserDialog.show(getActivity().getSupportFragmentManager(), "username dialog");

            }
        });
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
