package com.synopsis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ADMIN on 05-Aug-16.
 */
public class LastSlide extends android.support.v4.app.Fragment {
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        ViewGroup view=(ViewGroup)inflater.inflate(R.layout.last_slide,container,false);
        rootView = (ViewGroup) inflater.inflate(R.layout.empty, container, false);
        return rootView;
    }


}
