package ru.saveselovskiy.mycursach.Events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import ru.saveselovskiy.mycursach.R;

/**
 * Created by sergejveselovskij on 24.05.15.
 */
public class CinemaFragment extends EventsFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("Кино");
        ListView eventList = (ListView) rootView.findViewById(R.id.events_list);
        ProgressBar eventsProgress = (ProgressBar) rootView.findViewById(R.id.events_list_progress);

        String[] items = {"kino1","kino2","kino3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.events_list_cell,items);

        return rootView;
    }
}
