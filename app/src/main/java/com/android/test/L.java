package com.android.gpstest;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.gpstest.MainActivity.TAG;


public final class L {

    public static String LOG_TAG = TAG;


    private static L instance;

    public L() {
        instance = this ;
    }



    public static Map<String, Integer> mapLevelColors;
    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("VERBOSE", R.color.Gray_Goose);
        map.put("DEBUG", R.color.Green_Peas);
        map.put("INFO", R.color.Blue_Ivy);
        map.put("WARNING", R.color.Corn_Yellow);
        map.put("ERROR", R.color.Lava_Red);
        mapLevelColors = Collections.unmodifiableMap(map);
    }

    public static Map<String, Integer> mapClassColors;
    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(TAG, R.color.Shamrock_Green);
        map.put(TAG, R.color.Blue_Ivy);
        map.put(TestMurka.TAG, R.color.Cranberry);

        mapClassColors = Collections.unmodifiableMap(map);
    }

    public static void e(String message, Throwable cause) {
        Log.e(LOG_TAG, "[" + message + "]", cause);
    }

    public static void e(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.e(LOG_TAG, "[" + callerClassName + "] " + msg);
    }

    public static void w(String message, Throwable cause) {
        Log.w(LOG_TAG, "[" + message + "]", cause);
    }

    public static void w(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.w(LOG_TAG, "[" + callerClassName + "] " + msg);
    }

    public static void i(String message, Throwable cause) {
        int i = Log.i(LOG_TAG, "[" + message + "]", cause);
    }


    public static void i(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.i(LOG_TAG, "[" + callerClassName + "] " + msg);
    }

    public static void d(String msg, Throwable cause) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, msg, cause);
        }
    }


    public static void d(String logTag, String msg) {
        if (BuildConfig.DEBUG) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();

            String callerClassName = elements[1].getFileName();
            L.d(LOG_TAG, "[" + callerClassName + "] " + msg);
        }
    }

    public static void v(String msg, Throwable cause) {
        if (BuildConfig.DEBUG) {
            Log.v(LOG_TAG, msg, cause);
        }
    }


    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();

            String callerClassName = elements[1].getFileName();
            Log.v(LOG_TAG, "[" + callerClassName + "] " + msg);
        }
    }

    public static void v(String TAG, String msg) {
        Log.v(TAG, msg);
//        final DataBaseHelper db = DataBaseHelper.getInstance(MainActivity.getContext());
//        db.log(getTime(), TAG, msg);

        showLog(TAG, "VERBOSE", msg);
    }


//    public static void d(String TAG1, String msg) {
//        Log.d(TAG, msg);
//        final DataBaseHelper db = DataBaseHelper.getInstance(MainActivity.getContext());
//        db.log(getTime(), TAG, msg);
//
//        showLog(TAG, "DEBUG", msg);
//    }


    public static void i(String TAG, String msg) {

//        try {
            Log.i(TAG, msg);
//            final DataBaseHelper db = DataBaseHelper.getInstance(MainActivity.getContext());
//            db.log(getTime(), TAG, msg);
//
//            showLog(TAG, "INFO", msg);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        Log.i(TAG, msg);

        showLog(TAG, "INFO", msg);
    }


    public static void w(String TAG, String msg) {
        try {
            Log.w(LOG_TAG, msg);
//            final DataBaseHelper db = DataBaseHelper.getInstance(MainActivity.getContext());
//            db.log(getTime(), TAG, msg);

            showLog(TAG, "WARNING", msg);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void e(String TAG, String msg) {
        Log.e(LOG_TAG, msg);

//        final DataBaseHelper db = DataBaseHelper.getInstance(MainActivity.getContext());
//        db.log(getTime(), TAG, msg);

        showLog(TAG, "ERROR", msg);
    }

    public static void showLog(String TAG, String LEVEL, final String msg){



        try {
            final TextView tvNum = (TextView) ((AppCompatActivity) MainActivity.getInstance()).findViewById(R.id.log_view);
            String header = " :: [" + TAG + "] " + getTime() + " [" + LEVEL + "] :: " + "\r\n";
            int fgColor = ContextCompat.getColor(MainActivity.getInstance(), mapLevelColors.get(LEVEL));
            System.out.println(TAG+"_________________");
//            int bgColor = ContextCompat.getColor(MainActivity.getContext(), mapClassColors.get(TAG));

            final SpannableString spanedHeader = new SpannableString(header);
            spanedHeader.setSpan(new StyleSpan(Typeface.BOLD), 6, TAG.length()+8, 0);
            spanedHeader.setSpan(new StyleSpan(Typeface.BOLD), header.length()-LEVEL.length()-8, header.length() , 0);
            spanedHeader.setSpan(new TypefaceSpan("monospace"), 4, header.length()-6, 0);
            spanedHeader.setSpan(new ForegroundColorSpan(fgColor), header.length()-LEVEL.length()-8, header.length(), 0);
//            spanedHeader.setSpan(new BackgroundColorSpan(bgColor), 0, 4, 0);

            final SpannableString spanedBody = new SpannableString("    " + msg + "\r\n");
//            spanedBody.setSpan(new BackgroundColorSpan(bgColor), 0, 4, 0);

            ((AppCompatActivity) MainActivity.getInstance()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tvNum != null) tvNum.append(TextUtils.concat(spanedHeader, spanedBody));
                }
            });
        } catch (Exception e ){
            e.printStackTrace();
        }

    }

    public static String getTime() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }

    public static String getDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }




}
