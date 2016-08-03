package com.shoutvite.shoutvite;

/**
 * Created by Jonatan on 18.7.2016.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.widget.EditText;


public class CreateUserDialogFragment extends DialogFragment {
    MainActivity main;

    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.create_user_layout, null));
        main = (MainActivity)getActivity();
        builder.setPositiveButton(getString(R.string.create_user_string), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity main = (MainActivity)getActivity();
                EditText userText = (EditText)((AlertDialog) dialog).findViewById(R.id.newuser);
                String username = userText.getText().toString();
                userText = (EditText)((AlertDialog) dialog).findViewById(R.id.email);
                String email = userText.getText().toString();
                userText = (EditText)((AlertDialog) dialog).findViewById(R.id.password);
                String password = userText.getText().toString();

                User user = new User(email, username, null, password);
                AsyncTaskPayload payload = AsyncTaskPayload.createUserPayload(user);

                new RailsAPI(main).execute(payload);
            //    main.testShoutButton(userText.getText().toString());
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    //these used if login saved
    public void saveUser(User user){
        SharedPreferences settings = main.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", user.getNick());
        editor.putString("email", user.getEmail());
        editor.putString("auth", user.getAuthToken());
    }

    public User getSavedUser(){
        SharedPreferences settings = main.getPreferences(Context.MODE_PRIVATE);
        User user = new User(settings.getString("email", null), settings.getString("username", null), settings.getString("auth", null));
        if(user.getAuthToken() == null | user.getNick() == null){
            return null;
        }
        return user;
    }
}