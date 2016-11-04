package com.presto.zero.sensorfingerprint;

/**
 * Created by Zero on 10/29/2016.
 */

import android.hardware.SensorEvent;
import android.view.MotionEvent;

/**
 * A singleton class for log recording
 */
public class LogUtil {
  private static LogUtil instance;
  private final static String LOG_FILE_NAME = "touchLog.log";

  public static LogUtil getInstance() {
    if (instance == null) {
      instance = new LogUtil();
    }
    return instance;
  }

  synchronized public void initFile() {

  }

  synchronized public void closeFile() {

  }

  synchronized public void logEvent(MotionEvent e) {

  }

  synchronized public void logEvent(SensorEvent e) {

  }

}
