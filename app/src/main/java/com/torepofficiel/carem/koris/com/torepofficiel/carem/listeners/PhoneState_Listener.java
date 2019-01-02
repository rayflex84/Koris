package com.torepofficiel.carem.koris.com.torepofficiel.carem.listeners;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.MainActivity;
import com.torepofficiel.carem.koris.R;

import java.lang.ref.WeakReference;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by macbook on 13/09/17.
 */

public class PhoneState_Listener extends PhoneStateListener {

    WeakReference<Context> mContext;
    private boolean error;
    public static final String SIM_STATE = "sim_state";

    public PhoneState_Listener(Context context, boolean error) {
        mContext = new WeakReference<Context>(context);
        this.error = error;
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        //Toast toast = null;
        if( error && ErrorActivity.currentActivity != null && serviceState.getState() != ServiceState.STATE_IN_SERVICE) {
            /*toast = Toast.makeText(mContext.get(), "hors ligne", Toast.LENGTH_LONG);
            toast.show();*/

            Intent intent = new Intent(mContext.get(), ErrorActivity.class);
            intent.putExtra(SIM_STATE, mContext.get().getString(R.string.sim_state));
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.get().startActivity(intent);
            //if(PhoneStateService.getInstance() != null) PhoneStateService.getInstance().stopSelf();
        }

        if(!error && serviceState.getState() == ServiceState.STATE_IN_SERVICE) {
            /*toast = Toast.makeText(mContext.get(), "hors ligne", Toast.LENGTH_LONG);
            toast.show();*/

            Class current_class = (ErrorActivity.currentActivity == null || ErrorActivity.currentActivity.get() == null) ? MainActivity.class
            : ErrorActivity.currentActivity.get().getClass();

            Intent intent = new Intent(mContext.get(), current_class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.get().startActivity(intent);
            //if(PhoneStateService.getInstance() != null) PhoneStateService.getInstance().stopSelf();
        }

    }

}
