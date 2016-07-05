package com.shoutvite.shoutvite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Lauri on 6/29/2016.
 */
public class PlaceholderTabFragment extends Fragment {

    //public CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((MainActivity)getActivity()).callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup content, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.placeholder_content_layout, content, false);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setFragment(this);
  //      FacebookSdk.sdkInitialize(getActivity());
        ((MainActivity)getActivity()).callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(((MainActivity)getActivity()).callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v("login", "successsss");
      //          Profile.getCurrentProfile().getLastName();
            }

            @Override
            public void onCancel() {
                Log.v("login", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.v("login", "error");
            }
        });

        TextView textView = (TextView) view.findViewById(R.id.teksti);
        textView.setText(this.getTag() + " Content");
        return view;
    }


}
