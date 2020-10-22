package com.burhanrashid52.photoeditor.shakedetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeDetector implements SensorEventListener {
    private static final Float SHAKE_THRESHOLD_GRAVITY = 2F;
    private static final int SHAKE_SLOP_TIME_MS = 150;
    private OnShakeLisenter mListener;
    private long mShakeTimestamp = 0;

    public void setOnShakeListener(OnShakeLisenter onShakeListener) {
        this.mListener = onShakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Float x = event.values[0];
        Float y = event.values[1];
        Float z = event.values[2];

        Float gX = x / SensorManager.GRAVITY_EARTH;
        Float gY = y / SensorManager.GRAVITY_EARTH;
        Float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        Double gForce = Math.sqrt((gX * gX + gY * gY + gZ * gZ));
        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            long now = System.currentTimeMillis();
            // Bỏ qua các sự kiện lắc gần nhau (500ms)
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }
            mShakeTimestamp = now;
//            mListener.onShake();
            if (Round(x, 4) > 10.0000) {
                mListener.onShake(ShakeOri.RIGHT);
                Log.d("sensor", "X Right axis: " + x);

            } else if (Round(x, 4) < -10.0000) {
                mListener.onShake(ShakeOri.LEFT);
                Log.d("sensor", "X Left axis: " + x);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return;
    }

    public interface OnShakeLisenter {
        void onShake(ShakeOri ori);
    }

    public static float Round(float Rval, int Rpl) {
        float p = (float) Math.pow(10, Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float) tmp / p;
    }

}


