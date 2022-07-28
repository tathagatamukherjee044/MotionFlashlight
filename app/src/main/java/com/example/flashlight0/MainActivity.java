package com.example.flashlight0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean isSwitchOn= false;
    private boolean isBlink=false;

    Button button;
    CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        startService(new Intent(MainActivity.this,ShakeDetection.class));

        cameraManager= (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        Intent i = new Intent(this,ShakeDetection.class);
        startService(i);
    }

    public void toggle(View view) throws CameraAccessException {
        String cameraID = null;
        if (cameraManager != null)
        {
            cameraID=cameraManager.getCameraIdList()[0];
        }

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
}