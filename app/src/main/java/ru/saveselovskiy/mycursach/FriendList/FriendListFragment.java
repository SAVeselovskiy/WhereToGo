package ru.saveselovskiy.mycursach.FriendList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.methods.VKApiFriends;

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
        ListView rootView = (ListView)inflater.inflate(R.layout.fragment_friend_list, container,
                false);
        VKApiFriends api = new VKApiFriends();
        api.get().addExtraParameters(VKParameters.from(VKApiConst.FIELDS, "last_name"));

//        api.get(VKParameters.from(VKApiConst.FIELDS,"last_name"));
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.friend_list_item,new  String[]{"Рыжик", "Барсик", "Мурзик"});
        getActivity().setTitle("Друзья");
        rootView.setAdapter(adapter);
        up = getActivity();
//        getData();
        return rootView;
    }
}
