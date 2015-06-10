package ru.saveselovskiy.mycursach.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.dialogs.VKCaptchaDialog;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.saveselovskiy.mycursach.MainActivity;
import ru.saveselovskiy.mycursach.R;
import ru.saveselovskiy.mycursach.ServerWorker.ServerAdapter;
import ru.saveselovskiy.mycursach.ServerWorker.ServerWorker;

/**
 * Created by Admin on 27.03.2015.
 */
public class Login extends Activity {//implements View.OnClickListener{
    private static final String[] sMyScope = new String[] {
            VKScope.FRIENDS,
            VKScope.NOHTTPS
    };
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String tokenKey = "accessToken";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
//        VKSdk.getAccessToken();
        VKAccessToken token = VKAccessToken.tokenFromSharedPreferences(this, tokenKey);
        if (token != null){
            VKSdk.initialize(sdkListener, "4850781",token);
        }
        else{
            VKSdk.initialize(sdkListener, "4850781");
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Button btn = (Button) findViewById(R.id.LoginButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.authorize(sMyScope, true, false);

                int i = 0;
            }
        });
    }

    private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            Intent intent = new Intent(getApplication(),MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onAccessDenied(VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            final VKAccessToken myToken = newToken;
            RestAdapter restAdapter = ServerAdapter.getAdapter();
            final ServerWorker serverWorker = restAdapter.create(ServerWorker.class);
            myToken.saveTokenToSharedPreferences(getApplicationContext(), tokenKey);
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("currentUserId", Integer.parseInt(myToken.userId));
            edit.commit();


            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);

            VKApiUsers vkApiUser = new VKApiUsers();
            VKRequest request = vkApiUser.get(VKParameters.from(VKApiConst.FIELDS, "first_name"));
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    try {
                        JSONObject obj = response.json;
                        JSONArray array = obj.getJSONArray("response");
                        JSONObject me = (JSONObject) array.get(0);
                        String myName =  me.getString("first_name") + " " +me.getString("last_name");
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("currentUserName",myName);
                        editor.commit();
                        serverWorker.postUser(Integer.parseInt(myToken.userId),myName, new Callback<JSONObject>() {
                            @Override
                            public void success(JSONObject jsonObject, Response response) {

                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

                    }
                }
            });


        }
        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            Intent intent = new Intent(getApplication(),MainActivity.class);
            startActivity(intent);
        }
    };

//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(getApplication(),MainActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
