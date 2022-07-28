package com.example.flashlight0;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ShakeDetection extends Service implements SensorEventListener {

    public final int MIN_TIME_BETWEEN_SHAKE = 300;
    public final int MAX_TIME_BETWEEN_SHAKES =800;
    private long lastShakeTime = 0;
    private int shakeCount = 0;
    private boolean isFlashOn = false;
    private final float SHAKE_THRESHOLD = 1.3f;
    CameraManager mCameraManager;
    String mCameraId;
    Utility utility;

    public ShakeDetection(){}

    @Override
    public void onCreate()
    {
        super.onCreate();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        utility = new Utility(this);

        if (sensorManager !=null)
        {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }

        mCameraManager= (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try
        {
            mCameraId=mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e)
        {
            extracted(e);
        }
    }

    private void extracted(CameraAccessException e)
    {
        e.printStackTrace();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        float x = sensorEvent.values[0];
        float gX=x/SensorManager.GRAVITY_EARTH;

        if(gX > SHAKE_THRESHOLD)
        {
            long currentTime = System.currentTimeMillis();

            if (lastShakeTime+MIN_TIME_BETWEEN_SHAKE>currentTime) {
                return;
            }
            if (lastShakeTime+MAX_TIME_BETWEEN_SHAKES<currentTime) {
                shakeCount = 0;
            }
            lastShakeTime=currentTime;
            shakeCount++;
            if (shakeCount==2){
                shakeCount=0;
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(300);
                try
                {
                    isFlashOn=utility.toggle(isFlashOn);
                } catch (CameraAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }
}
