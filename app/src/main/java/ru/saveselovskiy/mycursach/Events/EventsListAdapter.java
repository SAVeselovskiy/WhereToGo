package ru.saveselovskiy.mycursach.Events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ru.saveselovskiy.mycursach.FriendList.ImageLoader;
import ru.saveselovskiy.mycursach.Model.Event;
import ru.saveselovskiy.mycursach.R;

/**
 * Created by Admin on 07.06.2015.
 */
public class EventsListAdapter extends BaseAdapter {
    private static String API_URL = "http://54.200.192.248:44480";
    private Event[] events;
    Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public EventsListAdapter(Activity a, Event[] array){
        activity = a;
        events = array;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
    @Override
    public int getCount() {
        return events.length;
    }

    @Override
    public Object getItem(int position) {
        return events[position];
    }

    @Override
    public long getItemId(int position) {
        return events[position].id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.events_list_cell, null);
        TextView name = (TextView)vi.findViewById(R.id.event_cell_title);
        TextView description = (TextView)vi.findViewById(R.id.event_cell_subtitle);
        ImageView icon = (ImageView)vi.findViewById(R.id.event_icon);
        name.setText(events[position].name);
        description.setText(events[position].description);
        if (events[position].hasPhoto){
            imageLoader.DisplayImage(urlForIcon(events[position].id), icon, placeholderImage(events[position].typeId));
        }
        else{
            icon.setImageResource(placeholderImage(events[position].typeId));
        }
        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,DetailEventActivity.class);
                intent.putExtra(Event.class.getCanonicalName(),events[position]);
                activity.startActivity(intent);
            }
        });
        return vi;
    }

    private String urlForIcon(int eventId){
        return API_URL+"/events/"+eventId+"/photo";
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
