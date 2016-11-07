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

  LogUtil logUtil = LogUtil.getInstance();

  private static EventUtil instance;

  public static EventUtil getInstance() {
    if (instance == null) {
      instance = new EventUtil();
    }
    return instance;
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    logUtil.logEvent(sensorEvent);
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {

    logUtil.logEvent(view, motionEvent);
    return false;
  }
}
