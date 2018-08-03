package com.android.gpstest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutHelp extends Fragment {

    protected static final String TAG = AboutHelp.class.getSimpleName();

    public static AboutHelp newInstance() {

        return new AboutHelp();
    }




    public AboutHelp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        L.i(TAG, "Запускаю фрагмент о программе и помощи");
        View v = inflater.inflate(R.layout.about, container,false);



        return  v;

    }


}
