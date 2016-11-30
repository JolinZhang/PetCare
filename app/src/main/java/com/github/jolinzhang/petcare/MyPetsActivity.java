package com.github.jolinzhang.petcare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Pet;
import com.github.jolinzhang.petcare.Adapter.MyPetsAdapter;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Jonelezhang on 11/29/16.
 */

public class MyPetsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    static private MyPetsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypets);

        //set on toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("My Pets");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.mypets_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (adapter == null) {
            adapter = new MyPetsAdapter(getApplicationContext());
        }
        recyclerView.setAdapter(adapter);

        DataRepository.getInstance().getPets(new RealmChangeListener<RealmResults<Pet>>() {
            @Override
            public void onChange(final RealmResults<Pet> element) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.pets = element;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
