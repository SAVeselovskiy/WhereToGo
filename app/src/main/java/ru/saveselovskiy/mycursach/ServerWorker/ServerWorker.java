package ru.saveselovskiy.mycursach.ServerWorker;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import ru.saveselovskiy.mycursach.Model.EventsList;
import ru.saveselovskiy.mycursach.Model.Friends;
import ru.saveselovskiy.mycursach.Model.Invites;

/**
 * Created by Admin on 03.06.2015.
 */
public interface ServerWorker {
    @GET("/events")
     void getEventsList(@Query("type") int typeId, Callback<EventsList> callback);
    @GET("/invites/{id}")
     void getMyInvites(@Path("id") int myVkId, Callback<Invites> callback);
    @POST("/invites/{id}")
    public void inviteFrientWithId(@Path("id") int friendId, Callback<JSONObject> callback);

    @GET("/friends")
     void getFriends(@Query("id") String indentifires, Callback<Friends> callback);
    @FormUrlEncoded
    @POST("/current")
     void postUser(@Field("id") int vk_id, Callback<JSONObject> callback);
}
