package com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires;

import android.app.Application;
import android.content.Context;

/**
 * Created by macbook on 12/09/17.
 */

public class RetrieveMyApplicationContext extends Application {


//Don't forget to mention RetriveMyApplicationContext Class in Manifests File otherwise it will throw NullPointer Exception
// <application
// android:name=".volley.RetriveMyApplicationContext"


    private static RetrieveMyApplicationContext mRetrieveMyApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mRetrieveMyApplicationContext = this;
    }

    public static RetrieveMyApplicationContext getInstance() {

        return mRetrieveMyApplicationContext;
    }

    public static Context getAppContext() {

        return mRetrieveMyApplicationContext.getApplicationContext();
    }
}
