package com.torepofficiel.carem.koris.com.torepofficiel.carem.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.torepofficiel.carem.koris.RegistrationActivity;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.CheckingConnectionAsyncTask;


/**
 * Created by macbook on 26/08/17.
 */

public class NotifiyCnxStateReiceiver extends BroadcastReceiver {

    public static boolean etat;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {


            new CheckingConnectionAsyncTask(false).execute((Void) null);

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(RegistrationActivity.getInstance() != null) {

                        RegistrationActivity.getInstance().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RegistrationActivity.getInstance().checkingConnexionState(etat);
                            }
                        });

                    }
                }
            }, 5000);


        }

    }

}

//registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

