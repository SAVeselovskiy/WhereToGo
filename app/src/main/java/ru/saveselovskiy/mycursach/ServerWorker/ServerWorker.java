package ru.saveselovskiy.mycursach.ServerWorker;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import ru.saveselovskiy.mycursach.Model.EventsList;
import ru.saveselovskiy.mycursach.Model.Invites;

/**
 * Created by Admin on 03.06.2015.
 */
public interface ServerWorker {
    @GET("/events")
    public void getEventsList(@Query("type_id") int typeId, Callback<EventsList> callback);
    @GET("/invites/{id}")
    public void getMyInvites(@Path("id") int myVkId, Callback<Invites> callback);
    @POST("/invites/{id}")
    public void inviteFrientWithId(@Path("id") int friendId, Callback<JSONObject> callback);
}
