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
    @GET("/invites")
     void getMyInvites(@Query("id") int myVkId, Callback<Invites> callback);

    @FormUrlEncoded
    @POST("/invites")
    void inviteFrientWithId(@Field("senderId") int myId, @Field("receiverId") String friendsId, @Field("eventId") int eventId, Callback<JSONObject> callback);

    @GET("/friends")
     void getFriends(@Query("id") String indentifires, Callback<Friends> callback);
    @FormUrlEncoded
    @POST("/current")
     void postUser(@Field("id") int vk_id, Callback<JSONObject> callback);

    @GET("/events/{id}/photo")
    void getEventPhoto(@Path("id") int id, Callback<Response> callback);
}
