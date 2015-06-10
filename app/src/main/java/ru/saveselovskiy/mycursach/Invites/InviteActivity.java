package ru.saveselovskiy.mycursach.Invites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import ru.saveselovskiy.mycursach.FriendList.LazyAdapter;
import ru.saveselovskiy.mycursach.Model.Friend;
import ru.saveselovskiy.mycursach.Model.Friends;
import ru.saveselovskiy.mycursach.R;
import ru.saveselovskiy.mycursach.ServerWorker.ServerAdapter;
import ru.saveselovskiy.mycursach.ServerWorker.ServerWorker;
import ru.saveselovskiy.mycursach.SupportFiles.ProgressDialogFragment;

/**
 * Created by Admin on 08.06.2015.
 */
public class InviteActivity extends ActionBarActivity {
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> photos = new ArrayList<String>();
    ArrayList<Integer> usersId = new ArrayList<>();
    int eventId;
    public ArrayList<Integer> recieversId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = (int)getIntent().getExtras().get("eventId");
        setContentView(R.layout.fragment_friend_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        final ListView friendList = (ListView)findViewById(R.id.SA_friend_list);
        final ProgressBar friendsProgress = (ProgressBar)findViewById(R.id.friends_list_progress);
        VKApiFriends api = new VKApiFriends();
        VKRequest request = api.get(VKParameters.from(VKApiConst.FIELDS, "photo_50"));


        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject obj = response.json;
                try {
                    JSONObject usersInfo = (JSONObject) obj.get("response");
                    final JSONArray users = (JSONArray) usersInfo.get("items");

                    String identifires = stringWithIdentifires(users);
                    RestAdapter restAdapter = ServerAdapter.getAdapter();
                    ServerWorker serverWorker = restAdapter.create(ServerWorker.class);
                    serverWorker.getFriends(identifires, new Callback<Friends>() {
                        @Override
                        public void success(Friends friendsList, Response response) {
                            Log.d("myTag", "im in success");
                            try {
                                JSONArray friends = compareFriendLists(users, friendsList.friends);
                                for (int i = 0; i < friends.length(); i++) {
                                    String name = (String) ((JSONObject) friends.get(i)).get("first_name") + " " + (String) ((JSONObject) friends.get(i)).get("last_name");
                                    String photoURL = (String) ((JSONObject) friends.get(i)).get("photo_50");
                                    int userId = (int) ((JSONObject) friends.get(i)).get("id");
                                    names.add(name);
                                    photos.add(photoURL);
                                    usersId.add(userId);
                                }
                            } catch (org.json.JSONException e) {
                                Log.d("myTag", e.getLocalizedMessage());
                            }
                            friendsProgress.setVisibility(View.INVISIBLE);
                            String[] photoArray = new String[photos.size()];
                            photoArray = (String[]) photos.toArray(photoArray);
                            InvitesFriendsAdapter myAdapter = new InvitesFriendsAdapter(InviteActivity.this, photoArray);
                            myAdapter.names = names;
                            myAdapter.usersId = usersId;
                            friendList.setAdapter(myAdapter);

                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InviteActivity.this);
                            builder.setTitle(retrofitError.getLocalizedMessage())
                                    .setMessage(retrofitError.getMessage())
                                    .setCancelable(false)
                                    .setNegativeButton("OK",
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_invites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send_invites) {
            ProgressDialogFragment.showProgressDialog(getFragmentManager(),"Отправка...",false);
            Log.d("myLog", "done button pressed");
            String friendsId = "";
            for (int i = 0; i < recieversId.size(); i++) {
                if (i == 0) {
                    friendsId = friendsId.concat("" + recieversId.get(i));
                }else {
                    friendsId = friendsId.concat("," + recieversId.get(i));
                }
            }
            RestAdapter restAdapter = ServerAdapter.getAdapter();
            ServerWorker serverWorker = restAdapter.create(ServerWorker.class);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int myId = prefs.getInt("currentUserId",0);
            serverWorker.inviteFrientWithId(myId, friendsId, eventId, new Callback<JSONObject>() {
                @Override
                public void success(JSONObject jsonObject, Response response) {
                    ProgressDialogFragment.closeProgressDialogSuccessfully(getFragmentManager());
//                    Toast.makeText(InviteActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    ProgressDialogFragment.closeProgressDialogSuccessfully(getFragmentManager());
                    AlertDialog.Builder builder = new AlertDialog.Builder(InviteActivity.this);
                    builder.setTitle(error.getLocalizedMessage())
                            .setMessage(error.getMessage())
                            .setCancelable(false)
                            .setNegativeButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            return true;
        }
        finish();
        return true;
    }
}
