package com.github.jolinzhang.petcare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jolinzhang.model.DataRepoConfig;
import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.IDataRepoConfig;
import com.github.jolinzhang.model.Pet;
import com.github.jolinzhang.petcare.Fragment.FutureEventFragment;
import com.github.jolinzhang.petcare.Fragment.GalleryFragment;
import com.github.jolinzhang.petcare.Fragment.SettingFragment;
import com.github.jolinzhang.petcare.Fragment.TimeLineFragment;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TimeLineFragment timeLineFragment;
    private FutureEventFragment futureEventFragment;
    private GalleryFragment galleryFragment;
    private SettingFragment settingFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private  View header;
    private Menu menuNav;
    private ImageView mswitch;
    private boolean  switchModel = false;
    private RealmResults<Pet> pets;
    private int  pet_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        //show TimeLineFragment as default fragment
        timeLineFragment = (TimeLineFragment) fragmentManager.findFragmentByTag("TimeLineFragment");
        if(timeLineFragment == null){
            timeLineFragment = new TimeLineFragment();
        }
        transaction.replace(R.id.content_scrolling, timeLineFragment,"TimeLineFragment")
                .commit();

        //Floating Action Bar action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //switch button in nav header.
        header = navigationView.getHeaderView(0);
        mswitch = (ImageView) header.findViewById(R.id.switchButton);
        mswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuNav = navigationView.getMenu();
                if(switchModel == false){
                    //menu_nav disable, show user_list
                    menuNav.setGroupVisible(R.id.menu_nav, false);
                    menuNav.setGroupVisible(R.id.user_list,true);
                    pets = DataRepository.getInstance().getPets();

                    pets.addChangeListener(new RealmChangeListener<RealmResults<Pet>>() {
                        @Override
                        public void onChange(RealmResults<Pet> element) {
                            //reset all value after clicking every time
                          menuNav.removeGroup(R.id.user_list);
                            pet_id = 0;
                          for(Pet pet: element){
                                menuNav.add(R.id.user_list,pet_id++,Menu.NONE,pet.getName()).setIcon(R.drawable.ic_setting_account);
                            }
                            menuNav.add(R.id.user_list,pet_id++,Menu.NONE,"Add Pet").setIcon(R.drawable.ic_menu_add);
                            switchModel = true;
                            }
                    });

                }else {
                    //show menu_nav, hide user_list
                    menuNav.setGroupVisible(R.id.menu_nav, true);
                    menuNav.setGroupVisible(R.id.user_list,false);
                    switchModel = false;
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }







    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        
        if (id == R.id.timeLine) {
            //connect to timeLine Fragment
            timeLineFragment = (TimeLineFragment) fragmentManager.findFragmentByTag("TimeLineFragment");
            if(timeLineFragment == null){
                timeLineFragment = new TimeLineFragment();
            }
            transaction.replace(R.id.content_scrolling, timeLineFragment,"TimeLineFragment")
            .commit();

        } else if (id == R.id.featureEvent) {
            futureEventFragment = (FutureEventFragment) fragmentManager.findFragmentByTag("FutureEventFragment");
            if(futureEventFragment == null){
                futureEventFragment = new FutureEventFragment();
            }

            transaction.replace(R.id.content_scrolling, futureEventFragment, "FutureEventFragment")
                    .commit();
        } else if (id == R.id.gallery) {
            galleryFragment = (GalleryFragment) fragmentManager.findFragmentByTag("GalleryFragment");
            if(galleryFragment == null){
                galleryFragment = new GalleryFragment();
            }
            transaction.replace(R.id.content_scrolling, galleryFragment, "GalleryFragment")
                    .commit();

        } else if (id == R.id.setting) {
            settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("SettingFragment");
            if(settingFragment == null){
                settingFragment = new SettingFragment();
            }
            transaction.replace(R.id.content_scrolling,settingFragment,"SettingFragment")
                    .commit();
        }


        //handle change pet event here

        if(pet_id > 0){
            // add new pet
            if(id == (pet_id-1)){





            }
            if(id< (pet_id-1)){
                DataRepoConfig.getInstance().setCurrentPetId(pets.get(id).getId());

                DataRepository.getInstance().getPet().addChangeListener(new RealmChangeListener<Pet>() {
                    @Override
                    public void onChange(Pet element) {
                       TextView name = (TextView) header.findViewById(R.id.nav_pet_name);
                        name.setText(element.getName());

                    }
                });

                menuNav.setGroupVisible(R.id.menu_nav, true);
                menuNav.setGroupVisible(R.id.user_list,false);
                switchModel = false;
                return true;
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
