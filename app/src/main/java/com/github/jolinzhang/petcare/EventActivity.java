package com.github.jolinzhang.petcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepoConfig;
import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.util.Util;

/**
 * Created by Ru Zhang - rxz151130 on 11/27/16.
 */

public class EventActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView descriptionTextView;
    ImageView pictureImageView;
    TextView dateTextView;

    Event event = new Event();

    /**
     *  Ru Zhang - rxz151130
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

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_edit_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *  Ru Zhang - rxz151130
     */
    //click item on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            //save what user changed
            case R.id.edit_event:
                Intent intent = new Intent(EventActivity.this,NewEventActivity.class);
                intent.putExtra("event_id", event.getId());
                startActivity(intent);
                break;
            case R.id.delete_event:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Please confirm deletion.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataRepository.getInstance().deleteEvent(event.getId());
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }



    /**
     *  Ru Zhang - rxz151130
     */
    private void bindUI() {
        titleTextView = (TextView) findViewById(R.id.event_title);
        descriptionTextView = (TextView) findViewById(R.id.event_description);
        pictureImageView = (ImageView) findViewById(R.id.event_picture);
        dateTextView = (TextView) findViewById(R.id.event_date);
    }

    /**
     *  Ru Zhang - rxz151130
     */
    private void renderUI() {
        titleTextView.setText(event.getTitle());
        descriptionTextView.setText(event.getDescription());
        if (event.hasPicture()) {
            Util.getInstance().loadImage(event.getId(), pictureImageView, false);
        } else { pictureImageView.setVisibility(View.GONE); }
        dateTextView.setText(Util.getInstance().dateFormatter().format(event.getDatetime()));
    }

}
