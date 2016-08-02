package com.shoutvite.shoutvite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by Jonatan on 1.8.2016.
 */
public class NotificationDialogFragment extends DialogFragment{

    public MainActivity main;
    public static final int LOGIN_FAILED = 0;
    public static final int LOGIN_EXPIRED = 1;
    public static final int UNKNOWN_LOCATION = 2;
    public static final int UNKNOWN_HOST_EXCEPTION = 3;
    public static final int PERMISSION_DENIED = 4;

    public static int notification = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        switch(notification){
            case LOGIN_FAILED:
                builder.setView(inflater.inflate(R.layout.login_failed_notification_layout, null));
                break;
            case LOGIN_EXPIRED:
                builder.setView(inflater.inflate(R.layout.login_expired_notification_layout, null));
                break;
            case UNKNOWN_LOCATION:
                builder.setView(inflater.inflate(R.layout.unknown_location_notification_layout, null));
                break;
            case UNKNOWN_HOST_EXCEPTION:
                builder.setView(inflater.inflate(R.layout.unknown_host_exception_notification_layout, null));
                break;
        }
        main = (MainActivity) getActivity();
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();

            }
        });

        return builder.create();

    }

}
