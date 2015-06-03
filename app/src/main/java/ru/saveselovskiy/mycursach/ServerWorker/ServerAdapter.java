package ru.saveselovskiy.mycursach.ServerWorker;

/**
 * Created by Admin on 03.06.2015.
 */
import retrofit.RestAdapter;

public class ServerAdapter {
    private static String API_URL = "http://54.200.192.248:44480";//
    private static RestAdapter adapter;

    public static RestAdapter getAdapter() {
        if (adapter == null)
            adapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
        return adapter;
    }
    public static String getURL() {
        return API_URL;
    }
}
