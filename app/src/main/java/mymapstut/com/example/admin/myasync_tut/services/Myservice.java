package mymapstut.com.example.admin.myasync_tut.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;



public class Myservice extends IntentService {
  //instance vars:

    public static final String TAG ="MyService";

    //these will be used as keys for listening and identifying in activity and one for msg
    public static final String MY_SERVICE_MSG  ="MY_SERVICE_MESSAGE";
    public static final String MY_SERVICE_PAYLOAD  ="My_SERVICE_PAYLOAD";


    //remeber register service in mainfest
    public Myservice() {
        super("My service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //get intent object from MainActivity
        Uri uri = intent.getData();

        //loging stuff
        Log.i(TAG, "HandleIntent " + uri.toString());

        //1.Intent Broadcast make intent method and pass in action myservice message
        //my service msg is the classifier that links with reciver
        Intent myIntentMessage = new Intent(MY_SERVICE_MSG);

        //2. pass payload as putExtra; Payload is the info
        myIntentMessage.putExtra(MY_SERVICE_PAYLOAD,"service all done bud");

        //3.now to send msg make instance of localbradcast manager with get instance method
        //use getappcontext or this
        LocalBroadcastManager manager = LocalBroadcastManager
                .getInstance(getApplicationContext());
        //4. send broadcast
        manager.sendBroadcast(myIntentMessage);


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
