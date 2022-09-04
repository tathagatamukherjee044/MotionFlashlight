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

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    boolean isSwitchOn= false;
    private boolean isBlink=false;

    Button flashButton, blinkButton;
    CameraManager cameraManager;
    String cameraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashButton=findViewById(R.id.flashButton);
        blinkButton = findViewById(R.id.blinkButton);
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
                try {
                    blinkOn();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
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
        },1000);
    }

    public void blinkOff() throws  CameraAccessException {

        if(!isBlink) return;

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
        },1000);
    }
}