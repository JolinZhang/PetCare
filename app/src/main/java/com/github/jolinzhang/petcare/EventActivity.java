package com.github.jolinzhang.petcare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.util.Util;

/**
 * Created by Shadow on 11/27/16.
 */

public class EventActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView descriptionTextView;
    ImageView pictureImageView;
    TextView locationTextView;
    TextView dateTextView;

    Event event = new Event();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Event Info");

        bindUI();

        String eventId = getIntent().getStringExtra("event_id");
        if (eventId != null) {
            event = DataRepository.getInstance().getEvent(eventId);
        }

        renderUI();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_save_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //click item on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            //save what user changed
            case R.id.save_event:
                finish();
                break;
            case R.id.delete_event:

                break;
        }


        return super.onOptionsItemSelected(item);
    }




    private void bindUI() {
        titleTextView = (TextView) findViewById(R.id.event_title);
        descriptionTextView = (TextView) findViewById(R.id.event_description);
        pictureImageView = (ImageView) findViewById(R.id.event_picture);
        locationTextView = (TextView) findViewById(R.id.event_location);
        dateTextView = (TextView) findViewById(R.id.event_date);
    }

    private void renderUI() {
        titleTextView.setText(event.getTitle());
        descriptionTextView.setText(event.getDescription());
        Util.getInstance().loadImage(event.getId(), pictureImageView, false);
        locationTextView.setText(event.getLatitude() + ", " + event.getLongitude());
        dateTextView.setText(Util.getInstance().dateFormatter().format(event.getDatetime()));
    }

}
