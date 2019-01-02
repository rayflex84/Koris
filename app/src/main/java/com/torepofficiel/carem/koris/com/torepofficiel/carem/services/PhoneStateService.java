package com.torepofficiel.carem.koris.com.torepofficiel.carem.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.torepofficiel.carem.koris.com.torepofficiel.carem.listeners.PhoneState_Listener;

import java.lang.ref.WeakReference;

public class PhoneStateService extends Service {

    private static WeakReference<PhoneStateService> phoneStateService;
    private Handler mHandler = new Handler();
    private Runnable myRunnable;
    public PhoneStateService() {
        phoneStateService = new WeakReference<>(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static PhoneStateService getInstance(){
        return (phoneStateService != null) ? phoneStateService.get() : null;
    }

    public void dismisHandler(){
        if(myRunnable != null)
            mHandler.removeCallbacks(myRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        myRunnable = new Runnable() {
            @Override
            public void run() {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                telephonyManager.listen(new PhoneState_Listener(PhoneStateService.this.getApplicationContext(), true),
                        PhoneStateListener.LISTEN_SERVICE_STATE);
            }
        };

        mHandler.post(myRunnable);

        return START_STICKY;
    }
}
