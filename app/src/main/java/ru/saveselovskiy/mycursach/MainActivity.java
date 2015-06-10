package ru.saveselovskiy.mycursach;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKUIHelper;

import ru.saveselovskiy.mycursach.Events.EventsFragment;
import ru.saveselovskiy.mycursach.Favorites.FavoritesFragment;
import ru.saveselovskiy.mycursach.FriendList.FriendListFragment;
import ru.saveselovskiy.mycursach.Invites.IncomingInvitesFragment;
import ru.saveselovskiy.mycursach.Model.EventsList;

public class MainActivity extends ActionBarActivity {
    private Fragment currentFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String str = "need changes";

//        SharedPreferences sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
//        String favorites = sharedPreferences.getString("events",null);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove("events");
//        editor.commit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_friend_list),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_invitation),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_favorites),
                        new SectionDrawerItem().withName(R.string.drawer_item_events),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_cinema),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_theater),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_museum),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_concert),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_question)
                ).withOnDrawerListener(new Drawer.OnDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }
        }).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            // Обработка клика
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                switch (position){
                    case 1:{
                        if (currentFragment != null && currentFragment instanceof FriendListFragment){
                            break;
                        }
                        if (currentFragment != null) {
                            getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                        currentFragment = FriendListFragment.newInstance();
                        getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, FriendListFragment.TAG).commit();
                        break;
                    }
                    case 2:{
                        IncomingInvitesFragment eventsFragment = new IncomingInvitesFragment();
                        if (currentFragment != null) {
                            getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                        currentFragment = eventsFragment;
                        getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment,EventsFragment.TAG).commit();
                        setTitle("Приглашения");
                        break;
                    }
                    case 3:{
                        FavoritesFragment eventsFragment = new FavoritesFragment();
                        if (currentFragment != null) {
                            getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                        currentFragment = eventsFragment;
                        getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment,EventsFragment.TAG).commit();
                        setTitle("Избранное");
                        break;
                    }
                    case 5:{

                        EventsFragment eventsFragment = new EventsFragment();
                        eventsFragment.typeId = 1;
                        if (currentFragment != null) {
                            getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                        currentFragment = eventsFragment;
                        getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment,EventsFragment.TAG).commit();
                        setTitle("Кино");
                        break;
                    }
                    case 6:{

                        EventsFragment eventsFragment = new EventsFragment();
                        eventsFragment.typeId = 2;
                        if (currentFragment != null) {
                            getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                        currentFragment = eventsFragment;
                        getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment,EventsFragment.TAG).commit();
                        setTitle("Театры");
                        break;
                    }
                    case 7:{
                        EventsFragment eventsFragment = new EventsFragment();
                        eventsFragment.typeId = 3;
                        if (currentFragment != null) {
                            getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                        currentFragment = eventsFragment;
                        getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment,EventsFragment.TAG).commit();
                        setTitle("Выставки");
                        break;
                    }
                    case 8:{
                        EventsFragment eventsFragment = new EventsFragment();
                        eventsFragment.typeId = 4;
                        if (currentFragment != null) {
                            getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                        currentFragment = eventsFragment;
                        getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment,EventsFragment.TAG).commit();
                        setTitle("Концерты");
                        break;
                    }
                    default:{
                        if (currentFragment == null) break;
                        setTitle(R.string.app_name);
                        getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        currentFragment = null;
                    }
                }
                closeContextMenu();

            }
        })
                .build();
        if (currentFragment != null){
            currentFragment = FriendListFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, FriendListFragment.TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //SomeActivity - имя класса Activity для которой переопределяем onBackPressed();

//                        MainActivity.finish();
                        VKAccessToken.removeTokenAtKey(getApplicationContext(), "accessToken");
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                        MainActivity.super.onBackPressed();
                        finish();

                    }
                }).create().show();
    }

}
