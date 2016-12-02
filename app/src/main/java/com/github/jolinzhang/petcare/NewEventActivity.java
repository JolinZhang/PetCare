package com.github.jolinzhang.petcare;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Event;
import com.github.jolinzhang.util.Util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Ru Zhang on 11/27/16.
 */

public class NewEventActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;

    private Event event = new Event();

    private EditText titleEditText;
    private EditText descriptionEditText;
    private ImageView pictureImageView;
    private ImageButton pictureButton;
    private ImageButton locationButton;
    private ImageButton dateButton;

    private Uri pictureUri;

    private Location location;

    private DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("New Event");

        //location service
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        bindUI();

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictureUri == null) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else {
                    pictureUri = null;
                    Util.getInstance().loadImage(event.getId(), pictureImageView, false);
                }
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_location_on_red));
                requestLocation();
            }
        });

        /* Date time picker. */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), i, i1);
                event.setDatetime(calendar.getTime());
                refreshInfo();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                timePickerDialog.show();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        String eventId = getIntent().getStringExtra("event_id");
        if (eventId != null) {
            setEvent(DataRepository.getInstance().getEvent(eventId));
        }

        pictureImageView.setVisibility(View.GONE);

    }

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_action:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  Ru Zhang - rxz151130
     */
    private void refreshInfo() {
        String info = "";
        if (location != null) {
            info = location.getLongitude() + ", " + location.getLatitude();
        }
        info += "\n";
        info += event.isCompleted() ? "completed" : "not completed";
        if (event.getDatetime() != null) {
            info += "\n";
            info += Util.getInstance().dateFormatter().format(event.getDatetime());
        }
    }

    private void bindUI() {
        titleEditText = (EditText) findViewById(R.id.new_event_title);
        descriptionEditText = (EditText) findViewById(R.id.new_event_description);
        pictureImageView = (ImageView) findViewById(R.id.new_event_picture);
        pictureButton = (ImageButton) findViewById(R.id.new_event_picture_button);
        locationButton = (ImageButton) findViewById(R.id.new_event_location);
        dateButton = (ImageButton) findViewById(R.id.new_event_date);
    }

    /**
     *  Ru Zhang - rxz151130
     */
    private void save() {
        event.setId(UUID.randomUUID().toString());
        event.setTitle(titleEditText.getText().toString());
        event.setDescription(descriptionEditText.getText().toString());
        if (pictureUri != null) {
            event.setHasPicture(true);
            Util.getInstance().uploadPicture(pictureUri, event.getId(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    finish();
                }
            });
        }
        event.setDatetime(calendar.getTime());
        if (event.getDatetime().after(new Date())) { event.setCompleted(false);}
        else { event.setCompleted(true); }
        if(location != null){
            event.setLongitude(location.getLongitude());
            event.setLatitude(location.getLatitude());
        }
        DataRepository.getInstance().createOrUpdateEvent(event);
        if (pictureUri == null) { finish(); }
    }

    /* Image picker. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            pictureUri = data.getData();
            pictureImageView.setVisibility(View.VISIBLE);
            Util.getInstance().loadImage(pictureUri, pictureImageView);
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        refreshInfo();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    /**
     *  Ru Zhang - rxz151130
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }

    private void setEvent(Event event) {
        this.event = event;
        titleEditText.setText(this.event.getTitle());
        descriptionEditText.setText(this.event.getDescription());
        if (this.event.hasPicture()) {
            pictureImageView.setVisibility(View.VISIBLE);
            Util.getInstance().loadImage(this.event.getId(), pictureImageView, false); }
    }
}
