package ru.saveselovskiy.mycursach.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 03.06.2015.
 */
public class Event  implements Parcelable{
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


    public Event(Parcel in){
        name = in.readString();
        description = in.readString();
        id = in.readInt();
        boolean[] array = new boolean[1];
        in.readBooleanArray(array);
        hasPhoto = array[0];
        start_date = in.readInt();
        end_date = in.readInt();
        typeId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(id);
        boolean[] array = new boolean[1];
        array[0] = hasPhoto;
        dest.writeBooleanArray(array);
        dest.writeInt(start_date);
        dest.writeInt(end_date);
        dest.writeInt(typeId);
    }
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>(){
        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }

        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }
    };
}
