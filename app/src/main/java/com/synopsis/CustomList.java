package com.synopsis;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] cat_name;
        private final String[] cat_Id;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    final TypedArray testArrayIcon = getContext().getResources().obtainTypedArray(R.array.category_image);
    boolean nightmode;

    public CustomList(Activity context,
                      String[] name,String[] id) {
        super(context, R.layout.categories_listitem, name);
        this.context = context;
        this.cat_name = name;
        this.cat_Id = id;
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        nightmode = sharedpreferences.getBoolean("Nightmode", false);

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.categories_listitem, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.categories_name);
        RelativeLayout rl=(RelativeLayout)rowView.findViewById(R.id.rlcategory);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.categories_image);
        txtTitle.setText(cat_name[position]);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int cat_list_position = sharedpreferences.getInt("cat_list_position", 0);
         if(cat_list_position==position)
              {
              rl.setBackgroundResource(R.color.light_gray_header_color);
               }
           else
             {

            }

//        imageView.setImageResource(testArrayIcon.getResourceId(position, -1));
        imageView.setBackgroundResource(testArrayIcon.getResourceId(position,1));


        if(nightmode)
        {
            txtTitle.setTextColor(getContext().getResources().getColor(R.color.n_title));
        }
        else
        {
            txtTitle.setTextColor(getContext().getResources().getColor(R.color.d_title));
        }
        return rowView;
    }
}