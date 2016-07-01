package com.shoutvite.shoutvite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Lauri on 6/29/2016.
 */
public class PlaceholderTabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedStateInstance){
        super.onCreate(savedStateInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup content, Bundle savedStateInstance){
        View view = inflater.inflate(R.layout.placeholder_content_layout, content, false);
        TextView textView = (TextView) view.findViewById(R.id.teksti);
        textView.setText(this.getTag() + " Content");
        return view;
    }
}
