package ru.saveselovskiy.mycursach.Events;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import ru.saveselovskiy.mycursach.R;

/**
 * Created by sergejveselovskij on 24.05.15.
 */
public class EventsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.events_layout, container,
                false);
//        ListView eventList = (ListView) rootView.findViewById(R.id.events_list);
//        ProgressBar eventsProgress = (ProgressBar) rootView.findViewById(R.id.events_list_progress);


        return rootView;
    }
}
