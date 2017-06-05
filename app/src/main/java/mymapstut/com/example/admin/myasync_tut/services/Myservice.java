package mymapstut.com.example.admin.myasync_tut.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import mymapstut.com.example.admin.myasync_tut.models.DataItem;
import mymapstut.com.example.admin.myasync_tut.utils.HttpHelper;


public class Myservice extends IntentService {
  //--------------------------------------instance vars:--------------------------------------------

    public static final String TAG ="MyService";

    //these will be used as keys for listening and identifying in activity and one for msg
    public static final String MY_SERVICE_MSG  ="MY_SERVICE_MESSAGE";
    public static final String MY_SERVICE_PAYLOAD  ="My_SERVICE_PAYLOAD";
    public static final String MY_SERVICE_EXEPTION  ="MY_SERVICE_Exeption";



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
        ////----download url method--- PUt response in intent///
        String response;
        try {
            response =
                    HttpHelper.downloadUrl(uri.toString(),"nadias","NadiasPassword");
        } catch (IOException e) {
            e.printStackTrace();
            Intent myIntentMessage = new Intent(MY_SERVICE_MSG);
            myIntentMessage.putExtra(MY_SERVICE_EXEPTION,e.getMessage());

            LocalBroadcastManager manager = LocalBroadcastManager
                    .getInstance(getApplicationContext());
            //4. send broadcast
            manager.sendBroadcast(myIntentMessage);

            return;
        }
        //--------------------------------------gson----------------------------///////
        // transfer response using gson baby----
        //1.create gson class obj
        Gson gson = new Gson();

        //2. dececlare array of dataItems class
        //get the response url from above,and an array of the class you wanna parse to
        DataItem[] dataItems = gson.fromJson(response,DataItem[].class);
        //--------------------------------------gson----------------------------///////

        //----------------------------------
        ////----download url method---///

        //1.Intent Broadcast make intent method and pass in action myservice message
        //my service msg is the classifier that links with reciver
        Intent myIntentMessage = new Intent(MY_SERVICE_MSG);

        //2. pass payload as putExtra; Payload is the info send data Items insead of raw response
        myIntentMessage.putExtra(MY_SERVICE_PAYLOAD,dataItems);

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
