package ru.saveselovskiy.mycursach.Events;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.saveselovskiy.mycursach.Invites.InviteActivity;
import ru.saveselovskiy.mycursach.Model.Event;
import ru.saveselovskiy.mycursach.R;
import ru.saveselovskiy.mycursach.ServerWorker.ServerAdapter;
import ru.saveselovskiy.mycursach.ServerWorker.ServerWorker;

/**
 * Created by Admin on 08.06.2015.
 */
public class DetailEventActivity extends ActionBarActivity {
    Event event;
    int eventId;
    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_event_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        final Event event = getIntent().getParcelableExtra(Event.class.getCanonicalName());
        eventId = event.id;


        TextView name = (TextView) findViewById(R.id.detail_event_name);
        name.setText(event.name);
        TextView description = (TextView) findViewById(R.id.detail_event_description);
        description.setText(event.description);
        final ImageView icon = (ImageView) findViewById(R.id.detail_event_icon);
        icon.setImageResource(placeholderImage(event.typeId));

        RestAdapter restAdapter = ServerAdapter.getAdapter();
        ServerWorker serverWorker = restAdapter.create(ServerWorker.class);
        serverWorker.getEventPhoto(event.id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    BufferedInputStream isr = new BufferedInputStream(response.getBody().in());
                    Bitmap bitmap = BitmapFactory.decodeStream(isr);
                    icon.setImageBitmap(bitmap);
                }catch (IOException e){
                    Log.d("myLog", e.getLocalizedMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("myLog", error.getLocalizedMessage());
            }
        });
        Button invite = (Button) findViewById(R.id.invite_button);
        invite.setText(R.string.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailEventActivity.this, InviteActivity.class);
                intent.putExtra("eventId",event.id);
                startActivity(intent);
            }
        });


        final Button addFavorites = (Button) findViewById(R.id.add_favorites_button);
        addButton = addFavorites;
        if (isInFavorites(event.id)){
            addFavorites.setText(R.string.add_favorite_done);
            addFavorites.setEnabled(false);
        }else{
            addFavorites.setText(R.string.add_favorite);
        }
        addFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
                String favorites = sharedPreferences.getString("events",null);
                if (favorites == null || favorites == ""){
                    favorites = "" + event.id;
                }else{
                    favorites = favorites + "," + event.id;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("events",favorites);
                editor.commit();
                addFavorites.setText(R.string.add_favorite_done);
                addFavorites.setEnabled(false);
            }
        });
    }
    private boolean isInFavorites(int id){
        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        String favorites = sharedPreferences.getString("events",null);
        if (favorites == null || favorites == ""){
            return false;
        }
        String[] identifires = favorites.split(",");
        for (int i = 0; i<identifires.length; i++){
            if (id == Integer.parseInt(identifires[i])){
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_event, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.event_settings){
            if (!isInFavorites(eventId)){
                return false;
            }
            SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
            String favorites = sharedPreferences.getString("events",null);
            String result = "";
            String[] events = favorites.split(",");
            int j = 0;
            for (int i = 0; i < events.length; i++) {
                if (eventId != Integer.parseInt(events[i])){
                    if (j == 0){
                        result = result + events[i];
                    }else{
                        result = result + "," + events[i];
                    }
                    j++;
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("events", result);
            editor.commit();
            addButton.setEnabled(true);
            addButton.setText("Добавить +");
        }
        else {
            finish();
        }
        return true;

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
