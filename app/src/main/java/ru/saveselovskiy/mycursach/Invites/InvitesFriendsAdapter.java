package ru.saveselovskiy.mycursach.Invites;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.saveselovskiy.mycursach.FriendList.ImageLoader;
import ru.saveselovskiy.mycursach.R;

/**
 * Created by Admin on 09.06.2015.
 */
public class InvitesFriendsAdapter extends BaseAdapter {

    private InviteActivity activity;
    private String[] data;
    public ArrayList<Integer> usersId;
    private static LayoutInflater inflater=null;
    public ArrayList<String> names;
    public ImageLoader imageLoader;

    public InvitesFriendsAdapter(InviteActivity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return names.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.invite_friend_cell, null);

        TextView text=(TextView)vi.findViewById(R.id.text1);
        ImageView image=(ImageView)vi.findViewById(R.id.imageView2);
        CheckBox checkBox = (CheckBox)vi.findViewById(R.id.checkBox_invite);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    activity.recieversId.add(usersId.get(position));
                }
                else{
                    activity.recieversId.remove(usersId.get(position));
                }
            }
        });
        text.setText(names.get(position));
        if (data == null){
            image.setImageResource(R.drawable.icon_user_default);
        }else {
            imageLoader.DisplayImage(data[position], image,R.drawable.icon_user_default);
        }
        return vi;
    }
}
