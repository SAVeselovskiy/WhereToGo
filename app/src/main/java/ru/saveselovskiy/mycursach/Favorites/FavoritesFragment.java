package ru.saveselovskiy.mycursach.Favorites;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.saveselovskiy.mycursach.Events.EventsListAdapter;
import ru.saveselovskiy.mycursach.Model.EventsList;
import ru.saveselovskiy.mycursach.R;
import ru.saveselovskiy.mycursach.ServerWorker.ServerAdapter;
import ru.saveselovskiy.mycursach.ServerWorker.ServerWorker;

/**
 * Created by Admin on 10.06.2015.
 */
public class FavoritesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.favorites_layout, container,
                false);
        final ListView eventList = (ListView) rootView.findViewById(R.id.favorites_list);
        final ProgressBar eventsProgress = (ProgressBar) rootView.findViewById(R.id.favorites_list_progress);
        final TextView backgroundView = (TextView) rootView.findViewById(R.id.event_background);
        backgroundView.setVisibility(View.INVISIBLE);

        RestAdapter restAdapter = ServerAdapter.getAdapter();
        ServerWorker serverWorker = restAdapter.create(ServerWorker.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Favorites", getActivity().MODE_PRIVATE);
        String favorites = sharedPreferences.getString("events", null);
        if (favorites == null || favorites == ""){
            backgroundView.setText("Список Пуст");
            eventsProgress.setVisibility(View.INVISIBLE);
            backgroundView.setVisibility(View.VISIBLE);
            return rootView;
        }
        serverWorker.getFavorites(favorites, new Callback<EventsList>() {
            @Override
            public void success(EventsList eventsList, Response response) {
                eventsProgress.setVisibility(View.INVISIBLE);
                EventsListAdapter adapter = new EventsListAdapter(getActivity(),eventsList.events);
                eventList.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(error.getLocalizedMessage())
                        .setMessage(error.getMessage())
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return rootView;
    }
}
