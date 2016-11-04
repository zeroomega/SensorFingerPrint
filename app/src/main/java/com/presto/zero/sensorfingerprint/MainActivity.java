package com.presto.zero.sensorfingerprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    Button[] buttonArray;
    File logFile = null;
    BufferedWriter bw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
        for (Button b : buttonArray) {
            b.setOnTouchListener(this);
        }

        Button btnRst = (Button) findViewById(R.id.btnRst);
        btnRst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logFile = new File(getExternalFilesDir(null), "touchLog.log");
                    Log.d("EVTFS", "Logfile : " + logFile.getAbsolutePath());
                    if (logFile.exists()) {
                        logFile.createNewFile();
                    }
                }
                catch (Exception e) {

                }
            }
        });
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        View rootView = findViewById(R.id.activity_main);
//        Log.v("ROOTVIEW", rootView.toString() + rootView.getWidth() + " " + rootView.getHeight());
//    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            logFile = new File(getExternalFilesDir(null), "touchLog.log");
            Log.d("EVTFS", "Logfile : " + logFile.getAbsolutePath());
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter fw = new FileWriter(logFile, true);
            bw = new BufferedWriter(fw);

        } catch (Exception e) {
            logFile = null;
            bw = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bw != null) {
            try {
                bw.close();
            } catch (Exception e) {

            }
        }
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view instanceof Button) {
            Button b = (Button) view;
            int buttonX = b.getLeft();
            int buttonY = b.getTop();
            int buttonW = b.getWidth();
            int buttonH = b.getHeight();
            long evtTime = motionEvent.getEventTime();

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float motionX = motionEvent.getRawX();
                float motionY = motionEvent.getRawY();
                Log.v("EVT", String.format("bX: %d, bY: %d, mX: %f, mY: %f",
                        buttonX, buttonY, motionX, motionY));
                String buf = String.format("%d, %d, %d, %d, %f, %f\n",
                        buttonX, buttonY, buttonW, buttonH, motionX, motionY);
                try {
                    if (bw != null) {
                        bw.write(buf);
                    }
                } catch (Exception e) {

                }
            }
            return true;
        }

        return false;
    }
}
