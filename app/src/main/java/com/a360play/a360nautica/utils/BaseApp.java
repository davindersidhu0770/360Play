package com.a360play.a360nautica.utils;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        init();
    }

    /**
     * Connect print service through interface library
     */
    private void init(){
        SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
    }
}
