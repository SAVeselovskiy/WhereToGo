package ru.saveselovskiy.mycursach;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.UIUtils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKUIHelper;

import ru.saveselovskiy.mycursach.Events.EventsFragment;
import ru.saveselovskiy.mycursach.Favorites.FavoritesFragment;
import ru.saveselovskiy.mycursach.FriendList.FriendListFragment;
import ru.saveselovskiy.mycursach.FriendList.ImageLoader;
import ru.saveselovskiy.mycursach.Invites.IncomingInvitesFragment;
import ru.saveselovskiy.mycursach.Model.EventsList;

public class MainActivity extends ActionBarActivity {
    private Fragment currentFragment = null;
    private AccountHeader headerResult = null;
    private Drawer result = null;


    //метод вызывающийся при создании Активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Берм из SharedPreferences информацию о пользователе
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = prefs.getString("currentUserName", "My name");
        final String photoURL = prefs.getString("userPhotoURL", "");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = new ImageView(MainActivity.this);
        ImageLoader imageLoader = new ImageLoader(MainActivity.this);
        imageLoader.DisplayImage(photoURL, imageView, R.drawable.icon_user_default);


        //создаем Заголов для NavigationDrawer, который содержит фото и имя пользователя

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(name).withIcon(getResources().getDrawable(R.drawable.avatar)).withTextColor(Color.LTGRAY)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //Создаем сам NavigationDrawer, добавляем слушателя на нажатие на элементы списка
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_friend_list).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_invitation),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_favorites),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_theater),
                        new SectionDrawerItem().withName(R.string.drawer_item_events),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_cinema),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_museum),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_concert),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_question)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        //В зависимости от позиции нажатого элемента удаляем старый фрагмент и добавляем новый
                        switch (position) {
                            case 0: {
                                if (currentFragment != null && currentFragment instanceof FriendListFragment) {
                                    break;
                                }
                                if (currentFragment != null) {
                                    getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                }
                                currentFragment = FriendListFragment.newInstance();
                                getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, FriendListFragment.TAG).commit();
                                break;
                            }
                            case 1: {
                                IncomingInvitesFragment eventsFragment = new IncomingInvitesFragment();
                                if (currentFragment != null) {
                                    getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                }
                                currentFragment = eventsFragment;
                                getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, EventsFragment.TAG).commit();
                                setTitle("Приглашения");
                                break;
                            }
                            case 2: {
                                FavoritesFragment eventsFragment = new FavoritesFragment();
                                if (currentFragment != null) {
                                    getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                }
                                currentFragment = eventsFragment;
                                getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, EventsFragment.TAG).commit();
                                setTitle("Избранное");
                                break;
                            }
                            case 5: {

                                EventsFragment eventsFragment = new EventsFragment();
                                eventsFragment.typeId = 1;
                                if (currentFragment != null) {
                                    getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                }
                                currentFragment = eventsFragment;
                                getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, EventsFragment.TAG).commit();
                                setTitle("Кино");
                                break;
                            }
                            case 3: {

                                EventsFragment eventsFragment = new EventsFragment();
                                eventsFragment.typeId = 2;
                                if (currentFragment != null) {
                                    getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                }
                                currentFragment = eventsFragment;
                                getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, EventsFragment.TAG).commit();
                                setTitle("Вакансии");
                                break;
                            }
                            case 6: {
                                EventsFragment eventsFragment = new EventsFragment();
                                eventsFragment.typeId = 3;
                                if (currentFragment != null) {
                                    getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                }
                                currentFragment = eventsFragment;
                                getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, EventsFragment.TAG).commit();
                                setTitle("Выставки");
                                break;
                            }
                            case 7: {
                                EventsFragment eventsFragment = new EventsFragment();
                                eventsFragment.typeId = 4;
                                if (currentFragment != null) {
                                    getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                }
                                currentFragment = eventsFragment;
                                getFragmentManager().beginTransaction().add(R.id.parent_container, currentFragment, EventsFragment.TAG).commit();
                                setTitle("Концерты");
                                break;
                            }
                            default: {
                                if (currentFragment == null) break;
                                setTitle(R.string.app_name);
                                getFragmentManager().beginTransaction().remove(currentFragment).commit();
                                currentFragment = null;
                            }
                        }
                        result.closeDrawer();
                        return true;
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
    //определяем методы жизненного цикла Activity. В каждом надо вызывать метод с таким же именем у VKUIHelper, для корректной работы Android SDK
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


    //Обработчик нажатия кнопки Back. По нажатию происходит выход из аккаунта
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        VKAccessToken.removeTokenAtKey(getApplicationContext(), "accessToken");
                        finish();

                    }
                }).create().show();
    }

}
