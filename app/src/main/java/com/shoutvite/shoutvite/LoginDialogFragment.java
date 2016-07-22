package com.shoutvite.shoutvite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by Jonatan on 23.7.2016.
 */
public class LoginDialogFragment extends DialogFragment {
    MainActivity main;

    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.login_layout, null));
        main = (MainActivity) getActivity();
        builder.setPositiveButton(getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity main = (MainActivity) getActivity();
                EditText userText = (EditText) ((AlertDialog) dialog).findViewById(R.id.login_email);
                String username = userText.getText().toString();
                userText = (EditText) ((AlertDialog) dialog).findViewById(R.id.login_password);
                String password = userText.getText().toString();
//TODO: login needs to return username
                User user = new User(username + "@jormail.com", null, null, password);
                AsyncTaskPayload payload = AsyncTaskPayload.createLoginPayload(user);

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