package ru.saveselovskiy.mycursach.FriendList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.saveselovskiy.mycursach.R;

/**
 * Created by Admin on 20.04.2015.
 */
public class CustomListAdapter  extends ArrayAdapter<String>{
    public String[] avatars;
    private final Activity context;
    private final ArrayList<String> names;
    private final Integer[] imgIds;

    public CustomListAdapter(Activity context, int resource, ArrayList<String> names, Integer[] imgIds) {
        super(context, R.layout.friend_list_item, names);
        this.context = context;
        this.names = names;
        this.imgIds = imgIds;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.friend_list_item, null,true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);
        TextView textView = (TextView) rowView.findViewById(R.id.text1);


        textView.setText(names.get(position).toCharArray(), 0, names.get(position).length());

        return rowView;
    }
}
