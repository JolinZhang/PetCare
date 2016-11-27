package com.github.jolinzhang.petcare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Pet;
import com.github.jolinzhang.petcare.Fragment.FutureEventFragment;
import com.github.jolinzhang.petcare.Fragment.GalleryFragment;
import com.github.jolinzhang.petcare.Fragment.SettingFragment;
import com.github.jolinzhang.petcare.Fragment.TimeLineFragment;

import java.util.Date;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TimeLineFragment timeLineFragment;
    private FutureEventFragment futureEventFragment;
    private GalleryFragment galleryFragment;
    private SettingFragment settingFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getFragmentManager();

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        if (id == R.id.timeLine) {
//            Realm.getDefaultInstance().beginTransaction();
//            Pet p = Realm.getDefaultInstance().where(Pet.class).equalTo("id", "hellodiva").findFirst();
//            p.setBirthday(new Date());
//            Realm.getDefaultInstance().copyToRealmOrUpdate(p);
//            Realm.getDefaultInstance().commitTransaction();
            Intent intent = new Intent(this, NewEventActivity.class);
//            intent.putExtra("pet_id", "hellodiva");
            startActivity(intent);
            //connect to timeLine Fragment
//            timeLineFragment = (TimeLineFragment) fragmentManager.findFragmentByTag("TimeLineFragment");
//            if(timeLineFragment == null){
//                timeLineFragment = new TimeLineFragment();
//            }
//            transaction.replace(R.id.content_scrolling, timeLineFragment,"TimeLineFragment")
//            .commit();

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
