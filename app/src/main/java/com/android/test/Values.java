package com.android.gpstest;


import java.util.Calendar;
import java.util.Date;

public class Values {

    public static final String TAG = Values.class.getSimpleName();

    private String Values1, Values2, Values3;
    private String time;
    private String values;


    public Values () {

    }

    public Values (String time, String Values1, String Values2, String Values3){
//        time = String.valueOf(Calendar.getInstance().getTime());
        this.time = time;
        this.Values1 = Values1;
        this.Values2 = Values2;
        this.Values3 = Values3;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }


    public void setValues1(String values1) {
        Values1 = values1;
    }

    public String getValues1() {
        return Values1;
    }

    public void setValues2(String values2) {
        Values2 = values2;
    }

    public String getValues2() {
        return Values2;
    }

    public void setValues3(String values3) {
        Values3 = values3;
    }

    public String getValues3() {
        return Values3;
    }

}
