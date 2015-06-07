package ru.saveselovskiy.mycursach.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 03.06.2015.
 */
public class Event {
    @SerializedName("id")
    public int id;

    @SerializedName("hasPhoto")
    public boolean hasPhoto;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("start_date")
    public int start_date;

    @SerializedName("end_date")
    public int end_date;

    @SerializedName("typeId")
    public int typeId;


}
