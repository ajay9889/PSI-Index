package com.psi.index.singapore.sp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.psi.index.singapore.sp.Activity.MapsActivity;

/*      As the API is getting updated hourly from NEA.
        Readings are provided for each major region in Singapore
        */
public class BackgroundIntentService extends IntentService {
    public static final int SERVICE_ID =1001;
    public BackgroundIntentService() {
        super("BackgroundIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // fetch the Retrofit API
        Intent  intents =new Intent(MapsActivity.ACTION_UPDATES_DATA);
        sendBroadcast(intents);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }


}
