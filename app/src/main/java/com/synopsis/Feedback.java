package com.synopsis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Feedback extends AppCompatActivity {
    EditText edtfeedback;
    Button btnsubmit;
    ImageView back;
String adminemail;
    JSONArray success=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        back = (ImageView) findViewById(R.id.back);
        edtfeedback = (EditText) findViewById(R.id.edtfeedback);
        edtfeedback.setBackground(null);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtfeedback.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Feedback.this, "Please enter your feedback", Toast.LENGTH_LONG).show();
                } else {
//                    new FeedBackProcess().execute();
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("plain/text");
                    List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
                    if (!resInfo.isEmpty()) {
                        for (ResolveInfo info : resInfo) {
                            if (info.activityInfo.packageName.toLowerCase().contains("com.google.android.gm")) {
                                share.putExtra(Intent.EXTRA_EMAIL, new String[]{adminemail});
                                share.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
                                share.putExtra(Intent.EXTRA_TEXT, edtfeedback.getText().toString());
                                share.setPackage(info.activityInfo.packageName);
                                break;
                            }
                        }

                        startActivity(Intent.createChooser(share, "Select"));
                    }
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback.this.finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                new FeedBackProcess().execute();
            }
        }).start();

    }

    public class FeedBackProcess extends AsyncTask<String, String, String> {
        ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdia = new ProgressDialog(Feedback.this);
//            pdia.setMessage("Please wait...");
//            pdia.setCancelable(false);
//            pdia.show();
        }

        @Override
        protected String doInBackground(String... params) {


            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "wp-ad-access.php");
            try

            {
                // Getting Array of Employee
                success = json.getJSONArray("success");
//            newslength = news.length();
//            // looping of List
//            newslist.clear();
//            for (int i = 0; i < newslength; i++) {

                JSONObject c = success.getJSONObject(0);
                adminemail = c.getString("email");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            Intent i = new Intent(Intent.ACTION_SEND);
//            i.setType("message/rfc822");
//            i.putExtra(Intent.EXTRA_EMAIL, new String[]{adminemail});
//            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
//            i.putExtra(Intent.EXTRA_TEXT   ,edtfeedback.getText().toString() );
//            try {
//                startActivity(Intent.createChooser(i, "Send mail..."));
//            } catch (android.content.ActivityNotFoundException ex) {
//               // Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//            }
//            Intent share = new Intent(android.content.Intent.ACTION_SEND);
//            share.setType("plain/text");
//            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
//            if (!resInfo.isEmpty()) {
//                for (ResolveInfo info : resInfo) {
//                    if (info.activityInfo.packageName.toLowerCase().contains("com.google.android.gm")) {
//                        share.putExtra(Intent.EXTRA_EMAIL, new String[]{adminemail});
//                        share.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
//                        share.putExtra(Intent.EXTRA_TEXT, edtfeedback.getText().toString());
//                        share.setPackage(info.activityInfo.packageName);
//                        break;
//                    }
//                }
//
//                startActivity(Intent.createChooser(share, "Select"));
//            }
        }
    }
}
