package com.shoutvite.shoutvite;

/**
 * Created by Jonatan on 18.7.2016.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
                User user = new User(username + "@jormail.com", username, null, "salasana");
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
}