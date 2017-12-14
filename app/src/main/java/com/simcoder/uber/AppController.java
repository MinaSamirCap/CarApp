package com.simcoder.uber;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mina on 16/10/17.
 */

public class AppController extends Application {

    private static AppController instance;

    public synchronized static AppController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        /*
        we could use the  android:fontFamily="monospace"
        but it is only supported from  min 16 api*/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")  /// to be the default font ..
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
