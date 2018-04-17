package com.psi.index.singapore.sp.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.psi.index.singapore.sp.R;
import com.psi.index.singapore.sp.Services.BackgroundIntentService;

import java.security.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.Calendar.AM_PM;

public class MainController {
    private static final String DATE_FORMAT_WITHOUT_AMPM= "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_FORMAT_WITH_AM_PM = "yyyy-MM-dd'T'HH aaa";
    public static final String BASE_URL = "https://api.data.gov.sg/v1/environment/";


    public static void runServiceOneHours(AlarmManager am ,PendingIntent servicePendingIntent,Context context) {
        Calendar cal = Calendar.getInstance();
        long interval = 1000 * 60 * 60; // 60 minutes in milliseconds
        Intent serviceIntent = new Intent(context, BackgroundIntentService.class);
// make sure you **don't** use *PendingIntent.getBroadcast*, it wouldn't work
         servicePendingIntent =
                PendingIntent.getService(context,
                        BackgroundIntentService.SERVICE_ID, // integer constant used to identify the service
                        serviceIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        // FLAG to avoid creating a second service if there's already one running
// there are other options like setInexactRepeating, check the docs
        am.setRepeating(
                AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                cal.getTimeInMillis(),
                interval,
                servicePendingIntent
        );
    }
    public static  String getAddedDate(int addedDays){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, -1*addedDays);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_AMPM, Locale.ENGLISH);
        TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
        sdf.setTimeZone(tz);
        String strDate = sdf.format(c.getTime());
        return strDate;
    }
    public static String getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_AMPM, Locale.ENGLISH);
        final Date dates;
        try {
            dates = sdf.parse(date);
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(dates);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return getAddedDate(0);
    }
    public static String getHrMinute(String date_time){
     try{
         SimpleDateFormat sdf_sgt = new SimpleDateFormat(DATE_FORMAT_WITHOUT_AMPM, Locale.ENGLISH);
         final Date dates = sdf_sgt.parse(date_time);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITH_AM_PM, Locale.ENGLISH);
            String strDate = sdf.format(dates);
            String time= strDate.split("T") [1].trim().split(" ")[0].trim();
            return time;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    return date_time;
    }
    public static String getDateTimeInSGT(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_AMPM, Locale.ENGLISH);
        TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
        sdf.setTimeZone(tz);
        Date date= new Date();
        String strDate = sdf.format(date);
        System.out.println("Local in String format " + strDate);
        return strDate;

    }
    /**
     * Resize pin
     * */
    public static Bitmap getViewBitmap(View view)
    {
        try {
            //Get the dimensions of the view so we can re-layout the view at its current size
            //and create a bitmap of the same size
            int width = view.getWidth();
            int height = view.getHeight();
            int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            //Cause the view to re-layout
            view.measure(measuredWidth, measuredHeight);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            //Create a bitmap backed Canvas to draw the view into

            System.gc();
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.drawColor(Color.TRANSPARENT);
            //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
            view.draw(c);

            return b;

        }catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * set here the Global fontawesome
     * */
    public static Typeface fontawesome (Activity act){
        Typeface font = Typeface.createFromAsset(act.getAssets(), "fontawesome-webfont.ttf" );
        return font;
    }

    /**
     * Create custome UI for pin for Google map
     * */
    public static Bitmap getPintoSet(Context ctx ,String psi , String name){
        View view = LayoutInflater.from(ctx).inflate(R.layout.map_pin_view, null);
        TextView values = (TextView) view.findViewById(R.id.values);
        TextView texttitle = (TextView) view.findViewById(R.id.name);
        values.setText(psi);
        texttitle.setText(name);
        texttitle.setVisibility(View.VISIBLE);
        texttitle.setVisibility(View.VISIBLE);
        view.layout(0, 0, 250, 250);
        return getViewBitmap
                (view);
    }


    public static boolean isNetworkAvailable(Context ctx) {

        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (wifi.isWifiEnabled() && (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
        {
            return  true;
        } else if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }
}
