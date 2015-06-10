package ru.saveselovskiy.mycursach.Invites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.saveselovskiy.mycursach.Events.DetailEventActivity;
import ru.saveselovskiy.mycursach.FriendList.ImageLoader;
import ru.saveselovskiy.mycursach.Model.Event;
import ru.saveselovskiy.mycursach.Model.Invite;
import ru.saveselovskiy.mycursach.R;

/**
 * Created by Admin on 11.06.2015.
 */
public class InviteListAdapter extends BaseAdapter {
    private static String API_URL = "http://54.200.192.248:44480";
    private Invite[] invites;
    Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public InviteListAdapter(Activity a, Invite[] array){
        activity = a;
        invites = array;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
    @Override
    public int getCount() {
        return invites.length;
    }

    @Override
    public Object getItem(int position) {
        return invites[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.events_list_cell, null);
        TextView name = (TextView)vi.findViewById(R.id.event_cell_title);
        TextView description = (TextView)vi.findViewById(R.id.event_cell_subtitle);
        ImageView icon = (ImageView)vi.findViewById(R.id.event_icon);
        name.setText(invites[position].event.name);
        String desc = invites[position].senderName + " пригласил вас";
        description.setText(desc);
        if (invites[position].event.hasPhoto){
            imageLoader.DisplayImage(urlForIcon(invites[position].event.id), icon, placeholderImage(invites[position].event.typeId));
        }
        else{
            icon.setImageResource(placeholderImage(invites[position].event.typeId));
        }
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,DetailEventActivity.class);
                intent.putExtra(Event.class.getCanonicalName(), invites[position].event);
                activity.startActivity(intent);
            }
        });
        return vi;
    }

    private String urlForIcon(int eventId){
        return API_URL+"/photo/"+eventId;
    }
    private int placeholderImage(int typeId){
        switch (typeId){
            case 1:{
                return R.drawable.icon_cinema;
            }
            case 2:{
                return R.drawable.icon_theater;
            }
            case 3:{
                return R.drawable.icon_museum;
            }
            case 4:{
                return R.drawable.icon_concert;
            }
        }
        return R.drawable.icon_event_undefined;
    }
}
