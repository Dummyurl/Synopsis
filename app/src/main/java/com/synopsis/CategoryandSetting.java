package com.synopsis;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class CategoryandSetting extends AppCompatActivity implements View.OnClickListener {
    TextView txtaccount, txtlanguage, txtenglish, txtnotification, txthdimages, txtnightmode, txtinvitefriend, txtrateapp, txtfeedback, txthowtowork;
    Button btncategories, btnsettings;
    View viewcategories, viewsettings;
    ListView categorieslist;
    RelativeLayout rlsetting, rlbottom, rlaccount, rlallaccount, rllanguage, rlnotification, rlhdimage, rlnightmode, rlinvitefriend, rlrate, rlfeedback, rlhowitwork;
    SwitchCompat notificationswitch, hdimageswitch, nightmodeswitch;
    ImageView imgaccount, imglanguage;
    String CatId, CateName;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String account_type, email, profile_image;
    int Acc_LoginId;
    ImageView imgfb, imggplush, imgtwitter;
    TextView txtappaccountname, txtfbaccountname, txtgplushaccountname, txttwitteraccountname;
    RelativeLayout rlheader, cat_close;
    RelativeLayout rlcategory;
    Drawable d = null;
    CircleImageView profile_imagefacebook, profile_imagetwitter, profile_imagegplush;
    Button fb_logout, twitter_logout, app_logout, g_logout;
    RelativeLayout rlconnectapp, rlconnectfb, rlconnectgplush, rlconnecttwitter;
    RelativeLayout rlimgtwitter, rlimgfb, rlimggplush;
    TextView txt_twitter, txt_facebook, txt_gooleplush, txt_app;
    boolean internet = true, nightmode, notification, hdimage;
    Cursor c;
    PopupWindow popupWindoww;
    View layout;
    int POSITION;
    DBHelper mydb;
    String NewsId = "", NewsTitle = "", NewsSummary = "", NewsVideo = "", NewsImage = "", NewsDate = "", News_source = "", news_like, News_url, Share_url, published_by = "", readtype, cattype, related_category;
    Boolean like_status;
    JSONArray news = null;
    String Insert_DateTime;
    RelativeLayout rootLayout;
    byte[] imagebitmap;
    String[] playlists;
    int releted_categoryid;
    String all = "false", trending = "false", topstory = "false", local = "false", international = "false", business = "false", politics = "false", sports = "false", property = "false", technology = "false", entertainment = "false", movies = "false", health = "false", lifestyle = "false", trivia = "false", jobs = "false", from_notification = "false";

    //String Insert_DateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categoryand_setting);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
//        overridePendingTransition(R.anim.enter, R.anim.exit);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        btncategories = (Button) findViewById(R.id.btncategories);
        btnsettings = (Button) findViewById(R.id.btnsettings);
        viewcategories = findViewById(R.id.viewcategories);
        viewsettings = findViewById(R.id.viewsetting);

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        viewsettings.setBackgroundColor(getResources().getColor(R.color.colorbrown));
        viewcategories.setBackgroundColor(getResources().getColor(R.color.colorred));
        btncategories.setBackgroundDrawable(null);
        btnsettings.setBackgroundDrawable(null);

//        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        nightmode = sharedpreferences.getBoolean("Nightmode", false);
//        if(nightmode)
//        {
//            btncategories.setTextColor(getResources().getColor(R.color.n_title));
//            btnsettings.setTextColor(getResources().getColor(R.color.colorblack));
//        }
//        else
//        {
//            btncategories.setTextColor(getResources().getColor(R.color.colorblack));
//            btnsettings.setTextColor(getResources().getColor(R.color.colorbrown));
//        }

        categorieslist = (ListView) findViewById(R.id.categories_list);
        rlsetting = (RelativeLayout) findViewById(R.id.rlsettings);
        rlbottom = (RelativeLayout) findViewById(R.id.rlbottom);

        rlaccount = (RelativeLayout) findViewById(R.id.rlaccount);
        rlallaccount = (RelativeLayout) findViewById(R.id.rlallaccount);
        rllanguage = (RelativeLayout) findViewById(R.id.rllanguage);
        rlnotification = (RelativeLayout) findViewById(R.id.rlnotification);
        rlhdimage = (RelativeLayout) findViewById(R.id.rlhdimage);
        rlnightmode = (RelativeLayout) findViewById(R.id.rlnightmode);
        rlinvitefriend = (RelativeLayout) findViewById(R.id.rlinvitefriend);
        rlrate = (RelativeLayout) findViewById(R.id.rlrate);
        rlfeedback = (RelativeLayout) findViewById(R.id.rlfeedback);
        rlhowitwork = (RelativeLayout) findViewById(R.id.rlhowitwork);
        rlconnectfb = (RelativeLayout) findViewById(R.id.rlconnectfb);
        rlcategory = (RelativeLayout) findViewById(R.id.rlcategory);
        imgaccount = (ImageView) findViewById(R.id.imgaccount);
        imglanguage = (ImageView) findViewById(R.id.imgchangelanguage);
        cat_close = (RelativeLayout) findViewById(R.id.cat_close);
        rlheader = (RelativeLayout) findViewById(R.id.rlheader);
        rlheader.setBackgroundColor(Color.argb(200, 255, 255, 255));
        imgfb = (ImageView) findViewById(R.id.imgfb);
        imggplush = (ImageView) findViewById(R.id.imggplush);
        imgtwitter = (ImageView) findViewById(R.id.imgtwitter);
        txtappaccountname = (TextView) findViewById(R.id.txtappaccountname);
        txtfbaccountname = (TextView) findViewById(R.id.txtfbaccountname);
        txtgplushaccountname = (TextView) findViewById(R.id.txtgplushaccountname);
        txttwitteraccountname = (TextView) findViewById(R.id.txttwitteraccountname);
        fb_logout = (Button) findViewById(R.id.btnfblogout);
        twitter_logout = (Button) findViewById(R.id.btntwitterlogout);
        app_logout = (Button) findViewById(R.id.btnemaillogout);
        g_logout = (Button) findViewById(R.id.btngplushlogout);

        txtaccount = (TextView) findViewById(R.id.txtaccount);
        txtlanguage = (TextView) findViewById(R.id.txtlanguage);
        txtenglish = (TextView) findViewById(R.id.txtenglish);
        txtnotification = (TextView) findViewById(R.id.txtnotification);
        txthdimages = (TextView) findViewById(R.id.txthdimages);
        txtnightmode = (TextView) findViewById(R.id.txtnightmode);
        txtinvitefriend = (TextView) findViewById(R.id.txtinvitefriend);
        txtrateapp = (TextView) findViewById(R.id.txtrateapp);
        txtfeedback = (TextView) findViewById(R.id.txtfeedback);
        txthowtowork = (TextView) findViewById(R.id.txthowtowork);


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            internet = false;

        }
//        ScreenSlidePagerActivity.GoCategory = false;
        LayoutInflater inflaterr = getLayoutInflater();
        layout = inflaterr.inflate(R.layout.smallcustom_progressbar, null);
        popupWindoww = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btncategories.setOnClickListener(this);
        btnsettings.setOnClickListener(this);
        rlaccount.setOnClickListener(this);
        rlhowitwork.setOnClickListener(this);
        rlrate.setOnClickListener(this);
        rlinvitefriend.setOnClickListener(this);
        rlfeedback.setOnClickListener(this);
        rlconnectfb.setOnClickListener(this);
        fb_logout.setOnClickListener(this);
        twitter_logout.setOnClickListener(this);
        app_logout.setOnClickListener(this);
        g_logout.setOnClickListener(this);
        txtfbaccountname.setOnClickListener(this);
        txtappaccountname.setOnClickListener(this);
        txtgplushaccountname.setOnClickListener(this);
        txttwitteraccountname.setOnClickListener(this);

        cat_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CategoryandSetting.this.finish();
                return false;
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        nightmode = sharedpreferences.getBoolean("Nightmode", false);
        notification = sharedpreferences.getBoolean("Notification", false);
        hdimage = sharedpreferences.getBoolean("HDImage", false);

        notificationswitch = (SwitchCompat) findViewById(R.id.notificationswitch);
        notificationswitch.setSwitchPadding(40);
        hdimageswitch = (SwitchCompat) findViewById(R.id.hdimageswitch);
        hdimageswitch.setSwitchPadding(40);
        nightmodeswitch = (SwitchCompat) findViewById(R.id.nightmodeswitch);
        nightmodeswitch.setSwitchPadding(40);


        if (nightmode) {
            nightmodeswitch.setChecked(true);
            rootLayout.setBackgroundColor(getResources().getColor(R.color.n_background));
            btncategories.setTextColor(getResources().getColor(R.color.n_title));
            btnsettings.setTextColor(getResources().getColor(R.color.colorblack));
        } else {
            nightmodeswitch.setChecked(false);
            rootLayout.setBackgroundColor(getResources().getColor(R.color.d_background));
            btncategories.setTextColor(getResources().getColor(R.color.colorblack));
            btnsettings.setTextColor(getResources().getColor(R.color.colorbrown));
        }

        if (notification) {
            notificationswitch.setChecked(true);
        } else {
            notificationswitch.setChecked(false);
        }

        if (hdimage) {
            hdimageswitch.setChecked(true);
        } else {
            hdimageswitch.setChecked(false);
        }

        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(CategoryandSetting.this, "" + isChecked, Toast.LENGTH_LONG).show();
                if (isChecked) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("Notification", true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("Notification", false);
                    editor.commit();
                }
            }
        });


        hdimageswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(CategoryandSetting.this, "" + isChecked, Toast.LENGTH_LONG).show();
                if (isChecked) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("HDImage", true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("HDImage", false);
                    editor.commit();
                }
            }
        });


        nightmodeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if (isChecked) {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    editor.putBoolean("Nightmode", true);
                    rootLayout.setBackgroundColor(getResources().getColor(R.color.n_background));
                    CustomList adapter = new
                            CustomList(CategoryandSetting.this, getResources().getStringArray(R.array.category_name), getResources().getStringArray(R.array.category_id));
                    categorieslist.setAdapter(adapter);
                    Utils.setListViewHeightBasedOnChildren(categorieslist);

                    txtaccount.setTextColor(getResources().getColor(R.color.n_title));
                    txtaccount.setTextColor(getResources().getColor(R.color.n_title));
                    txtlanguage.setTextColor(getResources().getColor(R.color.n_title));
                    txtenglish.setTextColor(getResources().getColor(R.color.n_title));
                    txtnotification.setTextColor(getResources().getColor(R.color.n_title));
                    txthdimages.setTextColor(getResources().getColor(R.color.n_title));
                    txtnightmode.setTextColor(getResources().getColor(R.color.n_title));
                    txtinvitefriend.setTextColor(getResources().getColor(R.color.n_title));
                    txtrateapp.setTextColor(getResources().getColor(R.color.n_title));
                    txtfeedback.setTextColor(getResources().getColor(R.color.n_title));
                    txthowtowork.setTextColor(getResources().getColor(R.color.n_title));
                    btncategories.setTextColor(getResources().getColor(R.color.colorblack));
                    btnsettings.setTextColor(getResources().getColor(R.color.n_title));

                    txtappaccountname.setTextColor(getResources().getColor(R.color.n_title));
                    txtfbaccountname.setTextColor(getResources().getColor(R.color.n_title));
                    txtgplushaccountname.setTextColor(getResources().getColor(R.color.n_title));
                    txttwitteraccountname.setTextColor(getResources().getColor(R.color.n_title));
                    app_logout.setTextColor(getResources().getColor(R.color.n_title));
                    fb_logout.setTextColor(getResources().getColor(R.color.n_title));
                    g_logout.setTextColor(getResources().getColor(R.color.n_title));
                    twitter_logout.setTextColor(getResources().getColor(R.color.n_title));

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("Nightmode", false);
                    rootLayout.setBackgroundColor(getResources().getColor(R.color.d_background));


                    txtaccount.setTextColor(getResources().getColor(R.color.d_title));
                    txtaccount.setTextColor(getResources().getColor(R.color.d_title));
                    txtlanguage.setTextColor(getResources().getColor(R.color.d_title));
                    txtenglish.setTextColor(getResources().getColor(R.color.d_title));
                    txtnotification.setTextColor(getResources().getColor(R.color.d_title));
                    txthdimages.setTextColor(getResources().getColor(R.color.d_title));
                    txtnightmode.setTextColor(getResources().getColor(R.color.d_title));
                    txtinvitefriend.setTextColor(getResources().getColor(R.color.d_title));
                    txtrateapp.setTextColor(getResources().getColor(R.color.d_title));
                    txtfeedback.setTextColor(getResources().getColor(R.color.d_title));
                    txthowtowork.setTextColor(getResources().getColor(R.color.d_title));
                    btncategories.setTextColor(getResources().getColor(R.color.colorbrown));
                    btnsettings.setTextColor(getResources().getColor(R.color.colorblack));
                    txtappaccountname.setTextColor(getResources().getColor(R.color.d_title));
                    txtfbaccountname.setTextColor(getResources().getColor(R.color.d_title));
                    txtgplushaccountname.setTextColor(getResources().getColor(R.color.d_title));
                    txttwitteraccountname.setTextColor(getResources().getColor(R.color.d_title));
                    app_logout.setTextColor(getResources().getColor(R.color.d_title));
                    fb_logout.setTextColor(getResources().getColor(R.color.d_title));
                    g_logout.setTextColor(getResources().getColor(R.color.d_title));
                    twitter_logout.setTextColor(getResources().getColor(R.color.d_title));
                }
                editor.commit();
            }
        });
        CustomList adapter = new
                CustomList(CategoryandSetting.this, getResources().getStringArray(R.array.category_name), getResources().getStringArray(R.array.category_id));
        categorieslist.setAdapter(adapter);
        Utils.setListViewHeightBasedOnChildren(categorieslist);
        categorieslist.setEnabled(true);
        btnsettings.setEnabled(true);
        categorieslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                  @Override
                                                  public void onItemClick(AdapterView<?> parent, View view,
                                                                          int position, long id) {

                                                      if (ScreenSlidePageFragment.t1 != null) {
                                                          ScreenSlidePageFragment.t1.stop();
                                                          ScreenSlidePageFragment.isChecked = true;
                                                      }


                                                      btnsettings.setEnabled(false);
                                                      categorieslist.setEnabled(false);
                                                      ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                                                      if (!cd.isConnectingToInternet()) {
                                                          internet = false;

                                                      } else {
                                                          internet = true;
                                                      }

                                                      CatId = getResources().getStringArray(R.array.category_id)[position];
                                                      CateName = getResources().getStringArray(R.array.category_name)[position];

                                                      if (CateName.equalsIgnoreCase("Contact us")) {

                                                          Intent in = new Intent(CategoryandSetting.this, ContactUs.class);
                                                          startActivity(in);
                                                          btnsettings.setEnabled(true);
                                                          categorieslist.setEnabled(true);
//                                                            }

                                                      } else {
                                                          SharedPreferences.Editor editor = sharedpreferences.edit();
                                                          editor.putString("CatId", CatId);
                                                          editor.putString("CateName", CateName);
                                                          editor.putInt("cat_list_position", position);
                                                          editor.commit();
                                                          POSITION = position;
                                                          ScreenSlidePagerActivity.execute = true;
                                                          mydb = new DBHelper(CategoryandSetting.this);

                                                          if (position == 0) {
                                                              c = mydb.getallnewsData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 1) {
                                                              c = mydb.gettrendingData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 2) {
                                                              c = mydb.gettopstoryData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 3) {
                                                              ScreenSlidePagerActivity.imgrefresh.setVisibility(View.GONE);
//                                                                Splash_Page.newslist.clear();
                                                              mydb = new DBHelper(CategoryandSetting.this);

                                                              c = mydb.getbookmarkData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  Toast.makeText(CategoryandSetting.this, "No bookmark news !", Toast.LENGTH_SHORT).show();
                                                                  categorieslist.setEnabled(true);
                                                                  btnsettings.setEnabled(true);
                                                              } else {
                                                                  MethodexecuteBookmark();
                                                              }

                                                          } else if (position == 4) {
                                                              c = mydb.getunreadData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength != 0) {
                                                                  Methodunreadexecute();
                                                                  Splash_Page.newslength = Splash_Page.newslist.size();
                                                              }
                                                              if (Splash_Page.newslength == 0) {
                                                                  Toast.makeText(CategoryandSetting.this, "No unread news !", Toast.LENGTH_SHORT).show();
                                                                  categorieslist.setEnabled(true);
                                                                  btnsettings.setEnabled(true);
                                                              } else {
                                                                  Intent in = new Intent(CategoryandSetting.this, ScreenSlidePagerActivity.class);
                                                                  startActivity(in);
                                                                  categorieslist.setEnabled(true);
                                                                  btnsettings.setEnabled(true);
                                                              }


                                                          } else if (position == 5) {
                                                              c = mydb.getlocalData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 6) {
                                                              c = mydb.getinternationalData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 7) {
                                                              c = mydb.getbusinessandfinanceData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 8) {
                                                              c = mydb.getpoliticsData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 9) {
                                                              c = mydb.getsportsData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 10) {
                                                              c = mydb.getpropertyData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 11) {
                                                              c = mydb.gettechnologyData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 12) {
                                                              c = mydb.getentertainmentData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 13) {
                                                              c = mydb.getmoviesData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 14) {
                                                              c = mydb.gethealthData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 15) {
                                                              c = mydb.getlifestyleData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 16) {
                                                              c = mydb.gettriviaData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          } else if (position == 17) {
                                                              c = mydb.getjobsData();
                                                              Splash_Page.newslength = c.getCount();
                                                              if (Splash_Page.newslength == 0) {
                                                                  if (internet) {
                                                                      new CategoryNewsProgress().execute();
                                                                  } else {
                                                                      Toast.makeText(CategoryandSetting.this, "No offline news !", Toast.LENGTH_SHORT).show();
                                                                      categorieslist.setEnabled(true);
                                                                      btnsettings.setEnabled(true);
                                                                  }
                                                              } else {
                                                                  Methodexecute();
                                                              }
                                                          }
                                                      }
                                                  }
                                              }
        );

    }

    public class CategoryNewsProgress extends AsyncTask<String, String, String> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            popupWindoww.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 0);
            popupWindoww.setFocusable(true);
            popupWindoww.update();
            sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            CateName = sharedpreferences.getString("CateName", "");
            CatId = sharedpreferences.getString("CatId", "");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Insert_DateTime = sdf.format(c.getTime());
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "category.php?cid=" + CatId + "&location=" + Splash_Page.country_name + "&device_id=" + Splash_Page.android_id);// + "&page=" + 1
            try {
                news = json.getJSONArray("news");
            } catch (Exception e) {

            }

            Splash_Page.newslength = news.length();
            // looping of List
            if (Splash_Page.newslength != 0)
                Splash_Page.newslist.clear();
            for (int i = 0; i < Splash_Page.newslength; i++) {
                try {
                    JSONObject c = news.getJSONObject(i);
                    NewsId = c.getString("id");
                    NewsTitle = c.getString("title");
                    NewsSummary = c.getString("summary");
                    NewsVideo = c.getString("video");
                    NewsImage = c.getString("image");
                    NewsDate = c.getString("post_date");
//                    NewsDate=NewsDate.substring(0,NewsDate.indexOf(" "));
                    News_source = c.getString("news_source");
                    news_like = c.getString("news_like");
                    like_status = c.getBoolean("like_status");
                    News_url = c.getString("news_url");
                    Share_url = c.getString("share_url");
                    published_by = c.getString("published_by");
                    related_category = c.getString("related_category");
                    playlists = related_category.split(",");


                    for (int j = 0; j < playlists.length; j++) {
                        releted_categoryid = Integer.parseInt(playlists[j]);

                        if (releted_categoryid == 4) {
                            trending = "true";
                        } else if (releted_categoryid == 3) {
                            topstory = "true";
                        } else if (releted_categoryid == 2) {
                            local = "true";
                        } else if (releted_categoryid == 5) {
                            international = "true";
                        } else if (releted_categoryid == 6) {
                            business = "true";
                        } else if (releted_categoryid == 7) {
                            politics = "true";
                        } else if (releted_categoryid == 8) {
                            sports = "true";
                        } else if (releted_categoryid == 9) {
                            property = "true";
                        } else if (releted_categoryid == 10) {
                            technology = "true";
                        } else if (releted_categoryid == 199) {
                            entertainment = "true";
                        } else if (releted_categoryid == 200) {
                            movies = "true";
                        } else if (releted_categoryid == 201) {
                            health = "true";
                        } else if (releted_categoryid == 202) {
                            lifestyle = "true";
                        } else if (releted_categoryid == 203) {
                            trivia = "true";
                        } else if (releted_categoryid == 204) {
                            jobs = "true";
                        }
                    }
                    all = "true";
                    from_notification = "true";
                    String convert="false";
                    mydb.insertall(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "All News", "Unread", Insert_DateTime, bitmap, all, trending, topstory, local, international, business, politics, sports, property, technology, entertainment, movies, health, lifestyle, trivia, jobs, from_notification,convert);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            popupWindoww.dismiss();
            if (Splash_Page.newslength == 0) {
                categorieslist.setEnabled(true);
                btnsettings.setEnabled(true);
                Toast.makeText(CategoryandSetting.this, "News not found in this category", Toast.LENGTH_LONG).show();
            } else {
                mydb = new DBHelper(CategoryandSetting.this);
                if (POSITION == 0) {
                    c = mydb.getallnewsData();
                } else if (POSITION == 1) {
                    c = mydb.gettrendingData();
                } else if (POSITION == 2) {
                    c = mydb.gettopstoryData();
                } else if (POSITION == 3) {
                    c = mydb.getbookmarkData();
                } else if (POSITION == 4) {
                    c = mydb.getunreadData();
                } else if (POSITION == 5) {
                    c = mydb.getlocalData();
                } else if (POSITION == 6) {
                    c = mydb.getinternationalData();
                } else if (POSITION == 7) {
                    c = mydb.getbusinessandfinanceData();
                } else if (POSITION == 8) {
                    c = mydb.getpoliticsData();
                } else if (POSITION == 9) {
                    c = mydb.getsportsData();
                } else if (POSITION == 10) {
                    c = mydb.getpropertyData();
                } else if (POSITION == 11) {
                    c = mydb.gettechnologyData();
                } else if (POSITION == 12) {
                    c = mydb.getentertainmentData();
                } else if (POSITION == 13) {
                    c = mydb.getmoviesData();
                } else if (POSITION == 14) {
                    c = mydb.gethealthData();
                } else if (POSITION == 15) {
                    c = mydb.getlifestyleData();
                } else if (POSITION == 16) {
                    c = mydb.gettriviaData();
                } else if (POSITION == 17) {
                    c = mydb.getjobsData();
                }
                Methodexecute();

            }
        }
    }

    public void Methodexecute() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("firstscreen", true);
        editor.putBoolean("second", true);
        editor.commit();
        mydb = new DBHelper(CategoryandSetting.this);
        if (POSITION == 0) {
            c = mydb.getallnewsData();
        } else if (POSITION == 1) {
            c = mydb.gettrendingData();
        } else if (POSITION == 2) {
            c = mydb.gettopstoryData();
        } else if (POSITION == 3) {
            c = mydb.getbookmarkData();
        } else if (POSITION == 4) {
            c = mydb.getunreadData();
        } else if (POSITION == 5) {
            c = mydb.getlocalData();
        } else if (POSITION == 6) {
            c = mydb.getinternationalData();
        } else if (POSITION == 7) {
            c = mydb.getbusinessandfinanceData();
        } else if (POSITION == 8) {
            c = mydb.getpoliticsData();
        } else if (POSITION == 9) {
            c = mydb.getsportsData();
        } else if (POSITION == 10) {
            c = mydb.getpropertyData();
        } else if (POSITION == 11) {
            c = mydb.gettechnologyData();
        } else if (POSITION == 12) {
            c = mydb.getentertainmentData();
        } else if (POSITION == 13) {
            c = mydb.getmoviesData();
        } else if (POSITION == 14) {
            c = mydb.gethealthData();
        } else if (POSITION == 15) {
            c = mydb.getlifestyleData();
        } else if (POSITION == 16) {
            c = mydb.gettriviaData();
        } else if (POSITION == 17) {
            c = mydb.getjobsData();
        }

        if (c != null) {
            Splash_Page.newslist.clear();
            c.moveToFirst();
        }
        do {
            try {
                NewsId = c.getString(c.getColumnIndex("newsid"));
                NewsTitle = c.getString(c.getColumnIndex("newstitle"));
                NewsSummary = c.getString(c.getColumnIndex("newssummary"));
                NewsVideo = c.getString(c.getColumnIndex("newsvideo"));
                NewsImage = c.getString(c.getColumnIndex("newsimageurl"));
                NewsDate = c.getString(c.getColumnIndex("newsdate"));
                News_source = c.getString(c.getColumnIndex("newssource"));
                news_like = c.getString(c.getColumnIndex("newslike"));
                int ab = c.getInt(c.getColumnIndex("likestatus"));
                if (ab == 1) {
                    like_status = true;
                } else {
                    like_status = false;
                }
                News_url = c.getString(c.getColumnIndex("newsurl"));
                Share_url = c.getString(c.getColumnIndex("shareurl"));
                published_by = c.getString(c.getColumnIndex("published_by"));
                cattype = c.getString(c.getColumnIndex("category"));
                imagebitmap = c.getBlob(c.getColumnIndex("newsimage"));
                Bitmap bitmap = null;

                ////////***********************for offline image
//                if (NewsImage.equalsIgnoreCase("")) {
//
//                } else {
//                    if (imagebitmap == null) {
//
//                    } else {
//                        try {
//                            bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                        } catch (OutOfMemoryError e) {
//                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                            bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
//                            bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
//                        }
//                    }
//
//                }

                Holder h = new Holder();


                h.setNewsId(NewsId);
                h.setNewsTitle(NewsTitle);
                h.setNewsSummary(NewsSummary);
                h.setNewsVideo(NewsVideo);
                h.setNewsImage(NewsImage);
                h.setNewsDate(NewsDate);
                h.setNews_source(News_source);
                h.setNews_url(News_url);
                h.setNews_like(news_like);
                h.setShare_url(Share_url);
                h.setLike_status(like_status);
                h.setPublished_by(published_by);
                h.setCattype(cattype);
//                h.setNewsbitmap(bitmap);
                h.setImagebitmap(imagebitmap);
                Splash_Page.newslist.add(h);
            } catch (Exception e) {

            }
        } while (c.moveToNext());
//        if (POSITION == 0) {
//            if (Splash_Page.newslist.size() != 0) {
//            } else {
////                Splash_Page.newslist.clear();
////                Splash_Page.newslist.addAll(Splash_Page.allnewslist);
//                Splash_Page.newslist.removeAll(Splash_Page.allnewslist);
//            }
//        }


        Intent in = new Intent(CategoryandSetting.this, ScreenSlidePagerActivity.class);
        startActivity(in);
        CategoryandSetting.this.finish();
    }

    public void go() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("firstscreen", true);
        editor.putBoolean("second", true);
        editor.commit();
        Intent in = new Intent(CategoryandSetting.this, ScreenSlidePagerActivity.class);
        startActivity(in);
        CategoryandSetting.this.finish();
    }

    public void MethodexecuteBookmark() {
//        Splash_Page.newslist.clear();
//        Splash_Page.newslist.clear();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("firstscreen", true);
        editor.putBoolean("second", true);
        editor.commit();
        if (c != null) {
            Splash_Page.newslist.clear();
            c.moveToLast();
        }
        do {
            NewsId = c.getString(c.getColumnIndex("newsid"));
            NewsTitle = c.getString(c.getColumnIndex("newstitle"));
            NewsSummary = c.getString(c.getColumnIndex("newssummary"));
            NewsVideo = c.getString(c.getColumnIndex("newsvideo"));
            NewsImage = c.getString(c.getColumnIndex("newsimageurl"));
            NewsDate = c.getString(c.getColumnIndex("newsdate"));
            News_source = c.getString(c.getColumnIndex("newssource"));
            news_like = c.getString(c.getColumnIndex("newslike"));
            int ab = c.getInt(c.getColumnIndex("likestatus"));
            if (ab == 1) {
                like_status = true;
            } else {
                like_status = false;
            }
//            like_status = Boolean.parseBoolean(c.getString(c.getColumnIndex("likestatus")));
            News_url = c.getString(c.getColumnIndex("newsurl"));
            Share_url = c.getString(c.getColumnIndex("shareurl"));
            published_by = c.getString(c.getColumnIndex("published_by"));
            cattype = c.getString(c.getColumnIndex("category"));
            imagebitmap = c.getBlob(c.getColumnIndex("newsimage"));
            Bitmap bitmap = null;
            ////////***********************for offline image
//            if (NewsImage.equalsIgnoreCase("")) {
//
//            } else {
//                if (imagebitmap == null) {
//
//                } else {
//                    bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                }
//            }

            Holder h = new Holder();


            h.setNewsId(NewsId);
            h.setNewsTitle(NewsTitle);
            h.setNewsSummary(NewsSummary);
            h.setNewsVideo(NewsVideo);
            h.setNewsImage(NewsImage);
            h.setNewsDate(NewsDate);
            h.setNews_source(News_source);
            h.setNews_url(News_url);
            h.setNews_like(news_like);
            h.setShare_url(Share_url);
            h.setLike_status(like_status);
            h.setPublished_by(published_by);
            h.setCattype(cattype);
//            h.setNewsbitmap(bitmap);
            h.setImagebitmap(imagebitmap);
            Splash_Page.newslist.add(h);
        } while (c.moveToPrevious());
        Intent in = new Intent(CategoryandSetting.this, ScreenSlidePagerActivity.class);
        startActivity(in);
        CategoryandSetting.this.finish();
    }

    public void Methodunreadexecute() {
        Splash_Page.newslist.clear();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("firstscreen", true);
        editor.putBoolean("second", true);
        editor.commit();
        Bitmap bitmap = null;
//        Splash_Page.newslist.clear();
        if (c != null) {
            Splash_Page.newslist.clear();
            c.moveToFirst();
        }
        do {

            NewsId = c.getString(c.getColumnIndex("newsid"));
            NewsTitle = c.getString(c.getColumnIndex("newstitle"));
            NewsSummary = c.getString(c.getColumnIndex("newssummary"));
            NewsVideo = c.getString(c.getColumnIndex("newsvideo"));
            NewsImage = c.getString(c.getColumnIndex("newsimageurl"));
            NewsDate = c.getString(c.getColumnIndex("newsdate"));
            News_source = c.getString(c.getColumnIndex("newssource"));
            news_like = c.getString(c.getColumnIndex("newslike"));
            like_status = Boolean.parseBoolean(c.getString(c.getColumnIndex("likestatus")));
            News_url = c.getString(c.getColumnIndex("newsurl"));
            Share_url = c.getString(c.getColumnIndex("shareurl"));
            published_by = c.getString(c.getColumnIndex("published_by"));
            readtype = c.getString(c.getColumnIndex("readtype"));
            cattype = c.getString(c.getColumnIndex("category"));
            imagebitmap = c.getBlob(c.getColumnIndex("newsimage"));
            if (readtype.equalsIgnoreCase("read")) {

            } else {

//                if (NewsImage.equalsIgnoreCase("")) {
//
//                } else {
//                    if (imagebitmap == null) {
//
//                    } else {
//                        try {
//                            bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                        } catch (OutOfMemoryError e) {
//                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                            bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
//                            bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
//                        }
//                    }
//                }

                Holder h = new Holder();


                h.setNewsId(NewsId);
                h.setNewsTitle(NewsTitle);
                h.setNewsSummary(NewsSummary);
                h.setNewsVideo(NewsVideo);
                h.setNewsImage(NewsImage);
                h.setNewsDate(NewsDate);
                h.setNews_source(News_source);
                h.setNews_url(News_url);
                h.setShare_url(Share_url);
                h.setNews_like(news_like);
                h.setLike_status(like_status);
                h.setPublished_by(published_by);
                h.setCattype(cattype);
//                h.setNewsbitmap(bitmap);
                h.setImagebitmap(imagebitmap);
                Splash_Page.newslist.add(h);
            }
        } while (c.moveToNext());
    }

    public static class Utils {

        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                int currentapiVersion1 = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion1 <= 18) {
//                    listItem.measure(10,10);
                    totalHeight += 80;
                } else {
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }

            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btncategories:
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                nightmode = sharedpreferences.getBoolean("Nightmode", false);
                if (nightmode) {
                    btncategories.setTextColor(getResources().getColor(R.color.n_title));
                    btnsettings.setTextColor(getResources().getColor(R.color.colorblack));
                } else {
                    btncategories.setTextColor(getResources().getColor(R.color.colorblack));
                    btnsettings.setTextColor(getResources().getColor(R.color.colorbrown));
                }
                viewsettings.setBackgroundColor(getResources().getColor(R.color.colorbrown));
                viewcategories.setBackgroundColor(getResources().getColor(R.color.colorred));


                categorieslist.setVisibility(View.VISIBLE);
                rlsetting.setVisibility(View.GONE);
                CustomList adapter = new
                        CustomList(CategoryandSetting.this, getResources().getStringArray(R.array.category_name), getResources().getStringArray(R.array.category_id));
                categorieslist.setAdapter(adapter);
                Utils.setListViewHeightBasedOnChildren(categorieslist);
                break;
            case R.id.btnsettings:

                viewsettings.setBackgroundColor(getResources().getColor(R.color.colorred));
                viewcategories.setBackgroundColor(getResources().getColor(R.color.colorbrown));

                btncategories.setTextColor(getResources().getColor(R.color.colorbrown));
                btnsettings.setTextColor(getResources().getColor(R.color.colorblack));
                categorieslist.setVisibility(View.GONE);
                rlsetting.setVisibility(View.VISIBLE);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                nightmode = sharedpreferences.getBoolean("Nightmode", false);
                if (nightmode) {
                    txtaccount.setTextColor(getResources().getColor(R.color.n_title));
                    txtaccount.setTextColor(getResources().getColor(R.color.n_title));
                    txtlanguage.setTextColor(getResources().getColor(R.color.n_title));
                    txtenglish.setTextColor(getResources().getColor(R.color.n_title));
                    txtnotification.setTextColor(getResources().getColor(R.color.n_title));
                    txthdimages.setTextColor(getResources().getColor(R.color.n_title));
                    txtnightmode.setTextColor(getResources().getColor(R.color.n_title));
                    txtinvitefriend.setTextColor(getResources().getColor(R.color.n_title));
                    txtrateapp.setTextColor(getResources().getColor(R.color.n_title));
                    txtfeedback.setTextColor(getResources().getColor(R.color.n_title));
                    txthowtowork.setTextColor(getResources().getColor(R.color.n_title));

                    btncategories.setTextColor(getResources().getColor(R.color.colorblack));
                    btnsettings.setTextColor(getResources().getColor(R.color.n_title));
                    txtappaccountname.setTextColor(getResources().getColor(R.color.n_title));
                    txtfbaccountname.setTextColor(getResources().getColor(R.color.n_title));
                    txtgplushaccountname.setTextColor(getResources().getColor(R.color.n_title));
                    txttwitteraccountname.setTextColor(getResources().getColor(R.color.n_title));
                    app_logout.setTextColor(getResources().getColor(R.color.n_title));
                    fb_logout.setTextColor(getResources().getColor(R.color.n_title));
                    g_logout.setTextColor(getResources().getColor(R.color.n_title));
                    twitter_logout.setTextColor(getResources().getColor(R.color.n_title));
                } else {
                    txtaccount.setTextColor(getResources().getColor(R.color.d_title));
                    txtaccount.setTextColor(getResources().getColor(R.color.d_title));
                    txtlanguage.setTextColor(getResources().getColor(R.color.d_title));
                    txtenglish.setTextColor(getResources().getColor(R.color.d_title));
                    txtnotification.setTextColor(getResources().getColor(R.color.d_title));
                    txthdimages.setTextColor(getResources().getColor(R.color.d_title));
                    txtnightmode.setTextColor(getResources().getColor(R.color.d_title));
                    txtinvitefriend.setTextColor(getResources().getColor(R.color.d_title));
                    txtrateapp.setTextColor(getResources().getColor(R.color.d_title));
                    txtfeedback.setTextColor(getResources().getColor(R.color.d_title));
                    txthowtowork.setTextColor(getResources().getColor(R.color.d_title));

                    btncategories.setTextColor(getResources().getColor(R.color.colorbrown));
                    btnsettings.setTextColor(getResources().getColor(R.color.colorblack));
                    txtappaccountname.setTextColor(getResources().getColor(R.color.d_title));
                    txtfbaccountname.setTextColor(getResources().getColor(R.color.d_title));
                    txtgplushaccountname.setTextColor(getResources().getColor(R.color.d_title));
                    txttwitteraccountname.setTextColor(getResources().getColor(R.color.d_title));
                    app_logout.setTextColor(getResources().getColor(R.color.d_title));
                    fb_logout.setTextColor(getResources().getColor(R.color.d_title));
                    g_logout.setTextColor(getResources().getColor(R.color.d_title));
                    twitter_logout.setTextColor(getResources().getColor(R.color.d_title));
                }

                break;
            case R.id.rlaccount:
                rlconnectapp = (RelativeLayout) findViewById(R.id.rlconnectapp);
                rlconnectfb = (RelativeLayout) findViewById(R.id.rlconnectfb);
                rlconnectgplush = (RelativeLayout) findViewById(R.id.rlconnectgplush);
                rlconnecttwitter = (RelativeLayout) findViewById(R.id.rlconnecttwitter);
                rlimgtwitter = (RelativeLayout) findViewById(R.id.rlimgtwitter);
                rlimgfb = (RelativeLayout) findViewById(R.id.rlimgfb);
                rlimggplush = (RelativeLayout) findViewById(R.id.rlimggplush);
                txt_app = (TextView) findViewById(R.id.txtappaccountname);
                txt_facebook = (TextView) findViewById(R.id.txtfbaccountname);
                txt_gooleplush = (TextView) findViewById(R.id.txtgplushaccountname);
                txt_twitter = (TextView) findViewById(R.id.txttwitteraccountname);
                profile_imagefacebook = (CircleImageView) findViewById(R.id.profile_imagefacebook);
                profile_imagetwitter = (CircleImageView) findViewById(R.id.profile_imagetwitter);
                profile_imagegplush = (CircleImageView) findViewById(R.id.profile_imagegplush);

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                account_type = sharedpreferences.getString("LoginType", "");
                Acc_LoginId = sharedpreferences.getInt("LoginId", 0);
                email = sharedpreferences.getString("email", "");
                profile_image = sharedpreferences.getString("profile_image", "");

                if (rlallaccount.getVisibility() == View.VISIBLE) {
                    // Its visible
                    rlallaccount.setVisibility(View.GONE);
                    imgaccount.setBackgroundResource(R.drawable.expendsarrow);
                } else {
                    rlallaccount.setVisibility(View.VISIBLE);
                    imgaccount.setBackgroundResource(R.drawable.collepsarrow);
                    if (Acc_LoginId != 0) {
                        if (account_type.equalsIgnoreCase("app")) {
                            rlconnectapp.setVisibility(View.VISIBLE);
                            rlconnectfb.setVisibility(View.GONE);
                            rlconnectgplush.setVisibility(View.GONE);
                            rlconnecttwitter.setVisibility(View.GONE);
                            txt_app.setText(email);
                            app_logout.setVisibility(View.VISIBLE);
                        } else if (account_type.equalsIgnoreCase("facebook")) {
                            rlconnectapp.setVisibility(View.GONE);
                            rlconnectfb.setVisibility(View.VISIBLE);
                            rlconnectgplush.setVisibility(View.GONE);
                            rlconnecttwitter.setVisibility(View.GONE);
                            rlimgfb.setVisibility(View.GONE);
                            txt_facebook.setText(email);
                            fb_logout.setVisibility(View.VISIBLE);
                            new ProfileLoad().execute();

                        } else if (account_type.equalsIgnoreCase("gplush")) {
                            rlconnectapp.setVisibility(View.GONE);
                            rlconnectfb.setVisibility(View.GONE);
                            rlconnectgplush.setVisibility(View.VISIBLE);
                            rlconnecttwitter.setVisibility(View.GONE);
                            rlimggplush.setVisibility(View.GONE);
                            txt_gooleplush.setText(email);
                            g_logout.setVisibility(View.VISIBLE);
                            new ProfileLoad().execute();

                        } else if (account_type.equalsIgnoreCase("twitter")) {

                            rlconnectapp.setVisibility(View.GONE);
                            rlconnectfb.setVisibility(View.GONE);
                            rlconnectgplush.setVisibility(View.GONE);
                            rlconnecttwitter.setVisibility(View.VISIBLE);
                            rlimgtwitter.setVisibility(View.GONE);
                            txt_twitter.setText(email);
                            twitter_logout.setVisibility(View.VISIBLE);
                            new ProfileLoad().execute();
                        }
                    } else {
                        rlconnectapp.setVisibility(View.VISIBLE);
                        rlconnectfb.setVisibility(View.VISIBLE);
                        rlconnectgplush.setVisibility(View.VISIBLE);
                        rlconnecttwitter.setVisibility(View.VISIBLE);
                        fb_logout.setVisibility(View.GONE);
                        twitter_logout.setVisibility(View.GONE);
                        app_logout.setVisibility(View.GONE);
                        g_logout.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.rlconnectfb:

                break;
            case R.id.rlhowitwork:
                Intent in = new Intent(CategoryandSetting.this, InstructionsPage.class);
                startActivity(in);
                break;
            case R.id.rlrate:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {   //https://play.google.com/store?hl=en
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store?hl=en")));//market://details?id="+"com.in.puyangun
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store?hl=en")));//https://play.google.com/store/apps/details?id=com.in.puyangun
                }
                break;
            case R.id.rlinvitefriend:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                i.putExtra(Intent.EXTRA_SUBJECT, "Synopsis News");
                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store?hl=en");
                i.setType("text/plain");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(CategoryandSetting.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
//                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Synopsis News");
//                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store?hl=en");//https://play.google.com/store/apps/details?id=com.in.puyangun
//                final PackageManager pm = getPackageManager();
//                final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
//                ResolveInfo best = null;
//                for (final ResolveInfo info : matches)
//                    if (info.activityInfo.packageName.endsWith(".gm") ||
//                            info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
//                if (best != null)
//                    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
//                startActivity(intent);

                break;
            case R.id.rlfeedback:

                Intent inten = new Intent(CategoryandSetting.this, Feedback.class);
                startActivity(inten);
                break;
            case R.id.btnemaillogout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("LoginId", 0);
                editor.putString("email", "");
                editor.commit();
                rlconnectapp.setVisibility(View.VISIBLE);
                rlconnectfb.setVisibility(View.VISIBLE);
                rlconnectgplush.setVisibility(View.VISIBLE);
                rlconnecttwitter.setVisibility(View.VISIBLE);
//                rlimga.setVisibility(View.VISIBLE);
                txt_app.setText("Connect with app");
                fb_logout.setVisibility(View.GONE);
                twitter_logout.setVisibility(View.GONE);
                app_logout.setVisibility(View.GONE);
                g_logout.setVisibility(View.GONE);
//                profile_imagea.setVisibility(View.GONE);
                break;
            case R.id.btnfblogout:
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                editor1.putInt("LoginId", 0);
                editor1.putString("email", "");
                editor1.commit();
                rlconnectapp.setVisibility(View.VISIBLE);
                rlconnectfb.setVisibility(View.VISIBLE);
                rlconnectgplush.setVisibility(View.VISIBLE);
                rlconnecttwitter.setVisibility(View.VISIBLE);
                rlimgfb.setVisibility(View.VISIBLE);
                txt_facebook.setText("Connect with facebook");
                profile_imagefacebook.setBackground(null);
                fb_logout.setVisibility(View.GONE);
                twitter_logout.setVisibility(View.GONE);
                app_logout.setVisibility(View.GONE);
                g_logout.setVisibility(View.GONE);
                break;
            case R.id.btngplushlogout:
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                        new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(Status status) {
////                        tv_username.setText("");
//                            }
//                        });
                SharedPreferences.Editor editor2 = sharedpreferences.edit();
                editor2.putInt("LoginId", 0);
                editor2.putString("email", "");
                editor2.commit();
                rlconnectapp.setVisibility(View.VISIBLE);
                rlconnectfb.setVisibility(View.VISIBLE);
                rlconnectgplush.setVisibility(View.VISIBLE);
                rlconnecttwitter.setVisibility(View.VISIBLE);
                rlimggplush.setVisibility(View.VISIBLE);
                txt_gooleplush.setText("Connect with google");
                profile_imagegplush.setBackground(null);
                fb_logout.setVisibility(View.GONE);
                twitter_logout.setVisibility(View.GONE);
                app_logout.setVisibility(View.GONE);
                g_logout.setVisibility(View.GONE);
                break;
            case R.id.btntwitterlogout:
                SharedPreferences.Editor editor3 = sharedpreferences.edit();
                editor3.putInt("LoginId", 0);
                editor3.putString("email", "");
                editor3.commit();
                rlconnectapp.setVisibility(View.VISIBLE);
                rlconnectfb.setVisibility(View.VISIBLE);
                rlconnectgplush.setVisibility(View.VISIBLE);
                rlconnecttwitter.setVisibility(View.VISIBLE);
                rlimgtwitter.setVisibility(View.VISIBLE);
                txt_twitter.setText("Connect with twitter");
                profile_imagetwitter.setBackground(null);
                fb_logout.setVisibility(View.GONE);
                twitter_logout.setVisibility(View.GONE);
                app_logout.setVisibility(View.GONE);
                g_logout.setVisibility(View.GONE);
                break;
            case R.id.txtappaccountname:
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int LoginIde = sharedpreferences.getInt("LoginId", 0);
                if (LoginIde == 0) {
                    Intent intent = new Intent(CategoryandSetting.this, SignInPage.class);
                    startActivity(intent);
                    rlallaccount.setVisibility(View.GONE);
                } else {

                }

                break;
            case R.id.txtfbaccountname:
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int LoginId = sharedpreferences.getInt("LoginId", 0);
                if (LoginId == 0) {
                    Intent intent1 = new Intent(CategoryandSetting.this, SignInPage.class);
                    startActivity(intent1);
                    rlallaccount.setVisibility(View.GONE);
                } else {

                }

                break;
            case R.id.txttwitteraccountname:
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int LoginIdd = sharedpreferences.getInt("LoginId", 0);
                if (LoginIdd == 0) {
                    Intent intent2 = new Intent(CategoryandSetting.this, SignInPage.class);
                    startActivity(intent2);
                    rlallaccount.setVisibility(View.GONE);
                } else {

                }


                break;
            case R.id.txtgplushaccountname:
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int LoginIds = sharedpreferences.getInt("LoginId", 0);
                if (LoginIds == 0) {

                    Intent intent3 = new Intent(CategoryandSetting.this, SignInPage.class);
                    startActivity(intent3);
                    rlallaccount.setVisibility(View.GONE);
                } else {

                }


                break;
        }
    }

    public class ProfileLoad extends AsyncTask<String, String, String> {
        Bitmap myBitmap;

        @Override
        protected String doInBackground(String... params) {
            if (profile_image.equalsIgnoreCase("")) {

            } else {
                try {
                    URL url = new URL(profile_image);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(30000);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream input = connection.getInputStream();

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    // opts.inJustDecodeBounds = true;
                    opts.inSampleSize = 2;
                    myBitmap = BitmapFactory.decodeStream(input, null, opts);
                    connection.disconnect();


                } catch (IOException e) {

                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (profile_image.equalsIgnoreCase("")) {

            } else {
                d = new BitmapDrawable(getResources(), myBitmap);
                if (account_type.equalsIgnoreCase("app")) {
                } else if (account_type.equalsIgnoreCase("facebook")) {
                    profile_imagefacebook.setBackground(d);
                } else if (account_type.equalsIgnoreCase("gplush")) {
                    profile_imagegplush.setBackground(d);
                } else if (account_type.equalsIgnoreCase("twitter")) {
                    profile_imagetwitter.setBackground(d);
                } else {
                }
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularRevealActivity() {

        int cx = rootLayout.getWidth() / 2;
        int cy = rootLayout.getHeight() / 2;

        float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, 0, 100, 100, finalRadius);
//        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, 100, 100, 100, 0);
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        rootLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
//        circularReveal.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        circularRevealActivity();

    }
}
