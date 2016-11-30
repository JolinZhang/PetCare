package com.github.jolinzhang.petcare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jolinzhang.model.DataRepoConfig;
import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Pet;
import com.github.jolinzhang.petcare.Fragment.FutureEventFragment;
import com.github.jolinzhang.petcare.Fragment.GalleryFragment;
import com.github.jolinzhang.petcare.Fragment.OnThisDayFragment;
import com.github.jolinzhang.petcare.Fragment.SettingFragment;
import com.github.jolinzhang.petcare.Fragment.TimeLineFragment;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TimeLineFragment timeLineFragment;
    private FutureEventFragment futureEventFragment;
    private GalleryFragment galleryFragment;
    private OnThisDayFragment onThisDayFragment;
    private SettingFragment settingFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private  View header;
    private Menu menuNav;
    private ImageView mswitch;
    private boolean switchModel = false;
    private RealmResults<Pet> pets;
    private int petId = 0;

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

        //action for drawerLayout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //show the menu nav by default
        menuNav = navigationView.getMenu();

        //switch button in nav header.
        header = navigationView.getHeaderView(0);
        mswitch = (ImageView) header.findViewById(R.id.switchButton);
        mswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(switchModel == false){
                    //operations on user_list
                    DataRepository.getInstance().getPets(new RealmChangeListener<RealmResults<Pet>>() {
                        @Override
                        public void onChange(RealmResults<Pet> element) {
                            pets = element;
                            menuNav.removeGroup(R.id.user_list);
                            petId = 0;
                            if (pets != null) {
                                for(Pet pet: pets){
                                    menuNav.add(R.id.user_list, petId++,Menu.NONE,pet.getName()).setIcon(R.drawable.ic_setting_account);
                                }
                            }
                            menuNav.add(R.id.user_list, petId++,Menu.NONE,"Add Pet").setIcon(R.drawable.ic_menu_add);
                        }
                    });

                    //menu_nav disable, show user_list
                    menuNav.setGroupVisible(R.id.menu_nav, false);
                    menuNav.setGroupVisible(R.id.user_list,true);
                    switchModel = true;

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

    //navigation item select control
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

        }else if(id == R.id.onThisDay){
            onThisDayFragment = (OnThisDayFragment) fragmentManager.findFragmentByTag("OnThisDayFragment");
            if(onThisDayFragment == null){
                onThisDayFragment = new OnThisDayFragment();
            }
            transaction.replace(R.id.content_scrolling, onThisDayFragment,"OnThisDayFragment")
                    .commit();
        } else if (id == R.id.setting) {
            settingFragment = (SettingFragment) fragmentManager.findFragmentByTag("SettingFragment");
            if(settingFragment == null){
                settingFragment = new SettingFragment();
            }
            transaction.replace(R.id.content_scrolling,settingFragment,"SettingFragment")
                    .commit();
        }


        //handle change pet and add pet here
        if(petId > 0){
            // add new pet
            if (id == (petId - 1)) {
                showDialog();
            } else if(id< (petId - 1)){

                DataRepoConfig.getInstance().setCurrentPetId(pets.get(id).getId());

                DataRepository.getInstance().getPet(new RealmChangeListener<Pet>() {
                    @Override
                    public void onChange(Pet element) {
                        TextView name = (TextView) header.findViewById(R.id.nav_pet_name);
                        name.setText(element.getName());
                    }
                });
                //after choose a pet, reset menuNav groupVisible
                menuNav.setGroupVisible(R.id.menu_nav, true);
                menuNav.setGroupVisible(R.id.user_list,false);
            }
        }

        switchModel = false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //add pets
    private void showDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("The pet already has a pet ID?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showAddPetDiablog();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, NewPetActivity.class);
                        startActivity(intent);
                    }
                })
                .show();

    }

    //add pet already have a pet ID
    private void showAddPetDiablog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Add Pet");
        alertDialog.setMessage("Enter Pet ID");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("ADD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String id = input.getText().toString();
                        Pet pet = DataRepository.getInstance().getPet(id);
                        if (pet == null) {
                            Toast.makeText(getApplicationContext(), "Invalid ID!", Toast.LENGTH_SHORT).show();
                        } else {
                            DataRepoConfig.getInstance().addPetId(id);
                            dialog.cancel();
                            //menu_nav disable, show user_list
                            menuNav.setGroupVisible(R.id.menu_nav, false);
                            menuNav.setGroupVisible(R.id.user_list,true);
                            switchModel = true;
                        }
                    }
                });
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
}
