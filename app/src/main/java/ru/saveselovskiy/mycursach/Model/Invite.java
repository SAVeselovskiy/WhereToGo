package ru.saveselovskiy.mycursach.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 03.06.2015.
 */
public class Invite {
    @SerializedName("sender")
    public int sender;

    @SerializedName("event")
    public Event event;

    @SerializedName("senderName")
    public  String senderName;
}
