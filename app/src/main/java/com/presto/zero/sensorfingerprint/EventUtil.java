package com.presto.zero.sensorfingerprint;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Zero on 10/29/2016.
 */

public class EventUtil implements SensorEventListener, View.OnTouchListener {

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
