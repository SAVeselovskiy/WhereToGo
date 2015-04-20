package ru.saveselovskiy.mycursach.FriendList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import ru.saveselovskiy.mycursach.R;

/**
 * Created by Admin on 29.03.2015.
 */
public class FriendListFragment extends Fragment {
    public static final String TAG = FriendListFragment.class
            .getSimpleName();
    private Activity up;
    public static FriendListFragment newInstance(){
        return new FriendListFragment();
    }
    public FriendListFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ListView rootView = (ListView)inflater.inflate(R.layout.fragment_friend_list, container,
                false);
        VKApiFriends api = new VKApiFriends();
//        api.get().addExtraParameters(VKParameters.from(VKApiConst.FIELDS, "last_name"));
        VKRequest request = api.get(VKParameters.from(VKApiConst.FIELDS, "last_name"));
//        request.addExtraParameters(VKParameters.from(VKApiConst.FIELDS, "last_name"));
        final ArrayList<String> names = new ArrayList<String>();
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject obj =  response.json;
                try {
                    TextView textView = (TextView)getActivity().findViewById(R.id.text1);
                    JSONObject usersInfo = (JSONObject) obj.get("response");
                    JSONArray users = (JSONArray) usersInfo.get("items");
                    for (int i = 0; i < (Integer)usersInfo.get("count"); i++) {
                       String name = (String)((JSONObject)users.get(i)).get("first_name") + " " + (String)((JSONObject)users.get(i)).get("last_name");
                        names.add(name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.friend_list_item,names);
                    getActivity().setTitle("Друзья");
                    rootView.setAdapter(adapter);
                    up = getActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                obj.getJSONArray();
//                response.json.getJSONArray()
            }
        });
//        api.get(VKParameters.from(VKApiConst.FIELDS,"last_name"));

//        getData();
        return rootView;
    }
}
