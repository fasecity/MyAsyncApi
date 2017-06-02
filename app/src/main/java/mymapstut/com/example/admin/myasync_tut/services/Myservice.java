package mymapstut.com.example.admin.myasync_tut.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by admin on 6/2/2017.
 */

public class Myservice extends IntentService {
    public static final String TAG ="MyService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *   Used to name the worker thread, important only for debugging.
     *             Modified taking Sting name out par
     *             remeber register service in mainfest
     */
    public Myservice() {
        super("My service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //get intent object from MainActivity
        Uri uri = intent.getData();
        //loging stuff
        Log.i(TAG, "HandleIntent" + uri.toString());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "OnCreateHandler");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "OnDestroy");

    }
}
