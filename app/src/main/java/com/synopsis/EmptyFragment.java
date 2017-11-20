package com.synopsis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ADMIN on 12-Jul-16.
 */
public class EmptyFragment extends Fragment {
    ViewGroup rootView;
    TextView txt;
    ImageView imgmenu;
    RelativeLayout rlheader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.empty, container, false);
        txt=(TextView)rootView.findViewById(R.id.txt);
        imgmenu = (ImageView)rootView.findViewById(R.id.imgmenu);
        rlheader=(RelativeLayout)rootView.findViewById(R.id.rlheader);
        Toast.makeText(getActivity(),"News not found in this category",Toast.LENGTH_LONG).show();
        getActivity().finish();
        Intent intent = new Intent(getActivity(), CategoryandSetting.class);
        startActivity(intent);

        rlheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryandSetting.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
