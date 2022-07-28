package com.example.flashlight0;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

public class Utility
{
    Context context;
    public Utility(Context context)
    {
        this.context = context;
    }

    public boolean toggle(boolean isSwitchOn) throws CameraAccessException
    {
        CameraManager cameraManager= (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
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
        return isSwitchOn;
    }
}
