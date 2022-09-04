package com.example.flashlight0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    boolean isSwitchOn= false;
    private boolean isBlink=false;

    Button flashButton, blinkButton;
    CameraManager cameraManager;
    SeekBar seekBar;
    TextView textView;
    String cameraID;

    long delay=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashButton=findViewById(R.id.flashButton);
        blinkButton = findViewById(R.id.blinkButton);
        seekBar = findViewById(R.id.seekBarDelay);
        textView = findViewById(R.id.textViewDelay);
        startService(new Intent(MainActivity.this,ShakeDetection.class));
        cameraManager= (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this,ShakeDetection.class);
        startService(i);

        blinkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isBlink = !isBlink;
                if (isBlink) blinkButton.setText("BLink Off");
                else  blinkButton.setText("Blink On");
                try {
                    blinkOn();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                delay=i;
                delay*=5;
                if(delay<20) delay=20;
                textView.setText(String.valueOf(delay));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void toggle(View view) throws CameraAccessException {


        if(isSwitchOn)
        {
            cameraManager.setTorchMode(cameraID,false);
            isSwitchOn=false;
        }
        else
        {
            cameraManager.setTorchMode(cameraID,true);
            isSwitchOn=true;
        }
    }
    
    public void blinkOn() throws  CameraAccessException {

        if(!isBlink) return;

        Timer pulse = new Timer();
        pulse.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    cameraManager.setTorchMode(cameraID,true);
                    blinkOff();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        },delay);
    }

    public void blinkOff() throws  CameraAccessException {

        //if(!isBlink) return;

        Timer pulse = new Timer();
        pulse.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    cameraManager.setTorchMode(cameraID,false);
                    blinkOn();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        },delay);
    }

    public void  stopBlinkFlashOn() throws CameraAccessException {
        isBlink=false;
        blinkOff();

    }
}