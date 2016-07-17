package com.shoutvite.shoutvite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by Lauri on 6/21/2016.
 */

public class ShoutDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.create_shout_layout, null));
        builder.setPositiveButton(getString(R.string.create_shout_string), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity main = (MainActivity)getActivity();
                EditText shoutText = (EditText)((AlertDialog) dialog).findViewById(R.id.newshout);
                double lat = main.getLat();
                double lon = main.getLon();
                Shout shout = new Shout(shoutText.getText().toString(), lat, lon);
                AsyncTaskPayload payload;
                payload = AsyncTaskPayload.createShoutPayload(shout);
                new RailsAPI(main).execute(payload);
                main.testShoutButton(shoutText.getText().toString());
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