package com.github.jolinzhang.petcare;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Shadow on 11/27/16.
 */

public class NewEventActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;

    private Event event = new Event();

    private EditText titleEditText;
    private EditText descriptionEditText;
    private ImageView pictureImageView;
    private TextView locationTextView;
    private ImageButton pictureButton;
    private ImageButton locationButton;
    private ImageButton dateButton;
    private ImageButton completeButton;

    private Uri pictureUri;

    private Location location;

    private DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;

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
                requestLocation();
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setCompleted(!event.isCompleted());
                refreshInfo();
            }
        });

        /* Birthday picker. */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                timePickerDialog.show();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), i, i1);
                event.setDatetime(calendar.getTime());
                refreshInfo();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save_action:
                save();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
        locationTextView.setText(info);
    }

    private void bindUI() {
        titleEditText = (EditText) findViewById(R.id.new_event_title);
        descriptionEditText = (EditText) findViewById(R.id.new_event_description);
        pictureImageView = (ImageView) findViewById(R.id.new_event_picture);
        locationTextView = (TextView) findViewById(R.id.new_event_location_info);
        pictureButton = (ImageButton) findViewById(R.id.new_event_picture_button);
        locationButton = (ImageButton) findViewById(R.id.new_event_location);
        dateButton = (ImageButton) findViewById(R.id.new_event_date);
        completeButton = (ImageButton) findViewById(R.id.new_event_complete);
    }

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
                }
            });
        }
        event.setDatetime(calendar.getTime());
        if(location != null){
            event.setLongitude(location.getLongitude());
            event.setLatitude(location.getLatitude());
        }
        DataRepository.getInstance().createOrUpdateEvent(event);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }
}
