package ru.saveselovskiy.mycursach.Events;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.saveselovskiy.mycursach.Login.Login;
import ru.saveselovskiy.mycursach.Model.EventsList;
import ru.saveselovskiy.mycursach.R;
import ru.saveselovskiy.mycursach.ServerWorker.ServerAdapter;
import ru.saveselovskiy.mycursach.ServerWorker.ServerWorker;

/**
 * Created by sergejveselovskij on 24.05.15.
 */
public class EventsFragment extends Fragment {
    public int typeId;
    public static final String TAG = EventsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.events_layout, container,
                false);
        final ListView eventList = (ListView) rootView.findViewById(R.id.events_list);
        final ProgressBar eventsProgress = (ProgressBar) rootView.findViewById(R.id.events_list_progress);

        RestAdapter restAdapter = ServerAdapter.getAdapter();
        ServerWorker serverWorker = restAdapter.create(ServerWorker.class);
        serverWorker.getEventsList(typeId,new Callback<EventsList>() {
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
                        .setNegativeButton("ОК",
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
