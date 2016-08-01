package com.shoutvite.shoutvite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class ProfileTabFragment extends Fragment {
    MainActivity main;
    //public CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
        main = (MainActivity)getActivity();
        main.profileFrag = this;
        Log.v("Here", "profile");
        main.changeTab(0); //need to make full circle at the start

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    /*    ((MainActivity)getActivity()).callbackManager.onActivityResult(requestCode, resultCode, data); */ //required for facebook
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup content, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.profile_layout, content, false);
/*        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
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



    <com.facebook.login.widget.LoginButton
        android:id="@+id/login_button"
        android:layout_width="wrap_content"
        android:layout_height="0dp" />


        */
        Button createUserButton = (Button) view.findViewById(R.id.username_launch_button);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Button button = (Button) v.findViewById(R.id.launch_button);
                //button.setText("jee");
                Log.v("paina", "nappulaa");
                CreateUserDialogFragment newUserDialog = new CreateUserDialogFragment();
                newUserDialog.show(getActivity().getSupportFragmentManager(), "username dialog");

            }
        });
        Button customLoginButton = (Button) view.findViewById(R.id.custom_login_button);
        customLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Button button = (Button) v.findViewById(R.id.launch_button);
                //button.setText("jee");
                LoginDialogFragment loginDialog = new LoginDialogFragment();
                loginDialog.show(getActivity().getSupportFragmentManager(), "login dialog");

            }
        });
        Button customLogoutButton = (Button) view.findViewById(R.id.custom_logout_button);
        customLogoutButton.setVisibility(View.GONE);
        customLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("clickattu", "log out");
                main.user.nullyfyUser(); //same as creating a new user with null values (still creates new joinedshoutlists)
                hasUserUpdateUI(false);
            }
        });

        if(!main.user.isNullified()) {
            TextView textView = (TextView) view.findViewById(R.id.teksti);
            textView.setText(main.user.getNick() + "\n" + main.user.getEmail());
            createUserButton.setVisibility(View.GONE);
            customLoginButton.setVisibility(View.GONE);
            customLogoutButton.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public void hasUserUpdateUI(boolean loggedIn){
        TextView userText = (TextView)getView().findViewById(R.id.teksti);
        String username = "";
        if(loggedIn) {
            if (main.user.getNick() != null) {
                username = main.user.getNick();
            }
            userText.setText(username + "\n" + main.user.getEmail());
            ((Button) getView().findViewById(R.id.username_launch_button)).setVisibility(View.GONE);
            ((Button) getView().findViewById(R.id.custom_logout_button)).setVisibility(View.VISIBLE);
            ((Button) getView().findViewById(R.id.custom_login_button)).setVisibility(View.GONE);
        }else{
            ((Button) getView().findViewById(R.id.username_launch_button)).setVisibility(View.VISIBLE);
            ((Button) getView().findViewById(R.id.custom_logout_button)).setVisibility(View.GONE);
            ((Button) getView().findViewById(R.id.custom_login_button)).setVisibility(View.VISIBLE);
            userText.setText("");
            main.joinedShoutAdapter.notifyDataSetChanged();
            Log.v("nullified", "joined shouts updated");

        }
        ;
    }


    public void launchLoginDialog(){
        LoginDialogFragment loginDialog = new LoginDialogFragment();
        loginDialog.show(getActivity().getSupportFragmentManager(), "login dialog");

    }
  /*  public void setUser(User newUser){
            TextView textView = (TextView) view.findViewById(R.id.teksti);
            textView.setText(main.user.getNick() + "\n" + main.user.getEmail());

    }
    */
}
