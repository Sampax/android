package com.shoutvite.shoutvite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonatan on 1.8.2016.
 */
public class ChatAdapter<String> extends ArrayAdapter<String> {

    private ArrayList<String> data;
    private LayoutInflater inflater;

    private final int OWN_MESSAGE = 0;
    private final int MESSAGE_FROM_ANOTHER = 1;


    public ChatAdapter(Context context, int resource, int wtf, ArrayList<String> listToShow) {
        super(context, resource, wtf, listToShow);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = listToShow;
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public int getItemViewType(int position){
 /*       if(position % 2 == 0){
            return 0;
        }else{
            return 1;       //TODO: REMOVE THIS!!!
        }  */
        //TODO: UNCOMMENT  THIS!!!
        String str = data.get(position);
        Log.v("message is", str.toString());
        Log.v("letter is", "" + str.toString().charAt(str.toString().length() - 1));
        if(str.toString().charAt(str.toString().length() - 1) == 'u'){

            return OWN_MESSAGE;
        }else if(str.toString().charAt(str.toString().length() - 1) == 'n') {
            return MESSAGE_FROM_ANOTHER;
        }
        Log.v("HolyShitWTFException", "WTF");
        return 0;
    }

    public void addItem(String string){
        data.add(string);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.chat_shout, null);
            if(this.getItemViewType(i) == MESSAGE_FROM_ANOTHER) {
                holder.text = (TextView) view.findViewById(R.id.chat_text);
                holder.text.setBackgroundResource(R.drawable.round_shape_listview);
            }else if (this.getItemViewType(i) == OWN_MESSAGE){
                holder.text = (TextView) view.findViewById(R.id.chat_text_own);
                holder.text.setBackgroundResource(R.drawable.round_shape_chat_listview_own);
            }
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        Log.v("toShow", data.get(i).toString());
        java.lang.String stringToShow = data.get(i).toString().substring(0, data.get(i).toString().length() - 1);
        holder.text.setText((CharSequence) stringToShow);
        return view;
    }

    public static class ViewHolder{
        public TextView text;
    }
}
