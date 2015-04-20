package ru.saveselovskiy.mycursach.FriendList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.saveselovskiy.mycursach.R;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ArrayList<String> names;
    public ImageLoader imageLoader;
    
    public LazyAdapter(Activity a, String[] d) {
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
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.friend_list_item, null);

        TextView text=(TextView)vi.findViewById(R.id.text1);;
        ImageView image=(ImageView)vi.findViewById(R.id.imageView2);
        text.setText(names.get(position));
        if (data == null){
            image.setImageResource(R.drawable.icon_user_default);
        }else {
            imageLoader.DisplayImage(data[position], image);
        }
        return vi;
    }
}