package com.presto.zero.sensorfingerprint;

/**
 * Created by Zero on 10/29/2016.
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A singleton class for log recording
 */
public class LogUtil {
  private static LogUtil instance;
  private final static String LOG_FILE_NAME = "touchLog.log";

  private File logFile;
  private BufferedWriter bw;


  public static LogUtil getInstance() {
    if (instance == null) {
      instance = new LogUtil();
    }
    return instance;
  }

  synchronized public void initFile() {
    logFile = new File(Configs.LOG_DIR, Configs.LOG_FILE);
    Log.d("EVTFS", "Logfile : " + logFile.getAbsolutePath());
    try {
      if (!logFile.exists()) {
        logFile.createNewFile();
      }
      FileWriter fw = new FileWriter(logFile, true);
      bw = new BufferedWriter(fw);
    } catch (IOException ioe) {
      Log.d("SENFIN", "Log file creation error " + ioe.getMessage());
      bw = null;
    }
  }

  synchronized public void closeFile() {
    try {
      if (bw != null) {
        bw.close();
      }
    } catch (Exception e) {
      //Ignore the exceptions when closing a file.
    } finally {
      bw = null;
    }
  }

  synchronized public void resetFile() {
    closeFile();
    logFile = new File(Configs.LOG_DIR, Configs.LOG_FILE);
    if (logFile.exists()) {
      logFile.delete();
    }
    try {
      logFile.createNewFile();
    } catch (IOException ioe) {
      Log.d("SENFIN", "Log file creation error in reset " + ioe.getMessage());
    }
  }

  //Scheme:
  //EventType(TOUCHDOWN/TOUCHUP), timestamp, widgetAbsX, widgetAbsY, widgetWidth, widgetHeight, touchAbsX, touchAbsY;
  synchronized public void logEvent(View v, MotionEvent e) {
    //If logfile is not available. Ignore this input.
    if (bw == null) {
      return;
    }
    String eventType;
    int motionAction = e.getAction();
    if (motionAction == MotionEvent.ACTION_DOWN) {
      eventType = "TOUCHDOWN";
    } else if (motionAction == MotionEvent.ACTION_UP) {
      eventType = "TOUCHUP";
    } else {
      eventType = "TOUCHUNKNOW" + motionAction;
    }

    int widgetX = v.getLeft();
    int widgetY = v.getTop();
    int widgetH = v.getHeight();
    int widgetW = v.getWidth();
    float touchX = e.getRawX();
    float touchY = e.getRawY();
    long timestamp = e.getEventTime();
    String buf = String.format("%s,%d,%d,%d,%d,%d,%f,%f\n",
            eventType, timestamp, widgetX, widgetY, widgetH, widgetW, touchX, touchY);
    try {
      bw.write(buf);
    } catch (Exception ec) {

    }
  }

  //Scheme:
  //EventType(ACCEL/GYRO), timestamp, Gx/Sx, Gy/Sy, Gz/Sz, 0, 0, 0
  synchronized public void logEvent(SensorEvent e) {
    //If logfile is not available. Ignore this input.
    if (bw == null) {
      return;
    }

    String eventType;
    int sensorType = e.sensor.getType();
    if (sensorType == Sensor.TYPE_ACCELEROMETER) {
      eventType = "ACCEL";
    } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
      eventType = "GYRO";
    } else {
      eventType = "UNKNOW" + sensorType;
    }

    float sX = 0;
    float sY = 0;
    float sZ = 0;
    long timestamp = e.timestamp;
    if (sensorType == Sensor.TYPE_GYROSCOPE || sensorType == Sensor.TYPE_ACCELEROMETER) {
      sX = e.values[0];
      sY = e.values[1];
      sZ = e.values[2];
    }

    String buf = String.format("%s,%d,%f,%f,%f,0,0,0\n", eventType, timestamp, sX, sY, sZ);
    try {
      bw.write(buf);
    } catch(Exception ec) {

    }
  }
}
