package mymapstut.com.example.admin.myasync_tut.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by admin on 6/3/2017.
 */

public class NetworkHelper {
    //this class is made toc check network internet status
    public  static  boolean HasNetworkAcess(Context context){

        //1.make Obj conectivity manager get system service and the contect is connctivity
        //this gets state of network
        ConnectivityManager  cm =(ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //2.make Obj of network Info = cm getactive netowrk metheod
        try {
            NetworkInfo activenetwork = cm.getActiveNetworkInfo();
            //3 return activeNetwork with make sure its not null or connecting
            // this make sure we know the state and behavior of object
            return activenetwork != null && activenetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    };
}

