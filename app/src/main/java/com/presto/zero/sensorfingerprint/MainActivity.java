package com.presto.zero.sensorfingerprint;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

  Button[] buttonArray;
  File logFile = null;
  BufferedWriter bw = null;
  EventUtil eventUtil;
  LogUtil logUtil;
  SensorManager sm;
  Sensor accelSensor;
  Sensor gyroSensor;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Configs.LOG_DIR = getExternalFilesDir(null);

    buttonArray = new Button[9];
    buttonArray[0] = (Button) findViewById(R.id.button);
    buttonArray[1] = (Button) findViewById(R.id.button2);
    buttonArray[2] = (Button) findViewById(R.id.button3);
    buttonArray[3] = (Button) findViewById(R.id.button4);
    buttonArray[4] = (Button) findViewById(R.id.button5);
    buttonArray[5] = (Button) findViewById(R.id.button6);
    buttonArray[6] = (Button) findViewById(R.id.button7);
    buttonArray[7] = (Button) findViewById(R.id.button8);
    buttonArray[8] = (Button) findViewById(R.id.button9);

    eventUtil = EventUtil.getInstance();
    logUtil = LogUtil.getInstance();
    sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    if (sm != null) {
      accelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      gyroSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    for (Button b : buttonArray) {
      b.setOnTouchListener(eventUtil);
    }

    Button btnRst = (Button) findViewById(R.id.btnRst);
    btnRst.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          logUtil.resetFile();
          logUtil.initFile();
        }
        catch (Exception e) {

        }
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    logUtil.initFile();
    if (sm != null) {
      if (accelSensor != null) {
        sm.registerListener(eventUtil, accelSensor, SensorManager.SENSOR_DELAY_UI);
      }
      if (gyroSensor != null) {
        sm.registerListener(eventUtil, gyroSensor, SensorManager.SENSOR_DELAY_UI);
      }
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (sm != null) {
      sm.unregisterListener(eventUtil);
    }
    logUtil.closeFile();
  }
}
