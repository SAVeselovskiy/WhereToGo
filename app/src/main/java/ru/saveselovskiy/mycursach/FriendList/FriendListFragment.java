package ru.saveselovskiy.mycursach.FriendList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.saveselovskiy.mycursach.Model.Friend;
import ru.saveselovskiy.mycursach.Model.Friends;
import ru.saveselovskiy.mycursach.R;
import ru.saveselovskiy.mycursach.ServerWorker.ServerAdapter;
import ru.saveselovskiy.mycursach.ServerWorker.ServerWorker;

/**
 * Created by Admin on 29.03.2015.
 */
public class FriendListFragment extends Fragment {
    public static final String TAG = FriendListFragment.class
            .getSimpleName();
    private Activity up;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> photos = new ArrayList<String>();

    public static FriendListFragment newInstance(){
        return new FriendListFragment();
    }
    public FriendListFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Друзья");
//        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend_list, container,
                false);
        final ListView friendList = (ListView)rootView.findViewById(R.id.SA_friend_list);
        final ProgressBar friendsProgress = (ProgressBar)rootView.findViewById(R.id.friends_list_progress);
        VKApiFriends api = new VKApiFriends();
        VKRequest request = api.get(VKParameters.from(VKApiConst.FIELDS, "photo_50"));


        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject obj =  response.json;
                try {
                    JSONObject usersInfo = (JSONObject) obj.get("response");
                    final JSONArray users = (JSONArray) usersInfo.get("items");

                    String identifires = stringWithIdentifires(users);
                    RestAdapter restAdapter = ServerAdapter.getAdapter();
                    ServerWorker serverWorker = restAdapter.create(ServerWorker.class);
                    serverWorker.getFriends(identifires, new Callback<Friends>() {
                        @Override
                        public void success(Friends friendsList, Response response) {
                            Log.d("myTag","im in success");
                            try {
                                JSONArray friends = compareFriendLists(users,friendsList.friends);
                                for (int i = 0; i < friends.length(); i++) {
                                    String name = (String) ((JSONObject) friends.get(i)).get("first_name") + " " + (String) ((JSONObject) friends.get(i)).get("last_name");
                                    String photoURL = (String) ((JSONObject) friends.get(i)).get("photo_50");
                                    names.add(name);
                                    photos.add(photoURL);
                                }
                            }catch(org.json.JSONException e){
                                Log.d("myTag",e.getLocalizedMessage());
                            }
                            friendsProgress.setVisibility(View.INVISIBLE);
                            String[] photoArray = new String[photos.size()];
                            photoArray = (String[])photos.toArray(photoArray);
                            LazyAdapter myAdapter = new LazyAdapter(getActivity(),photoArray);
                            myAdapter.names = names;
                            friendList.setAdapter(myAdapter);
                            up = getActivity();
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(retrofitError.getLocalizedMessage())
                                    .setMessage(retrofitError.getMessage())
                                    .setCancelable(false)
                                    .setNegativeButton("ОК",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    private String stringWithIdentifires(JSONArray friends){
        String identifires = "";
        try {
            for (int i = 0; i < friends.length()-1; i++) {
                identifires = identifires + ((JSONObject) friends.get(i)).get("id")+",";
            }
            identifires = identifires + ((JSONObject) friends.get(friends.length()-1)).get("id");
        }catch(org.json.JSONException e){
            Log.d("myTag",e.getLocalizedMessage());
        }
        return identifires;
    }

    private JSONArray compareFriendLists(JSONArray users, Friend[] friends){
        JSONArray newArray = new JSONArray();
        try {
            for (int i = 0; i < users.length(); i++) {
                for (int j = 0; j < friends.length; j++) {
                    if ((int) ((JSONObject) users.get(i)).get("id") == friends[j].id) {
                        newArray.put(users.get(i));
                    }
                }
            }
        }catch(org.json.JSONException e){
            Log.d("myTag",e.getLocalizedMessage());
        }
        return newArray;
    }

}
