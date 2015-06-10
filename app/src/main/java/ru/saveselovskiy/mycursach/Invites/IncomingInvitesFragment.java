package ru.saveselovskiy.mycursach.Invites;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.saveselovskiy.mycursach.Model.Invites;
import ru.saveselovskiy.mycursach.R;
import ru.saveselovskiy.mycursach.ServerWorker.ServerAdapter;
import ru.saveselovskiy.mycursach.ServerWorker.ServerWorker;

/**
 * Created by Admin on 11.06.2015.
 */
public class IncomingInvitesFragment extends Fragment {

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int vkId = prefs.getInt("currentUserId",0);
        serverWorker.getMyInvites(vkId, new Callback<Invites>() {
            @Override
            public void success(Invites invites, Response response) {
                eventsProgress.setVisibility(View.INVISIBLE);
                if (invites.invites.length==0){
                    backgroundView.setVisibility(View.VISIBLE);
                    return;
                }
                InviteListAdapter adapter = new InviteListAdapter(getActivity(), invites.invites);
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
