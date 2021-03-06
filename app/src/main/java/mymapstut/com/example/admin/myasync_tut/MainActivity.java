package mymapstut.com.example.admin.myasync_tut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mymapstut.com.example.admin.myasync_tut.models.DataItem;
import mymapstut.com.example.admin.myasync_tut.services.Myservice;
import mymapstut.com.example.admin.myasync_tut.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    //------------------------------------instance vars---------------------------------------------
    //Broadcast reciver arch vars: this recives the broadcat
    private BroadcastReceiver mBroadcastreciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 1.get string/dataitem[] of payload msg with this intent.getExta
            ///String message = intent.getStringExtra(Myservice.MY_SERVICE_PAYLOAD);--depricated
            //Intead of a string well get back a dataitem obj using parcable extra method with
            //myservice class payload, then iterate using a foreach
            if (intent.hasExtra(Myservice.MY_SERVICE_PAYLOAD)) {
                DataItem[] dataItems = (DataItem[])
                        intent.getParcelableArrayExtra(Myservice.MY_SERVICE_PAYLOAD);

                for (DataItem items : dataItems) {
                    output.append(items.getItemName() + "\n");

                }
            }
            else if (intent.hasExtra(Myservice.MY_SERVICE_EXEPTION)){
                String msg = intent.getStringExtra(Myservice.MY_SERVICE_EXEPTION);
                Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
            }
            //put in output
           // output.append(dataItems + "\n"); //depricated use for each loop above
            //2. make reciver in oncCreate method
        }
    };
    //----------------------
    private  boolean networkOk;
    private TextView output;
    private Button sendBtn;
    private Button clearBtn;
    public static final String JSON_URL ="http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    public static final String JSON_URL_SECURED= "http://560057.youcanlearnit.net/secured/json/itemsfeed.php";
    //------------------------------------instance vars---------------------------------------------


    @Override//---------------------onCreate method-------------------------------------------
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------Bradcast register and listner-----------//////
        //3.Get instange and register reciver link filter with
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastreciver,
                        new IntentFilter(Myservice.MY_SERVICE_MSG));

        //-------------Broadcast register------------------------//////


        sendBtn = (Button) findViewById(R.id.sendBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        output = (TextView) findViewById(R.id.resultBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  output.append("buton clicked \n");
                //make instace of my async task
                //then call execute method :you can only pass same type as generic.
               // MyAsyncTask task = new MyAsyncTask();
                //task.execute("String 1","String 2","String 3");
                ////////////////////////////////////////////////

                //AsycLoader instaniate getduppoartloade rmethod.
                //call initLoader method: params(0 = number of first loader
                //null = bundle argument dont know
                //third is the loader callback method = this
                // Try restartLoader method(real reload) or initLoader (no restart of thread bypass
                //do in background)
              //  getSupportLoaderManager().initLoader(0,null,MainActivity.this).forceLoad();
                ////////////////////////////////////////////////////////////////////////////

                /////----Intent service-------////////
                //1.Instantiate Intent Myservice is the second param instantiate servece
                //2.pass in data
                //3.Start the service with intent object
                if (networkOk) {
                    Intent intent = new Intent(MainActivity.this, Myservice.class);
                    intent.setData(Uri.parse(JSON_URL_SECURED));
                    startService(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Network not avail",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText("");
            }
        });

        //make sure network is okay
        networkOk = NetworkHelper.HasNetworkAcess(this);
        output.append("network avialible :" + networkOk + "\n");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastreciver);


    }
    ////////////////////////////////////////////----asyncTask Loader---////////////////////////////////

////////////////////////////////////////////----asyncTask Loader overrides---///////////////////////
    @Override//remeber change types default is Object.
    public Loader<String> onCreateLoader(int id, Bundle args) {
        //Create instance of MyTaskLoader
        output.append("creating async loader...\n");
        return new MyTaskLoader(this);
    }

    @Override// handles data retured from loader
    public void onLoadFinished(Loader<String> loader, String data) {
        output.append("AsyncLoader Finished : " + data + "\n");
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
////////////////////////////////////////////----asyncTask Loader overrides---/////////////////////

    //asyncTaskLoader in support repo of android
    private static class MyTaskLoader extends AsyncTaskLoader<String>{

        public MyTaskLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "from my loader";
        }

        @Override//can add extra shit this method comes after the load in background method
        public void deliverResult(String data) {
            data +="...data delivered";
            super.deliverResult(data);
        }
    }

////////////////////////////////////////////----asyncTask Loader ---///////////////////////////////
///Regular async task starts here:
//    needs parameters (params,progress,result) needs implemted methods
//    String is the type in abstract method and Void matches up with return.
//    Do in background method gets execute stings in array form
    private class MyAsyncTask extends AsyncTask<String,String,Void>{

        @Override
        protected Void doInBackground(String... params) {
            for (String index : params) {
                //publish progreess shares result with rest of app
                publishProgress(index);
                //thread sleep slows shit down for experimental purposes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            return null;
        }

        @Override// this method runs in the foreground
        //matches thes second String generic value in the <>
        //values will be passed in as an array
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            output.append(values[0] + "\n");
        }
    }
}
