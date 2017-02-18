package com.example.bluesky.app;

import android.app.Application;
import android.content.SharedPreferences;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by bluesky on 16/6/17.
 */
public class ShoppingApplication extends Application {
   public static DbManager dbManager;
    public static SharedPreferences sp;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        DbManager.DaoConfig config = new DbManager.DaoConfig().setDbName("sky.db")
                .setAllowTransaction(true).setDbVersion(1);
        dbManager = x.getDb(config);

        sp= getSharedPreferences("loginInfo",MODE_PRIVATE);
    }


}
