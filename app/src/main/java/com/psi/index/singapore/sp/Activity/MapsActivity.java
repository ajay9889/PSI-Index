package com.psi.index.singapore.sp.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.psi.index.singapore.sp.Adapter.PSIStatistic;
import com.psi.index.singapore.sp.DataModel.PSIDataResponse;
import com.psi.index.singapore.sp.DataModel.PsiTwentyFourHourly;
import com.psi.index.singapore.sp.DataModel.Readings;
import com.psi.index.singapore.sp.DataModel.RegionMetadata;
import com.psi.index.singapore.sp.NetworkCall.PSIRestAPI;
import com.psi.index.singapore.sp.R;
import com.psi.index.singapore.sp.Utils.MainController;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.psi.index.singapore.sp.Utils.MainController.getPintoSet;
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String ACTION_UPDATES_DATA = "com.psi.hourly.updates.receiving";
    public static final String ACTION_UPDATES_GOOGLE_MAP= "com.update.google.map.marker";
    private GoogleMap mMap;
    AlarmManager am = null;
    PendingIntent servicePendingIntent=null;
    IntentFilter mIntentFilter;
    RecyclerView mRecyclerView =null;
    LinkedHashMap<String ,LinkedHashMap<String, PSIDataResponse>> mMapPSIDataResponse =new LinkedHashMap<String ,LinkedHashMap<String, PSIDataResponse>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TextView refresh =(TextView) findViewById(R.id.refresh);
        refresh.setTypeface(MainController.fontawesome(MapsActivity.this));
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestPSIData();
            }
        });
        // Intent filter add here the action
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ACTION_UPDATES_DATA);
        mIntentFilter.addAction(ACTION_UPDATES_GOOGLE_MAP);
         mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // alarm manage to requets the API to update the PSI data automatically every one hours
        am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        /*
        * BackgroundIntentService, will run every 1 hour
        * Running the Service to fetch the NEA Api response automatically in background
        * **/
        MainController.runServiceOneHours(am ,servicePendingIntent,MapsActivity.this);
        RequestPSIData();
    }

    public void RequestPSIData(){
        try {
            TextView date_txt = (TextView) findViewById(R.id.date);
            TextView navigate = (TextView) findViewById(R.id.navigate);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.line_tables_data);
            navigate.setTypeface(MainController.fontawesome(MapsActivity.this));
            if (!MainController.isNetworkAvailable(MapsActivity.this)) {
                date_txt.setText(getResources().getString(R.string.network_error));
                navigate.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.GONE);
                return;
            }
            navigate.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            date_txt.setText(getResources().getString(R.string.please_wait));
            String date = MainController.getDateTimeInSGT();
            /*
             * Modified by ajay, previously was not clearing
             * */
            if(mMapPSIDataResponse!=null)
            mMapPSIDataResponse.clear();
            for (int i = 0; i < 5; i++) {
                String previous_days_hours = MainController.getAddedDate(i);
                if (MainController.getDate(date).equalsIgnoreCase(MainController.getDate(previous_days_hours))) {
                    NEARestAPI(previous_days_hours, MainController.getDate(previous_days_hours));
                } else {
                    break;
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }

    /*
     * Request fro PSI data using NEARestAPI
     * */

    public void NEARestAPI(final String date_time ,final String date) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainController.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        PSIRestAPI getPSIAPI = retrofit.create(PSIRestAPI.class);
        Call<PSIDataResponse> call = getPSIAPI. getPSIData(date_time,date);
        call.enqueue(new Callback<PSIDataResponse>() {
            @Override
            public void onResponse(Call<PSIDataResponse> call, Response<PSIDataResponse> response) {
                if(response.isSuccessful()) {
                    /*
                    * Received respons ehere
                    * */
                    PSIDataResponse mPSIDataResponse = response.body();
                    if(mPSIDataResponse.getmApiInfo().getStatus().equalsIgnoreCase("healthy")) {
                        LinkedHashMap<String, PSIDataResponse> map=null;
                        if(mMapPSIDataResponse.containsKey(date)){
                            map = mMapPSIDataResponse.get(date);
                            map.put(MainController.getHrMinute(date_time) , mPSIDataResponse);
                        }else{
                            map = new LinkedHashMap<String, PSIDataResponse>();
                            map.put(MainController.getHrMinute(date_time) , mPSIDataResponse);
                        }
                        mMapPSIDataResponse.put(date , map);
                        Intent intent = new Intent(ACTION_UPDATES_GOOGLE_MAP);
                        sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<PSIDataResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userLocation();
        registerReceiver(mBroadcast, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (mMap != null) {
                mMap.stopAnimation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(servicePendingIntent!=null && am!=null )
            am.cancel(servicePendingIntent);
            if(mBroadcast!=null)
            unregisterReceiver(mBroadcast);
            if(mMapPSIDataResponse!=null)
            mMapPSIDataResponse.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker when data is available
        setPinOnMap();
    }
    PSIStatistic mPSiStaticsAdapter=null;
    private void setPinOnMap(){
        try{
        if(mMap!=null && mMapPSIDataResponse!=null){
            mMap.clear();
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            // prepare the list at bottom
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            mRecyclerView.setLayoutManager(layout);
            ArrayList<String> mArrayList = new ArrayList<String>();

            for (LinkedHashMap.Entry<String ,LinkedHashMap<String, PSIDataResponse>> entry : mMapPSIDataResponse.entrySet()) {

                LinkedHashMap<String, PSIDataResponse> mPSIdataAtThatTime= entry.getValue();
                for (LinkedHashMap.Entry<String, PSIDataResponse> mPSIDataResponse : mPSIdataAtThatTime.entrySet()) {
                    PSIDataResponse mreadPSIDataResponse = mPSIDataResponse.getValue();
                    mArrayList.add(mPSIDataResponse.getKey());
                    Readings getmReading = mreadPSIDataResponse.getmItems().get(0).getmReading();
                    PsiTwentyFourHourly mPsiTwentyFourHourly= getmReading.getgPsiTwentyFourHourly();
                    List<RegionMetadata> mRegionMetadata = mPSIDataResponse.getValue().getmRegionMetadata();
                    createMarker(mRegionMetadata , mPsiTwentyFourHourly);
                }
                TextView date = (TextView) findViewById(R.id.date);
                TextView navigate = (TextView) findViewById(R.id.navigate);
                date.setText(entry.getKey());
                navigate.setTypeface(MainController.fontawesome(MapsActivity.this));
                navigate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestPSIData();
                    }
                });
                if(mPSiStaticsAdapter!=null){
                    mRecyclerView.removeAllViews();
                    mPSiStaticsAdapter=null;
                }
                 mPSiStaticsAdapter = new PSIStatistic(MapsActivity.this,mArrayList, mPSIdataAtThatTime , mMap);
                 mRecyclerView.setAdapter(mPSiStaticsAdapter);
            }
        }
        }catch(Exception e){e.printStackTrace();}
    }


    public void createMarker(List<RegionMetadata> mRegionMetadata,PsiTwentyFourHourly mPsiTwentyFourHourly){
        try{
        for(int i =0 ; i<mRegionMetadata.size() ; i++){
            LatLng PSI_location = new LatLng(mRegionMetadata.get(i).getmLocation().getLatitude(), mRegionMetadata.get(i).getmLocation().getLongitude());
            String label_on_pin="";
            switch(mRegionMetadata.get(i).getName()){
                case "east":
                    label_on_pin=String.valueOf(mPsiTwentyFourHourly.getEast());
                    break;
                case "west":
                    label_on_pin=String.valueOf(mPsiTwentyFourHourly.getWest());
                    break;
                case "north":
                    label_on_pin=String.valueOf(mPsiTwentyFourHourly.getNorth());
                    break;
                case "south":
                    label_on_pin=String.valueOf(mPsiTwentyFourHourly.getSouth());
                    break;
                case "central":
                    label_on_pin=String.valueOf(mPsiTwentyFourHourly.getCentral());
                    break;
                case "national":
                    label_on_pin=String.valueOf(mPsiTwentyFourHourly.getNational());
                    break;

            }
            // added the marker
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(getPintoSet(MapsActivity.this,label_on_pin,mRegionMetadata.get(i).getName()))).position(PSI_location));
            // moving camera to highlight the pin
            if(mRegionMetadata.get(i).getName().equalsIgnoreCase("south")){
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(PSI_location, 10);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(PSI_location));
                mMap.animateCamera(cameraUpdate);
            }
        }
        }catch(Exception e){e.printStackTrace();}
    }

    public boolean userLocation(){
        if (!(ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return false;
        }else if(mMap!=null){
            mMap.setMyLocationEnabled(true);
        }
        return true;
    }

    /*
    * Updating the UI after getting the new response from NEA rest API
    * */
    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase(ACTION_UPDATES_GOOGLE_MAP))
            {
                setPinOnMap();
            }else if(intent.getAction().equalsIgnoreCase(ACTION_UPDATES_DATA)){
                RequestPSIData();
            }
        }
    };

}
