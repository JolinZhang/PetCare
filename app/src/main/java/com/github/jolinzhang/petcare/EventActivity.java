package com.github.jolinzhang.petcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.util.Util;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Shadow on 11/27/16.
 */

public class EventActivity extends AppCompatActivity {

    EditText titleTextView;
    EditText descriptionTextView;
    ImageView pictureImageView;
    TextView locationTextView;
    TextView dateTextView;
    private Uri pictureUri;

    Event event = new Event();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        //edit title
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleTextView.setCursorVisible(true);
            }
        });

        //edit content
        descriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionTextView.setCursorVisible(true);

            }
        });

        //edit picture
        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
            }
        });

    }


    /* Image picker. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            pictureUri = data.getData();
            Util.getInstance().loadImage(pictureUri, pictureImageView);
        }
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
                save();
                finish();
                break;
            case R.id.delete_event:

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void save(){
        event.setTitle(titleTextView.getText().toString());
        event.setDescription(titleTextView.getText().toString());
        if (pictureUri != null) {
            event.setHasPicture(true);
            Util.getInstance().uploadPicture(pictureUri, event.getId(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        }
        DataRepository.getInstance().createOrUpdateEvent(event);
    }

    private void bindUI() {
        titleTextView = (EditText) findViewById(R.id.event_title);
        descriptionTextView = (EditText) findViewById(R.id.event_description);
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
