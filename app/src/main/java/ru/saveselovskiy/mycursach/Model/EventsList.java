package ru.saveselovskiy.mycursach.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 03.06.2015.
 */
public class EventsList {
    @SerializedName("events")
    public Event[] events;
}
