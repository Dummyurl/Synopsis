package com.synopsis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ContactUs extends AppCompatActivity {
    ImageView imgmenu,progress;
    TextView txttwitter, txtfb, txtemail, txtphone;
    JSONArray success;
    String email, telephone, facebook_url, twitter_url;
    RelativeLayout rl, rl1, rl2, rlheader;
    Animation rotation;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        imgmenu = (ImageView) findViewById(R.id.imgmenu);
        txttwitter = (TextView) findViewById(R.id.txttwitter);
        txtfb = (TextView) findViewById(R.id.txtfb);
        txtphone = (TextView) findViewById(R.id.txtphone);
        txtemail = (TextView) findViewById(R.id.txtemail);
        rl = (RelativeLayout) findViewById(R.id.rl);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl2 = (RelativeLayout) findViewById(R.id.rl2);
        rlheader = (RelativeLayout) findViewById(R.id.rlheader);
        progress=(ImageView)findViewById(R.id.progress);
        rl.setVisibility(View.GONE);
        rl1.setVisibility(View.GONE);
        rl2.setVisibility(View.GONE);
        rotation = AnimationUtils.loadAnimation(ContactUs.this, R.anim.button_rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        rlheader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });
//        imgdivider.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        txtphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telephone));

                try {
                    startActivity(callIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        txtphone.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + telephone));
//
//                try {
//                    startActivity(callIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//        });
//        txtemail.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Intent share = new Intent(android.content.Intent.ACTION_SEND);
//                share.setType("plain/text");
//                List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
//                if (!resInfo.isEmpty()) {
//                    for (ResolveInfo info : resInfo) {
//                        if (info.activityInfo.packageName.toLowerCase().contains("com.google.android.gm")) {
//                            share.putExtra(Intent.EXTRA_TEXT, "");
//                            share.setPackage(info.activityInfo.packageName);
//                            break;
//                        }
//                    }
//
//                    startActivity(Intent.createChooser(share, "Select"));
//                }
//                return false;
//            }
//        });
        txtemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("plain/text");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                intent.putExtra(Intent.EXTRA_TEXT, "");
//                startActivity(intent);
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("plain/text");
                List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
                if (!resInfo.isEmpty()) {
                    for (ResolveInfo info : resInfo) {
                        if (info.activityInfo.packageName.toLowerCase().contains("com.google.android.gm")) {
                            share.putExtra(Intent.EXTRA_TEXT, "");
                            share.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                            share.setPackage(info.activityInfo.packageName);
                            break;
                        }
                    }

                    startActivity(Intent.createChooser(share, "Select"));
                }
            }
        });





//        txtemail.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("plain/text");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ashok.vantage@gmail.com"});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                intent.putExtra(Intent.EXTRA_TEXT, "");
//                startActivity(intent);
//                return true;
//            }
//        });
//        Intent in=new Intent(Intent.ACTION_CALL,Uri.parse("0000000000"));

        txttwitter.setClickable(true);
        txttwitter.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href=''> @thencrafts </a>";//http://www.google.com
        txttwitter.setText(Html.fromHtml(text));

        txtfb.setClickable(true);
        txtfb.setMovementMethod(LinkMovementMethod.getInstance());
        String textfb = "<a href=''>Formcra </a>";//http://www.google.com
        txtfb.setText(Html.fromHtml(text));

         sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            email = sharedpreferences.getString("Contactemail", "aiwahannah@yahoo.com");
            telephone = sharedpreferences.getString("Contactnumber", "+312425436546");
            txtemail.setText(email);
            txtphone.setText(telephone);
            rl.setVisibility(View.VISIBLE);
            rl1.setVisibility(View.VISIBLE);
            rl2.setVisibility(View.VISIBLE);

        } else {
            new Progress().execute();
        }



    }

    public class Progress extends AsyncTask<String, String, String> {
        ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress.startAnimation(rotation);
//            pdia = new ProgressDialog(ContactUs.this);
//            pdia.setMessage("Please wait few seconds...");
//            pdia.setCancelable(false);
//            pdia.show();
        }


        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
//        JSONObject json;//http://www.vantagewebtech.com/synopsis/api/wp-ad-access.php
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "wp-ad-access.php");
            try {
                // Getting Array of Employee
                success = json.getJSONArray("success");
                // looping of List
                for (int i = 0; i < success.length(); i++) {

                    JSONObject c = success.getJSONObject(i);

                    email = c.getString("email");
                    telephone = c.getString("telephone");
                    facebook_url = c.getString("facebook_url");
                    twitter_url = c.getString("twitter_url");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pdia.dismiss();
//            progress.clearAnimation();
//            progress.setVisibility(View.GONE);
            txtemail.setText(email);
            txtphone.setText(telephone);
            rl.setVisibility(View.VISIBLE);
            rl1.setVisibility(View.VISIBLE);
            rl2.setVisibility(View.VISIBLE);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Contactemail", email);
            editor.putString("Contactnumber", telephone);
            editor.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

//                    Log.i(LOG_TAG, "restart app");
//
//                    // restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//
                    isPhoneCalling = false;
                }

            }
        }
    }

}


