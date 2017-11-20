package com.synopsis;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScreenSlidePagerActivity extends FragmentActivity implements TextToSpeech.OnInitListener {
    public int NUM_PAGES = 0;// = CategoryandSetting.newslength;
    public static VerticalViewPager mPager;
    public static int ListPosition = 0;
    public static Toolbar toolbar;
    private static final int REQ_TTS_STATUS_CHECK = 0;
    private static final String TAG = "TTS Demo";
    public static TextToSpeech mTts = null;
    public static int check = 0;
    private MediaPlayer player = null;
    public static TextView txtnew_news;
    public static ImageView imgrefresh;
    public static LinearLayout layout_new_news;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String NewsId = "", NewsTitle = "", NewsSummary = "", NewsVideo = "", NewsImage = "", NewsDate = "", News_source = "", news_like, News_url, Share_url, published_by = "", cattype, related_category;
    Boolean like_status;
    JSONArray news = null;
    int temp_length;
    public static int FirstId;
    public static int SecondId;
    public static int LastId;
    int Position;
    public static boolean execute = true;
    String CatId, CateName;
    int position;
    DBHelper mydb, mydbletest;
    Cursor c, cc, c_background;
    public static int total_letest;
    int page_no = 0;
    ArrayList array_list;
    String str = "";
    JSONArray total_latest_news = null;
    Handler mHandler = new Handler();
    Handler handler;
    PopupWindow popupWindoww;
    View layout;
    ImageView imgmenu;
    Animation rotation;
    public static boolean firstcheck = false, update = false;
    public ScreenSlidePagerAdapter adapter;
    CountProgress c_progress;
    String Insert_DateTime;
    LinearLayout layout_menu;
    RelativeLayout layout_imgrefresh, layout_txtnew_news;
    boolean letest_url_call = true;
    public static boolean news_refresh = false;
    public static boolean new_news = false;
    public static boolean firstimage = false, secondimage = false;
    byte[] imagebitmap, imagebitmap_background;

    InputStream input;
    //    Bitmap myBitmap;
    Convertimage convert_image;
    String NewsId_background, NewsImage_background;
    int newslength_background;
//    boolean refresh_new = false;
    ArrayList<Holder> newslist_temp = new ArrayList<Holder>();
    int a = 0;
    //    public static boolean GoCategory = false;
    public static boolean txtspeechlisten = false;
    public static boolean datatransfer = false;
    boolean executioncountfinish = true;
    public static boolean afterrefresh;
    String all = "false", trending = "false", topstory = "false", local = "false", international = "false", business = "false", politics = "false", sports = "false", property = "false", technology = "false", entertainment = "false", movies = "false", health = "false", lifestyle = "false", trivia = "false", jobs = "false", from_notification = "false";
    int releted_categoryid;
    String[] playlists;
    public static Thread thread1,thread2;
boolean minimize=false;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//CategoryandSetting.newslist.size()
        setContentView(R.layout.activity_screen_slide);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        CateName = sharedpreferences.getString("CateName", "");
        CatId = sharedpreferences.getString("CatId", "");
        position = sharedpreferences.getInt("cat_list_position", 0);
        mydb = new DBHelper(ScreenSlidePagerActivity.this);


        Splash_Page.Go_Category_page = false;

        if (Splash_Page.newslist.size() > 0) {
            NUM_PAGES = Splash_Page.newslist.size();
        } else {
            NUM_PAGES = 1;
        }

//        c_progress = new CountProgress();
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
        imgmenu = (ImageView) findViewById(R.id.imgmenu);

        layout_new_news = (LinearLayout) findViewById(R.id.layout_new_news);
        txtnew_news = (TextView) findViewById(R.id.txtnew_news);
        imgrefresh = (ImageView) findViewById(R.id.imgrefresh);
        layout_imgrefresh = (RelativeLayout) findViewById(R.id.layout_imgrefresh);
        layout_txtnew_news = (RelativeLayout) findViewById(R.id.layout_txtnew_news);

        toolbar.setBackgroundColor(Color.argb(200, 255, 255, 255));
        ScreenSlidePagerActivity.toolbar.setVisibility(View.GONE);
        ScreenSlidePagerActivity.toolbar.setActivated(false);


        rotation = AnimationUtils.loadAnimation(ScreenSlidePagerActivity.this, R.anim.button_rotate);
        rotation.setRepeatCount(Animation.INFINITE);

//        rlcategory.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                myTask.cancel(true);
//                c_progress.cancel(true);
//                Intent in = new Intent(ScreenSlidePagerActivity.this, CategoryandSetting.class);
//                startActivity(in);
//                return false;
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                while (true) {
//                    try {
//                        Thread.sleep(20000);
//                        mHandler.post(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                // Write your code here to update the UI.
//                               toggleActionBar();
//                            }
//                        });
//
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
//
//                }
//            }
//        }).start();
        layout_menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Splash_Page.Go_Category_page = true;
                if (c_progress != null) {
                    c_progress.cancel(true);
                }
//                if (convert != null) {
//                    convert.cancel(true);
//                }


                Intent in = new Intent(ScreenSlidePagerActivity.this, CategoryandSetting.class);
                startActivity(in);
//                overridePendingTransition(R.anim.enter, R.anim.exit);
                return false;
            }
        });


        layout_imgrefresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ListPosition > 0) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    if (ListPosition <= 2) {

                    } else if (ListPosition <= 3) {
                        editor.putBoolean("firstscreen", true);
//           editor.putBoolean("second", true);
                        editor.commit();
                    } else {
                        editor.putBoolean("firstscreen", true);
                        editor.putBoolean("second", true);
                        editor.commit();
                    }
                    ListPosition = 0;
                    mPager.setCurrentItem(0, true);
                } else {
                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                    if (cd.isConnectingToInternet()) {
//                    Toast.makeText(getActivity(), "Internet connection lost.",Toast.LENGTH_SHORT).show();
                        letest_url_call = false;
                        imgrefresh.startAnimation(rotation);
                        news_refresh = true;
                        layout_txtnew_news.callOnClick();
                    } else {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
                        TextView text = (TextView) layout.findViewById(R.id.textToShow);
                        // Set the Text to show in TextView
                        text.setText("Unable to refresh,please try again later");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 15);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
//                        Toast.makeText(ScreenSlidePagerActivity.this, "Unable to refresh,please try again later",Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
        });
//        rlimgtop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putBoolean("firstscreen", true);
//                editor.putBoolean("second", true);
//                editor.commit();
//                adapter.notifyDataSetChanged();
//                mPager.setCurrentItem(0);
//
//
//
//            }
//        });
        layout_txtnew_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                news_refresh = true;
//                refresh_new = true;
                 new_news = true;

                if (c_progress != null) {
                    c_progress.cancel(true);
                }
                ScreenSlidePagerActivity.txtnew_news.setVisibility(View.GONE);
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                CateName = sharedpreferences.getString("CateName", "");
                CatId = sharedpreferences.getString("CatId", "");
                mydb = new DBHelper(ScreenSlidePagerActivity.this);
                if (CateName.equalsIgnoreCase("All News")) {


                    Cursor c = mydb.getallnewsData();
                    Splash_Page.newslength = c.getCount();
                    array_list = mydb.getalldata();
                    str = "";
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }

//                                            new CategoryNewsProgress().execute();
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }


                } else if (CateName.equalsIgnoreCase("Trending")) {
                    Cursor c = mydb.gettrendingData();
                    Splash_Page.newslength = c.getCount();
                    array_list = mydb.gettrendingdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Top Stories")) {
                    Cursor c = mydb.gettopstoryData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.gettopstorydata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Saved Article/Bookmarks")) {
                    imgrefresh.setBackgroundResource(R.drawable.aa);
                    imgrefresh.startAnimation(rotation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgrefresh.clearAnimation();
                            imgrefresh.setBackgroundResource(R.drawable.refresh);
                            LayoutInflater inflater = getLayoutInflater();
                            // Inflate the Layout
                            View layout = inflater.inflate(R.layout.my_custom_toast,
                                    (ViewGroup) findViewById(R.id.custom_toast_layout));

                            TextView text = (TextView) layout.findViewById(R.id.textToShow);
                            // Set the Text to show in TextView
                            text.setText("Already Up to Date");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 15);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        }
                    }, 2000);

                } else if (CateName.equalsIgnoreCase("Unread")) {
                    imgrefresh.setBackgroundResource(R.drawable.aa);
                    imgrefresh.startAnimation(rotation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgrefresh.clearAnimation();
                            imgrefresh.setBackgroundResource(R.drawable.refresh);
                            LayoutInflater inflater = getLayoutInflater();
                            // Inflate the Layout
                            View layout = inflater.inflate(R.layout.my_custom_toast,
                                    (ViewGroup) findViewById(R.id.custom_toast_layout));

                            TextView text = (TextView) layout.findViewById(R.id.textToShow);
                            // Set the Text to show in TextView
                            text.setText("Already Up to Date");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 15);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        }
                    }, 2000);
                } else if (CateName.equalsIgnoreCase("Local")) {


                    Cursor c = mydb.getlocalData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getlocaldata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("International")) {


                    Cursor c = mydb.getinternationalData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getinternationaldata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Business and Finance")) {

                    Cursor c = mydb.getbusinessandfinanceData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getbusinessdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Politics")) {

                    Cursor c = mydb.getpoliticsData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getpoliticsdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Sports")) {

                    Cursor c = mydb.getsportsData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getsportsdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Property")) {

                    Cursor c = mydb.getpropertyData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getpropertydata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Technology")) {

                    Cursor c = mydb.gettechnologyData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.gettechnologydata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Entertainment & Gossip")) {

                    Cursor c = mydb.getentertainmentData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getentertainmentdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Movies and Series")) {

                    Cursor c = mydb.getmoviesData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getmoviesdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Health/Science")) {

                    Cursor c = mydb.gethealthData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.gethealthdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }
                } else if (CateName.equalsIgnoreCase("Lifestyle")) {

                    Cursor c = mydb.getlifestyleData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getlifestyledata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Trivia")) {
                    Cursor c = mydb.gettriviaData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.gettriviadata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    }

                } else if (CateName.equalsIgnoreCase("Jobs")) {

                    Cursor c = mydb.getjobsData();
                    Splash_Page.newslength = c.getCount();
                    page_no = Splash_Page.newslength / 10;
                    page_no = page_no + 1;
                    array_list = mydb.getjobsdata();
                    for (int i = 0; i < array_list.size(); i++) {
                        str = str + "," + (String) array_list.get(i);
                    }
                    if (cd.isConnectingToInternet()) {
                        new NewProgress().execute();
                    } else {

                    }

                }

            }
        });

        mPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);

        mPager.setPageTransformer(true, new DepthPageTransformer());
//        mPager.setPageTransformer(true, new DepthPageTransformer());
//        mPager.setOrientation(VerticalViewPager.SCROLL_AXIS_VERTICAL);
//        mPager.setVerticalScrollBarEnabled(true);
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.smallcustom_progressbar, null);
        popupWindoww = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while ((!minimize)&&(!news_refresh)&&(!new_news)) {
                    try {
                        Thread.sleep(30000);
//                        if (refresh_new)
//                        {
//
//                        }
//                        else {
//                            if(executioncountfinish)
//                            {
                            executioncountfinish = false;
                            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            CatId = sharedpreferences.getString("CatId", "");
                            CateName = sharedpreferences.getString("CateName", "");
                            if (CateName.equalsIgnoreCase("All News")) {
                                array_list = mydb.getalldata();
                            } else if (CateName.equalsIgnoreCase("Trending")) {
                                array_list = mydb.gettrendingdata();
                            } else if (CateName.equalsIgnoreCase("Top Stories")) {
                                array_list = mydb.gettopstorydata();
                            } else if (CateName.equalsIgnoreCase("Local")) {
                                array_list = mydb.getlocaldata();
                            } else if (CateName.equalsIgnoreCase("International")) {
                                array_list = mydb.getinternationaldata();
                            } else if (CateName.equalsIgnoreCase("Business and Finance")) {
                                array_list = mydb.getbusinessdata();
                            } else if (CateName.equalsIgnoreCase("Politics")) {
                                array_list = mydb.getpoliticsdata();
                            } else if (CateName.equalsIgnoreCase("Sports")) {
                                array_list = mydb.getsportsdata();
                            } else if (CateName.equalsIgnoreCase("Property")) {
                                array_list = mydb.getpropertydata();
                            } else if (CateName.equalsIgnoreCase("Technology")) {
                                array_list = mydb.gettechnologydata();
                            } else if (CateName.equalsIgnoreCase("Entertainment & Gossip")) {
                                array_list = mydb.getentertainmentdata();
                            } else if (CateName.equalsIgnoreCase("Movies and Series")) {
                                array_list = mydb.getmoviesdata();
                            } else if (CateName.equalsIgnoreCase("Health/Science")) {
                                array_list = mydb.gethealthdata();
                            } else if (CateName.equalsIgnoreCase("Lifestyle")) {
                                array_list = mydb.getlifestyledata();
                            } else if (CateName.equalsIgnoreCase("Trivia")) {
                                array_list = mydb.gettriviadata();
                            } else if (CateName.equalsIgnoreCase("Jobs")) {
                                array_list = mydb.getjobsdata();
                            }

                            str = "";
                            for (int i = 0; i < array_list.size(); i++) {
                                str = str + "," + (String) array_list.get(i);
                            }


//                                new CountProgress().execute();
                            ConnectionDetector cd3 = new ConnectionDetector(getApplicationContext());
                            if (!cd3.isConnectingToInternet()) {
                                executioncountfinish = true;
                            } else {
                                if (CateName.equalsIgnoreCase("Saved Article/Bookmarks")) {
                                    executioncountfinish = true;
                                } else if (CateName.equalsIgnoreCase("Unread")) {
                                    executioncountfinish = true;
                                } else {

                                    ActivityManager am = (ActivityManager) ScreenSlidePagerActivity.this.getSystemService(ACTIVITY_SERVICE);
                                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                                    Log.d("df", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName() + "   Package Name :  " + componentInfo.getPackageName());

                                    if (taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.synopsis.ScreenSlidePagerActivity")) {
                                        if (c_progress != null) {
                                            c_progress.cancel(true);
                                        }
                                        c_progress = new CountProgress();
                                        c_progress.execute();
                                    }


                                }
//                            }
//                            }
//                            else
//                            {
//
//                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            }
        });
        thread1.start();


        thread2= new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                CateName = sharedpreferences.getString("CateName", "");
                if (CateName.equalsIgnoreCase("Contact us"))
                {

                }
                else
                {
                    convert_image = new Convertimage();
                    convert_image.execute();
                }
            }
        });
        thread2.start();

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Position = position;
                if (ScreenSlidePageFragment.t1 != null) {
                    ScreenSlidePageFragment.t1.stop();
                    // t1.shutdown();
                }
                if (position > 0) {

                    imgrefresh.clearAnimation();
                    imgrefresh.setBackgroundResource(R.drawable.toparrow);
                    if (Splash_Page.count > 0) {
                        txtnew_news.setVisibility(View.VISIBLE);
                    } else {
                        txtnew_news.setVisibility(View.GONE);
                    }
                } else {
//                    total_letest=0;
//                    if (Splash_Page.count > 0) {
//                        ScreenSlidePagerActivity.txtnew_news.setText("" + total_letest + " New");
//                        imgrefresh.setBackgroundResource(R.drawable.refresh);
//                        txtnew_news.setVisibility(View.VISIBLE);
//                    } else {
//                    Splash_Page.count = 0;
                    imgrefresh.setBackgroundResource(R.drawable.refresh);

                    if (Splash_Page.count > 0) {
                        txtnew_news.setVisibility(View.VISIBLE);
                    } else {
                        txtnew_news.setVisibility(View.GONE);
                    }
//                    }
                    if (news_refresh) {

                        imgrefresh.startAnimation(rotation);
                        imgrefresh.setBackgroundResource(R.drawable.aa);
                    } else {

                    }
                }

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class Convertimage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mydb = new DBHelper(ScreenSlidePagerActivity.this);
            c_background = mydb.getallnewsDataconverted();
            newslength_background = c_background.getCount();
        }

        @Override
        protected String doInBackground(String... params) {
            if (c_background != null) {
                c_background.moveToFirst();
            }
            if (newslength_background != 0) {
                do {

                    NewsId_background = c_background.getString(c_background.getColumnIndex("newsid"));
                    NewsImage_background = c_background.getString(c_background.getColumnIndex("newsimageurl"));
                    imagebitmap_background = c_background.getBlob(c_background.getColumnIndex("newsimage"));


                    try {
                        if (NewsImage_background.equalsIgnoreCase("")) {

                        } else {
                            if (NewsImage_background.contains("base64")) {
//                                String imageDataBytes = NewsImage_background.substring(NewsImage_background.indexOf(",") + 1);
//
//                                InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
//                                BitmapFactory.Options opts = new BitmapFactory.Options();
//                                // opts.inJustDecodeBounds = true;
//                                opts.inSampleSize = 1;
//                                myBitmap = BitmapFactory.decodeStream(stream, null, opts);
                            } else if (NewsImage_background.contains("http://images")) {

                            } else {
                                if (imagebitmap_background == null) {

                                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                                    if (cd.isConnectingToInternet()) {


                                        URL url = new URL(NewsImage_background);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setDoInput(true);
                                        connection.setInstanceFollowRedirects(false);
                                        connection.setRequestMethod("GET");
                                        connection.connect();

                                        if (connection.getResponseCode() == 200) {
                                            input = connection.getInputStream();

                                            BitmapFactory.Options opts = new BitmapFactory.Options();
                                            // opts.inJustDecodeBounds = true;
                                            opts.inSampleSize = 1;
                                            Bitmap bitmap = null;
                                            bitmap = BitmapFactory.decodeStream(input, null, opts);

                                            mydb.updateBitmap(NewsId_background, "All News", bitmap, "true");

                                            connection.disconnect();
                                            a++;
                                        }
                                    }
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (Splash_Page.Go_Category_page) {
//                        Splash_Page.Go_Category_page = false;
                        break;
                    }
                    if (new_news) {
                        break;
                    }
                    if (news_refresh) {
                        break;
                    }

                    if (txtspeechlisten) {
                        break;
                    }

//                    if (a > 10) {
//
//                        Methodexecute();
//                    }

                } while ((c_background.moveToNext())&&(minimize));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (a > 1) {
////
//                Methodexecute();
//            }


        }
    }

    public void Methodexecute() {
        a = 0;
//        c_background = mydb.getallnewsData();
//
//        Splash_Page.newslength = c_background.getCount();
//        if (c_background != null) {
//            c_background.moveToFirst();
//        }
//        do {
//            try {
//                NewsId = c_background.getString(c_background.getColumnIndex("newsid"));
//                NewsTitle = c_background.getString(c_background.getColumnIndex("newstitle"));
//                NewsSummary = c_background.getString(c_background.getColumnIndex("newssummary"));
//                NewsVideo = c_background.getString(c_background.getColumnIndex("newsvideo"));
//                NewsImage = c_background.getString(c_background.getColumnIndex("newsimageurl"));
//                NewsDate = c_background.getString(c_background.getColumnIndex("newsdate"));
//                News_source = c_background.getString(c_background.getColumnIndex("newssource"));
//                news_like = c_background.getString(c_background.getColumnIndex("newslike"));
//                int ab = c_background.getInt(c_background.getColumnIndex("likestatus"));
//                if (ab == 1) {
//                    like_status = true;
//                } else {
//                    like_status = false;
//                }
////            like_status = Boolean.parseBoolean(c.getString(c.getColumnIndex("likestatus")));
//                News_url = c_background.getString(c_background.getColumnIndex("newsurl"));
//                Share_url = c_background.getString(c_background.getColumnIndex("shareurl"));
//                published_by = c_background.getString(c_background.getColumnIndex("published_by"));
//                cattype = c_background.getString(c_background.getColumnIndex("category"));
//                imagebitmap = c_background.getBlob(c_background.getColumnIndex("newsimage"));
//                Bitmap bitmap = null;
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
//                            bmOptions.inSampleSize = 4; // 1 = 100% if you write 4 means 1/4 = 25%
//                            bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
//                        }
//                    }
//
//                }
//
//                Holder h1 = new Holder();
//
//
//                h1.setNewsId(NewsId);
//                h1.setNewsTitle(NewsTitle);
//                h1.setNewsSummary(NewsSummary);
//                h1.setNewsVideo(NewsVideo);
//                h1.setNewsImage(NewsImage);
//                h1.setNewsDate(NewsDate);
//                h1.setNews_source(News_source);
//                h1.setNews_url(News_url);
//                h1.setShare_url(Share_url);
//                h1.setNews_like(news_like);
//                h1.setLike_status(like_status);
//                h1.setPublished_by(published_by);
//                h1.setCattype(cattype);
//                h1.setNewsbitmap(bitmap);
//                newslist_temp.add(h1);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } while (c_background.moveToNext());
        mydb = new DBHelper(ScreenSlidePagerActivity.this);


        if (CateName.equalsIgnoreCase("All News")) {
            c = mydb.getallnewsData();
        } else if (CateName.equalsIgnoreCase("Trending")) {
            c = mydb.gettrendingData();
        } else if (CateName.equalsIgnoreCase("Top Stories")) {
            c = mydb.gettopstoryData();
        } else if (CateName.equalsIgnoreCase("Local")) {
            c = mydb.getlocalData();
        } else if (CateName.equalsIgnoreCase("International")) {
            c = mydb.getinternationalData();
        } else if (CateName.equalsIgnoreCase("Business and Finance")) {
            c = mydb.getbusinessandfinanceData();
        } else if (CateName.equalsIgnoreCase("Politics")) {
            c = mydb.getpoliticsData();
        } else if (CateName.equalsIgnoreCase("Sports")) {
            c = mydb.getsportsData();
        } else if (CateName.equalsIgnoreCase("Property")) {
            c = mydb.getpropertyData();
        } else if (CateName.equalsIgnoreCase("Technology")) {
            c = mydb.gettechnologyData();
        } else if (CateName.equalsIgnoreCase("Entertainment & Gossip")) {
            c = mydb.getentertainmentData();
        } else if (CateName.equalsIgnoreCase("Movies and Series")) {
            c = mydb.getmoviesData();
        } else if (CateName.equalsIgnoreCase("Health/Science")) {
            c = mydb.gethealthData();
        } else if (CateName.equalsIgnoreCase("Lifestyle")) {
            c = mydb.getlifestyleData();
        } else if (CateName.equalsIgnoreCase("Trivia")) {
            c = mydb.gettriviaData();
        } else if (CateName.equalsIgnoreCase("Jobs")) {
            c = mydb.getjobsData();
        }
        Splash_Page.newslength = c.getCount();
        if (c != null) {
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
            cattype = c.getString(c.getColumnIndex("category"));
            imagebitmap = c.getBlob(c.getColumnIndex("newsimage"));
            Bitmap bitmap = null;
//            if (NewsImage.equalsIgnoreCase("")) {
//
//            } else {
//                if (imagebitmap == null) {
//
//                } else {
//                    try {
//                        bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                    } catch (OutOfMemoryError e) {
//                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                        bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
//                        bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
//                    }
//                }
//
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
            h.setShare_url(Share_url);
            h.setNews_like(news_like);
            h.setLike_status(like_status);
            h.setPublished_by(published_by);
            h.setCattype(cattype);
//            h.setNewsbitmap(bitmap);
            h.setImagebitmap(imagebitmap);
            newslist_temp.add(h);
        } while (c.moveToNext());
//        datatransfer=true;
        Splash_Page.newslist.removeAll(newslist_temp);
//        Splash_Page.newslist.addAll(newslist_temp);
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//***Change Here***
//        startActivity(intent);
//
//        System.exit(0);
//        finish();
        finishAffinity();

    }

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ListPosition = position;
            if (Splash_Page.newslength == 0) {
                return new EmptyFragment();
            } else {
                return new ScreenSlidePageFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_TTS_STATUS_CHECK) {
            switch (resultCode) {
                case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
//                    String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";
                    mTts = new TextToSpeech(ScreenSlidePagerActivity.this, this,"com.svox.pico");
                    Log.v(TAG, "Pico is installed okay");
                    check = 1;

                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
                    Log.v(TAG, "Pico is installed okay");
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
                    Log.v(TAG, "Pico is installed okay");
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
                    Intent installIntent = new Intent();
                    installIntent.setAction(
                            TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
                    Log.v(TAG, "Pico is installed okay");
                    break;
                default:
                    Log.e(TAG, "Got a failure. TTS apparently not available");
            }
        }
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
        }
        if (mTts != null)
            mTts.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        minimize=true;
        if (c_progress != null) {
            c_progress.cancel(true);
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
        if (mTts != null) {
            mTts.shutdown();
        }
    }

    public class NewProgress extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File dir = new File(Environment.getExternalStorageDirectory() + "/LazyList");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
//            imgtotalnews.setVisibility(View.GONE);
//            imgrefresh.startAnimation(rotation);
//            imgrefresh.setVisibility(View.VISIBLE);
//            imgrefresh.startAnimation(rotation);
            if (c_progress != null) {
                c_progress.cancel(true);
            }
//            news_refresh = true;
            imgrefresh.setBackgroundResource(R.drawable.aa);
//            ScreenSlidePagerActivity.txtnew_news.setVisibility(View.GONE);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Insert_DateTime = sdf.format(c.getTime());
            Splash_Page.count = 0;
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jParser = new JSONParser();
            JSONObject json;
            int ab;
            if (letest_url_call) {
                ab = 0;
                json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "latest.php?location=" + Splash_Page.country_name + "&cid=" + CatId + "&cid_array=" + str);

            } else {
                ab = 1;
                json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "category.php?cid=" + CatId + "&location=" + Splash_Page.country_name + "&device_id=" + Splash_Page.android_id + "&cid_array=" + str);// + "&page=" + 1

            }
            try {
                if (ab == 0) {
                    total_latest_news = json.getJSONArray("total_latest_news");
//                    int length = total_latest_news.length();
//                    for (int j = 0; j < length; j++) {
////                        JSONObject cc = total_latest_news.getJSONObject(j);
////                        Splash_Page.count = Integer.parseInt(cc.getString("total_latest_news"));
//                    }
                }
//                else {
//                   total_latest_news = json.getJSONArray("count_latest");
//                    Splash_Page.count = 1;
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (Splash_Page.count != 0) {
            try {
                news = json.getJSONArray("news");
                temp_length = news.length();
            } catch (Exception e) {

            }
            for (int i = 0; i < news.length(); i++) {
                Holder h = new Holder();
                try {
                    all = "false";
                    trending = "false";
                    topstory = "false";
                    local = "false";
                    international = "false";
                    business = "false";
                    politics = "false";
                    sports = "false";
                    property = "false";
                    technology = "false";
                    entertainment = "false";
                    movies = "false";
                    health = "false";
                    lifestyle = "false";
                    trivia = "false";
                    jobs = "false";

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
                    Bitmap bitmap = null;

                    if (news.length() == 1) {
                        try {
                            if (NewsImage.equalsIgnoreCase("")) {

                            } else {
                                if (NewsImage.contains("base64")) {
//                                String imageDataBytes = NewsImage_background.substring(NewsImage_background.indexOf(",") + 1);
//
//                                InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
//                                BitmapFactory.Options opts = new BitmapFactory.Options();
//                                // opts.inJustDecodeBounds = true;
//                                opts.inSampleSize = 1;
//                                myBitmap = BitmapFactory.decodeStream(stream, null, opts);
                                } else if (NewsImage.contains("http://images")) {

                                } else {


                                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                                    if (cd.isConnectingToInternet()) {


                                        URL url = new URL(NewsImage);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setDoInput(true);
                                        connection.setInstanceFollowRedirects(false);
                                        connection.setRequestMethod("GET");
                                        connection.connect();

                                        if (connection.getResponseCode() == 200) {
                                            input = connection.getInputStream();

                                            BitmapFactory.Options opts = new BitmapFactory.Options();
                                            // opts.inJustDecodeBounds = true;
                                            opts.inSampleSize = 1;
                                            bitmap = BitmapFactory.decodeStream(input, null, opts);
                                            connection.disconnect();
                                        }
                                    }

                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }

//                    h.setNewsId(NewsId);
//                    h.setNewsTitle(NewsTitle);
//                    h.setNewsSummary(NewsSummary);
//                    h.setNewsVideo(NewsVideo);
//                    h.setNewsImage(NewsImage);
//                    h.setNewsDate(NewsDate);
//                    h.setNews_source(News_source);
//                    h.setNews_url(News_url);
//                    h.setNews_like(news_like);
//                    h.setLike_status(like_status);
//                    h.setPublished_by(published_by);

//                    if (letest_url_call) {
////                            Splash_Page.letest_newslist_id.add(h);
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "letest", "Unread", Insert_DateTime, bitmap);
//
//                    }
                    for (int j = 0; j < playlists.length; j++) {
                        releted_categoryid = Integer.parseInt(playlists[j]);

                        if (releted_categoryid == 4) {
                            trending = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Trending", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 3) {
                            topstory = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Top Stories", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 2) {
                            local = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Local", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 5) {
                            international = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "International", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 6) {
                            business = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Business and Finance", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 7) {
                            politics = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Politics", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 8) {
                            sports = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Sports", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 9) {
                            property = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Property", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 10) {
                            technology = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Technology", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 199) {
                            entertainment = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Entertainment & Gossip", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 200) {
                            movies = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Movies and Series", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 201) {
                            health = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Health/Science", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 202) {
                            lifestyle = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Lifestyle", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 203) {
                            trivia = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Trivia", "Unread", Insert_DateTime, bitmap);
                        } else if (releted_categoryid == 204) {
                            jobs = "true";
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Jobs", "Unread", Insert_DateTime, bitmap);
                        }
                    }
                    all = "true";
                    from_notification = "true";
                    String convert = "false";
                    mydb.insertall(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "All News", "Unread", Insert_DateTime, bitmap, all, trending, topstory, local, international, business, politics, sports, property, technology, entertainment, movies, health, lifestyle, trivia, jobs, from_notification, convert);

//                    mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, CateName, "Unread", Insert_DateTime, bitmap);
//                    mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Unread", "Unread", Insert_DateTime, bitmap);
//                    if (!CateName.equalsIgnoreCase("All News")) {
//                        mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "All News", "Unread", Insert_DateTime, bitmap);
//
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            letest_url_call = true;
//            imgrefresh.clearAnimation();
//            refresh = false;
//            refresh_new = false;
//            imgrefresh.setBackgroundResource(R.drawable.refresh);
//            imgtotalnews.setVisibility(View.VISIBLE);
//            imgrefresh.clearAnimation();
//            imgrefresh.setVisibility(View.GONE);
//            txttotalnews.setVisibility(View.GONE);
//            imgtotalnews.setBackgroundResource(R.drawable.refresh);
//            Splash_Page.count=0;

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.my_custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
            TextView text = (TextView) layout.findViewById(R.id.textToShow);
            // Set the Text to show in TextView
            text.setText("Already Up to Date");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 15);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            if (temp_length == 0) {
                toast.show();

            } else {
                if (Splash_Page.newslength == temp_length) {
                    toast.show();
                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("firstscreen", true);
                    editor.putBoolean("second", true);
                    editor.commit();
//                    Splash_Page.newslist.clear();
//                    Splash_Page.newslength = 0;
                    mydb = new DBHelper(ScreenSlidePagerActivity.this);
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    CateName = sharedpreferences.getString("CateName", "");
                    CatId = sharedpreferences.getString("CatId", "");
                    if (CateName.equalsIgnoreCase("All News")) {
                        c = mydb.getallnewsData();
                    } else if (CateName.equalsIgnoreCase("Local")) {
                        c = mydb.getlocalData();
                    } else if (CateName.equalsIgnoreCase("Top Stories")) {
                        c = mydb.gettopstoryData();
                    } else if (CateName.equalsIgnoreCase("Trending")) {
                        c = mydb.gettrendingData();
                    } else if (CateName.equalsIgnoreCase("International")) {
                        c = mydb.getinternationalData();
                    } else if (CateName.equalsIgnoreCase("Business and Finance")) {
                        c = mydb.getbusinessandfinanceData();
                    } else if (CateName.equalsIgnoreCase("Politics")) {
                        c = mydb.getpoliticsData();
                    } else if (CateName.equalsIgnoreCase("Sports")) {
                        c = mydb.getsportsData();
                    } else if (CateName.equalsIgnoreCase("Property")) {
                        c = mydb.getpropertyData();
                    } else if (CateName.equalsIgnoreCase("Technology")) {
                        c = mydb.gettechnologyData();
                    } else if (CateName.equalsIgnoreCase("Entertainment & Gossip")) {
                        c = mydb.getentertainmentData();
                    } else if (CateName.equalsIgnoreCase("Movies and Series")) {
                        c = mydb.getmoviesData();
                    } else if (CateName.equalsIgnoreCase("Health/Science")) {
                        c = mydb.gethealthData();
                    } else if (CateName.equalsIgnoreCase("Lifestyle")) {
                        c = mydb.getlifestyleData();
                    } else if (CateName.equalsIgnoreCase("Trivia")) {
                        c = mydb.gettriviaData();
                    } else if (CateName.equalsIgnoreCase("Jobs")) {
                        c = mydb.getjobsData();
                    } else if (CateName.equalsIgnoreCase("Unread")) {
                        c = mydb.getunreadData();
                    }

                    Splash_Page.newslength = c.getCount();

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
                        int ab = c.getInt(c.getColumnIndex("likestatus"));
                        if (ab == 1) {
                            like_status = true;
                        } else {
                            like_status = false;
                        }
//                        like_status = Boolean.parseBoolean(c.getString(c.getColumnIndex("likestatus")));
                        News_url = c.getString(c.getColumnIndex("newsurl"));
                        Share_url = c.getString(c.getColumnIndex("shareurl"));
                        published_by = c.getString(c.getColumnIndex("published_by"));
                        cattype = c.getString(c.getColumnIndex("category"));
                        imagebitmap = c.getBlob(c.getColumnIndex("newsimage"));
                        Bitmap bitmap = null;

//                        if (NewsImage.equalsIgnoreCase("")) {
//
//                        } else {
//                            if (imagebitmap == null) {
//
//                            } else {
//                                try {
//                                    bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                                } catch (OutOfMemoryError e) {
//                                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                                    bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
//                                    bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
//                                }
//                            }
//                        }


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
//                        h.setNewsbitmap(bitmap);
                        h.setImagebitmap(imagebitmap);
                        Splash_Page.newslist.add(h);

                    } while (c.moveToNext());
//                    if (CateName.equalsIgnoreCase("All News")) {
//                        Splash_Page.allnewslist.addAll(Splash_Page.newslist);
//
//                    } else if (CateName.equalsIgnoreCase("Trending")) {
//                        Splash_Page.trendingnewslist.addAll(Splash_Page.newslist);
//                    }
//                    int p = ListPosition;

                    if (Splash_Page.newslist.size() > 0) {
                        NUM_PAGES = Splash_Page.newslist.size();
                    } else {
                        NUM_PAGES = 1;
                    }
                    adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                    mPager.setAdapter(adapter);
                    afterrefresh = true;
                    Splash_Page.count = 0;
                    ScreenSlidePagerActivity.txtnew_news.setVisibility(View.GONE);
                    mPager.pageUp();

////                    Splash_Page.count = 0;
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putBoolean("firstscreen", true);
//                    editor.putBoolean("second", true);
//                    editor.putBoolean("third", true);
//                    editor.commit();
//                    mPager.setOffscreenPageLimit(0);
//                    mPager.setCurrentItem(p+Splash_Page.count-1, true);

//                    mPager.pageDown();
//                    if(letest_url_call)
//                    {
//                        int p=ListPosition;
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        editor.putBoolean("firstscreen", true);
//                        editor.putBoolean("second", true);
//                        editor.commit();
//                        mPager.setCurrentItem(p+Splash_Page.count-1, true);
//                    }
//                    else
//                    {
//                        adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//                    mPager.setAdapter(adapter);
//                    Splash_Page.count = 0;
//                    }

//                    mPager.setCurrentItem(0, true);
//                    ScreenSlidePagerActivity.this.finish();
//                    Intent in = new Intent(ScreenSlidePagerActivity.this, ScreenSlidePagerActivity.class);
//                    startActivity(in);
                }
            }
            imgrefresh.clearAnimation();
            new_news = false;
            news_refresh = false;
            imgrefresh.setBackgroundResource(R.drawable.refresh);
        }
    }

    public class CountProgress extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ConnectionDetector cd3 = new ConnectionDetector(getApplicationContext());
            if (!cd3.isConnectingToInternet()) {
            } else {
                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "latest.php?location=" + Splash_Page.country_name + "&cid=" + CatId + "&cid_array=" + str);
                try {
                    total_latest_news = json.getJSONArray("total_latest_news");
                    int length = total_latest_news.length();
                    for (int j = 0; j < length; j++) {
                        JSONObject cc = total_latest_news.getJSONObject(j);
                        Splash_Page.count = Integer.parseInt(cc.getString("total_latest_news"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            mydbletest = new DBHelper(ScreenSlidePagerActivity.this);
//            cc = mydbletest.getallletestData();
//            int total_letest = cc.getCount();

//            Splash_Page.count = Splash_Page.count + total_letest;
            executioncountfinish = true;
            ActivityManager am = (ActivityManager) ScreenSlidePagerActivity.this.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            Log.d("df", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName() + "   Package Name :  " + componentInfo.getPackageName());

            if (taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.synopsis.ScreenSlidePagerActivity")) {
                if (Splash_Page.count > 0) {
                    ScreenSlidePagerActivity.txtnew_news.setVisibility(View.VISIBLE);
                    ScreenSlidePagerActivity.txtnew_news.setText("" + Splash_Page.count + " New");

//                    layout_txtnew_news.callOnClick();


                    imgrefresh.setVisibility(View.VISIBLE);
                } else {
                    ScreenSlidePagerActivity.txtnew_news.setVisibility(View.GONE);
                    imgrefresh.setVisibility(View.VISIBLE);
                }
            } else {

            }
        }
    }

    private void toggleActionBar() {
        if (ScreenSlidePagerActivity.toolbar != null) {
            if (ScreenSlidePagerActivity.toolbar.isActivated()) {
                ScreenSlidePagerActivity.toolbar.setVisibility(View.GONE);
                ScreenSlidePagerActivity.toolbar.setActivated(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        minimize=false;
    }

}