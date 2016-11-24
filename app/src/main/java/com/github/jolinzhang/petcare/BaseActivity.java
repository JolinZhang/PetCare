package com.github.jolinzhang.petcare;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Shadow on 11/20/16.
 */

public class BaseActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLight;

    private float NIGHT_MODE_THRESHHOLD = 70;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (Configuration.getInstance().getNightMode(this)) {
            this.setTheme(R.style.AppThemeNight);
        } else {
            this.setTheme(R.style.AppTheme);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float newLight = sensorEvent.values[0];
        Boolean nightMode = Configuration.getInstance().getNightMode(this);
        if (newLight > NIGHT_MODE_THRESHHOLD && nightMode) {
            Configuration.getInstance().setNightMode(false, this);
            recreate();
        } else if (newLight < NIGHT_MODE_THRESHHOLD && !nightMode) {
            Configuration.getInstance().setNightMode(true, this);
            recreate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
