package com.psi.index.singapore.sp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.psi.index.singapore.sp.DataModel.PSIDataResponse;
import com.psi.index.singapore.sp.DataModel.PsiTwentyFourHourly;
import com.psi.index.singapore.sp.DataModel.Readings;
import com.psi.index.singapore.sp.DataModel.RegionMetadata;
import com.psi.index.singapore.sp.R;
import com.psi.index.singapore.sp.Utils.MainController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.psi.index.singapore.sp.Utils.MainController.getPintoSet;

public class PSIStatistic extends RecyclerView.Adapter<RecyclerViewHolder> {
    private HashMap<String, PSIDataResponse> arrayList;
    private ArrayList<String> mArrayList=null;
    private Activity context;
    GoogleMap mMap;
    public PSIStatistic(Activity context, ArrayList<String> mArrayList, LinkedHashMap<String, PSIDataResponse> arrayList , GoogleMap mMap) {
        this.context = context;
        this.arrayList = arrayList;
        this.mArrayList = mArrayList;
        this.mMap =mMap;
        Log.d("mArrayList",mArrayList.toString());

    }
    public Object getElementByIndex(HashMap<String , PSIDataResponse> map, int index){
        return map.get(map.keySet().toArray()[index]);
    }
    @Override
    public int getItemCount() {
        return (null != mArrayList ? mArrayList.size() : 0);
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolder mainHolder,final int position) {
        try {
            String key =mArrayList.get(position);
            final PSIDataResponse mPSIDataResponse = arrayList.get(key);
            if(mPSIDataResponse!=null) {
                mainHolder.east.setText("" + mPSIDataResponse.getmItems().get(0).getmReading().getgPsiTwentyFourHourly().getEast());
                mainHolder.west.setText("" + mPSIDataResponse.getmItems().get(0).getmReading().getgPsiTwentyFourHourly().getWest());
                mainHolder.center.setText("" + mPSIDataResponse.getmItems().get(0).getmReading().getgPsiTwentyFourHourly().getCentral());
                mainHolder.north.setText("" + mPSIDataResponse.getmItems().get(0).getmReading().getgPsiTwentyFourHourly().getNorth());
                mainHolder.south.setText("" + mPSIDataResponse.getmItems().get(0).getmReading().getgPsiTwentyFourHourly().getSouth());
                mainHolder.time.setText(key.replace(" ",":00"));
                String date = MainController.getDateTimeInSGT();
                if(key.equalsIgnoreCase(MainController.getHrMinute(date))){
                    mainHolder.line_items.setBackgroundColor(context.getResources().getColor(R.color.background));
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.tables, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;
    }

}