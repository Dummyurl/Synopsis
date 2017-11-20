package com.synopsis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScreenSlidePageFragment extends Fragment {
    RelativeLayout rl, rllike, rlbottom, rlfooter;
    ViewGroup rootView;
    ImageView Video_play;
    ImageView newsimages, imglike;
    TextView title, news, shortby, txtshortbyheding, source, txtlike, txtshare, txtmoreat, txtdatetime;
    Button audio_play_stop;
    TextView txtlisten;
    String NewsId = "", NewsTitle = "", NewsSummary = "", NewsVideo = "", NewsImageUrl = "", NewsDate = "", News_source = "", news_like, News_url, Share_url, newsaudio, published_by = "", Cattype;//, current_news_like,
    String NewsIdd = "", NewsTitled = "", NewsSummaryd = "", NewsVideod = "", NewsImageUrld = "", NewsImaged = "", NewsDated = "", News_sourced = "", news_liked, current_news_liked, News_urld, Share_urld, newsaudiod, published_byd = "", Cattyped;

    public static String f_N_id, f_Cat_type, f_imgUrl, s_N_id, s_Cat_type, s_imgUrl, N_id, Cat_type, N_imgUrl;
    JSONArray total_like;
    Boolean Like_status;//, News_like_current_Status;
    public static TextToSpeech t1;
    DBHelper mydb, mydbletest;
    Boolean allreadybookmark = false;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    RelativeLayout like, share, rlaudio, rlaudios;
    public static Boolean isChecked = true;
    RelativeLayout rlmediaplayer;
    Holder h;
    Cursor c, cc;
    String letestNewsIds;
    boolean catch_check = false;
    ArrayList<String> unreadarray_list;
    int totalunread;
    String CateName, CatId;
    Boolean like_status, like_statusd;
    PopupWindow popupWindow;
    View layout;
    boolean nightmode,hdimage;
    Bitmap myBitmap = null;
    InputStream input = null;
    String Insert_DateTime;
    public ImageLoader imageLoader;
    Bitmap BitMap = null;
    byte[] imagebitmap;
    LikeProgressBar loginprogress;
    public static boolean videofullscreen = false;
    boolean likeexecutionfinish = true;
    int m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        try {
            rl = (RelativeLayout) rootView.findViewById(R.id.rl);
//            rlmusic = (RelativeLayout) rootView.findViewById(R.id.rlmusic);
            Video_play = (ImageView) rootView.findViewById(R.id.play);
            newsimages = (ImageView) rootView.findViewById(R.id.newsimage);
            like = (RelativeLayout) rootView.findViewById(R.id.like);
            title = (TextView) rootView.findViewById(R.id.txttitle);
            news = (TextView) rootView.findViewById(R.id.txtnews);
            shortby = (TextView) rootView.findViewById(R.id.txtshortby);
            txtdatetime = (TextView) rootView.findViewById(R.id.txtdatetime);
            txtshortbyheding = (TextView) rootView.findViewById(R.id.txtshortbyheding);
            source = (TextView) rootView.findViewById(R.id.textsource);
            share = (RelativeLayout) rootView.findViewById(R.id.share);
            txtlike = (TextView) rootView.findViewById(R.id.txtlike);
            txtshare = (TextView) rootView.findViewById(R.id.txtshare);
            txtmoreat = (TextView) rootView.findViewById(R.id.txtmoreat);
            rllike = (RelativeLayout) rootView.findViewById(R.id.rllike);
            imglike = (ImageView) rootView.findViewById(R.id.imglike);
            rlbottom = (RelativeLayout) rootView.findViewById(R.id.rlbottom);
            rlfooter = (RelativeLayout) rootView.findViewById(R.id.rlfooter);
            rlaudio = (RelativeLayout) rootView.findViewById(R.id.rlaudio);
            rlaudios = (RelativeLayout) rootView.findViewById(R.id.rlaudios);
            audio_play_stop = (Button) rootView.findViewById(R.id.audio_play_stop);
            rlmediaplayer = (RelativeLayout) rootView.findViewById(R.id.rlmediaplayer);
            txtlisten = (TextView) rootView.findViewById(R.id.txtlisten);
            // news.setTextSize(12);

            sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            boolean firstscreen = sharedpreferences.getBoolean("firstscreen", false);
            Boolean second = sharedpreferences.getBoolean("second", false);
            nightmode = sharedpreferences.getBoolean("Nightmode", false);
            hdimage = sharedpreferences.getBoolean("HDImage", false);
//            boolean thirdscreen = sharedpreferences.getBoolean("third", false);
            sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            CateName = sharedpreferences.getString("CateName", "");
            LayoutInflater inflaterr = getActivity().getLayoutInflater();
            layout = inflaterr.inflate(R.layout.smallcustom_progressbar, null);
            popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ScreenSlidePagerActivity.update = true;

//            audio_play_stop.setBackgroundResource(R.drawable.dull);
            imageLoader = new ImageLoader(getActivity().getApplicationContext());

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            if (height <= 854) {
                title.setTextSize(13);
                news.setTextSize(12);
                ViewGroup.LayoutParams params = rlmediaplayer.getLayoutParams();
                params.height = 300;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                rlmediaplayer.setLayoutParams(params);
            }
//            if(videofullscreen)
//            {
//                ScreenSlidePagerActivity.ListPosition=ScreenSlidePagerActivity.ListPosition-2;
//            }


            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (Splash_Page.newslist.size() > 1) {
                    if (firstscreen) {
                        ScreenSlidePagerActivity.ListPosition = ScreenSlidePagerActivity.ListPosition + 0;
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("firstscreen", false);
                        editor.commit();
                        ScreenSlidePagerActivity.firstimage = true;
                        h = Splash_Page.newslist.get(ScreenSlidePagerActivity.ListPosition);
                        ScreenSlidePagerActivity.FirstId = Integer.parseInt(h.getNewsId());

                        f_N_id = h.getNewsId();
                        f_Cat_type = h.getCattype();
                        f_imgUrl = h.getNewsImage();

                    } else if (second) {

                        ScreenSlidePagerActivity.ListPosition = ScreenSlidePagerActivity.ListPosition - 1;
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("second", false);
                        editor.commit();
                        ScreenSlidePagerActivity.secondimage = true;
                        h = Splash_Page.newslist.get(ScreenSlidePagerActivity.ListPosition);
                        ScreenSlidePagerActivity.SecondId = Integer.parseInt(h.getNewsId());

                        s_N_id = h.getNewsId();
                        s_Cat_type = h.getCattype();
                        s_imgUrl = h.getNewsImage();

                    } else {
                        h = Splash_Page.newslist.get(ScreenSlidePagerActivity.ListPosition);

                    }
                } else {

                }
            } else {
                if (Splash_Page.newslist.size() > 1) {
                    if (firstscreen) {
                        if (ScreenSlidePagerActivity.ListPosition == 0) {
                            ScreenSlidePagerActivity.ListPosition = ScreenSlidePagerActivity.ListPosition - 0;
                        } else {
                            ScreenSlidePagerActivity.ListPosition = ScreenSlidePagerActivity.ListPosition - 1;
                        }

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("firstscreen", false);
                        editor.commit();
                        ScreenSlidePagerActivity.firstimage = true;
                        h = Splash_Page.newslist.get(ScreenSlidePagerActivity.ListPosition);
                        ScreenSlidePagerActivity.FirstId = Integer.parseInt(h.getNewsId());

                        f_N_id = h.getNewsId();
                        f_Cat_type = h.getCattype();
                        f_imgUrl = h.getNewsImage();
                    } else if (second) {

                        ScreenSlidePagerActivity.ListPosition = ScreenSlidePagerActivity.ListPosition + 1;
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("second", false);
                        editor.commit();
                        ScreenSlidePagerActivity.secondimage = true;
                        h = Splash_Page.newslist.get(ScreenSlidePagerActivity.ListPosition);
                        ScreenSlidePagerActivity.SecondId = Integer.parseInt(h.getNewsId());

                        s_N_id = h.getNewsId();
                        s_Cat_type = h.getCattype();
                        s_imgUrl = h.getNewsImage();
                    } else {
                        h = Splash_Page.newslist.get(ScreenSlidePagerActivity.ListPosition);
                    }
                } else {

                }
            }

//            t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
//                @Override
//                public void onInit(int status) {
//                    if (status != TextToSpeech.ERROR) {
//                        t1.setLanguage(Locale.UK);
//                    }
//                }
//            });
//            if (t1 != null) {
//                t1.stop();
//                ScreenSlidePageFragment.isChecked = true;
//                audio_play_stop.setBackgroundResource(R.drawable.audioplay);
//                // t1.shutdown();
//            }
            h = Splash_Page.newslist.get(ScreenSlidePagerActivity.ListPosition);
            NewsId = h.getNewsId();
            Cattype = h.getCattype();
            NewsImageUrl = h.getNewsImage();
            N_id = NewsId;
            Cat_type = Cattype;
            N_imgUrl = NewsImageUrl;
            NewsTitle = h.getNewsTitle();
            BitMap = h.getNewsbitmap();
            imagebitmap = h.getImagebitmap();
            if (NewsId.equalsIgnoreCase("") || (NewsTitle.equalsIgnoreCase(""))) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(CateName);
                alertDialog.setMessage("" + ScreenSlidePagerActivity.ListPosition);
                alertDialog.setIcon(R.drawable.launchicon);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                    Splash_Page.this.finish();

                    }
                });
                alertDialog.show();
            }
            ScreenSlidePagerActivity.LastId = Integer.parseInt(NewsId);

            NewsSummary = h.getNewsSummary();
            NewsVideo = h.getNewsVideo();
//            NewsImageUrl = h.getNewsImage();
//            new Convertimage().execute();
//            convert.execute();
            ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.argb(200, 255, 255, 255));
            ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
            if (!cd.isConnectingToInternet()) {
//                    Toast.makeText(getActivity(), "Internet connection lost.",Toast.LENGTH_SHORT).show();
//                if (BitMap == null) {
//                    newsimages.setBackgroundResource(R.drawable.aaa);
//                } else {
//                    BitmapDrawable ob = new BitmapDrawable(getResources(), BitMap);
//                    newsimages.setBackgroundDrawable(ob);
//                }
                if (imagebitmap == null) {
                    newsimages.setBackgroundResource(R.drawable.aaa);
                } else {
                    if(hdimage)
                    {
                        try {
                            BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
                        } catch (OutOfMemoryError e) {
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
                            BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
                        }
                    }
                    else
                    {
                        try {
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
                            BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
                        } catch (OutOfMemoryError e) {
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
                            BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
                        }
                    }


//                    try {
//                        BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                    } catch (OutOfMemoryError e) {
//                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                        bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
//                        BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
//                    }
                    ScreenSlidePagerActivity.afterrefresh = false;
                    BitmapDrawable ob = new BitmapDrawable(getResources(), BitMap);
                    newsimages.setBackgroundDrawable(ob);
                }
            } else {
                if (NewsImageUrl.equalsIgnoreCase("")) {
                    newsimages.setBackgroundResource(R.drawable.aaa);
//                    ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.WHITE);
                } else {
                    if (NewsImageUrl.contains("base64")) {
//                    Picasso.with(getActivity()).load(new File("/images/oprah_bees.gif")).into(newsimages);
//                    ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.argb(200, 255, 255, 255));
//                        new Convertimage().execute();
                        newsimages.setBackgroundResource(R.drawable.aaa);
                    } else if (NewsImageUrl.contains("http://images")) {
                        newsimages.setBackgroundResource(R.drawable.aaa);
                    } else {
//                        if (BitMap == null) {
//                            ImageLoader.memoryCache.clear();
//                            imageLoader.DisplayImage(NewsImageUrl, newsimages);
//                        } else {
//                            ScreenSlidePagerActivity.afterrefresh = false;
//                            BitmapDrawable ob = new BitmapDrawable(getResources(), BitMap);
//                            newsimages.setBackgroundDrawable(ob);
//                        }
                        if (imagebitmap == null) {
                            ImageLoader.memoryCache.clear();
                            imageLoader.DisplayImage(NewsImageUrl, newsimages);
                        } else {

                            if(hdimage)
                            {
                                try {
                                    BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
                                } catch (OutOfMemoryError e) {
                                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                    bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
                                    BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
                                }
                            }
                            else
                            {
                                try {
                                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                    bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
                                    BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
                                } catch (OutOfMemoryError e) {
                                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                    bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
                                    BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
                                }
                            }

//                            try {
//                                BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                            } catch (OutOfMemoryError e) {
//                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                                bmOptions.inSampleSize = 2; // 1 = 100% if you write 4 means 1/4 = 25%
//                                BitMap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length, bmOptions);
//                            }
                            ScreenSlidePagerActivity.afterrefresh = false;
                            BitmapDrawable ob = new BitmapDrawable(getResources(), BitMap);
                            newsimages.setBackgroundDrawable(ob);
                        }


//                    Picasso.with(getActivity()).load(NewsImageUrl).into(newsimages);
//                        if (BitMap==null) {
//                        imageLoader.DisplayImage(NewsImageUrl, newsimages);
//                        } else {
//                            BitmapDrawable ob = new BitmapDrawable(getResources(), BitMap);
//                            newsimages.setBackgroundDrawable(ob);
//                        }


//                            try {
//                                Picasso.with(getActivity())
//                                        .load(NewsImageUrl)
//                                        .into(new Target() {
//                                            @Override
//                                            @TargetApi(16)
//                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                                int sdk = android.os.Build.VERSION.SDK_INT;
//                                                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                                                    newsimages.setBackgroundDrawable(new BitmapDrawable(bitmap));
//                                                } else {
//                                                    if(bitmap.equals(null))
//                                                    {
//
//                                                    }
//                                                    else {
//                                                        try {
//                                                            newsimages.setBackground(new BitmapDrawable(getResources(), bitmap));
//                                                        }
//                                                        catch(Exception e)
//                                                        {
//                                                            Log.e("Picoso ","image error");
//                                                        }
//                                                    }
//
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onBitmapFailed(Drawable errorDrawable) {
//                                                // use error drawable if desired
//                                            }
//
//                                            @Override
//                                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                                                // use placeholder drawable if desired
//                                            }
//                                        });
//                                ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.argb(200, 255, 255, 255));
//                            }
//                            catch(Exception e)
//                            {
//                                Log.e("Picoso ", "image error");
//                            }
                    }
                }
            }

            NewsDate = h.getNewsDate();
            News_source = h.getNews_source();
            NewsId = h.getNewsId();
            Like_status = h.getLike_status();
//            News_like_current_Status = Like_status;
            news_like = h.getNews_like();
//            current_news_like = news_like;
            News_url = h.getNews_url();
            Share_url = h.getShare_url();
            published_by = h.getPublished_by();
            Cattype = h.getCattype();
            mydbletest = new DBHelper(getActivity());

//            if (Like_status) {
//                imglike.setBackgroundResource(R.drawable.like);
//            } else {
//                imglike.setBackgroundResource(R.drawable.unlike);
//            }


            if (Integer.parseInt(news_like) == 1) {
                txtlike.setText("1 Like");
                if (Like_status) {
                    imglike.setBackgroundResource(R.drawable.like);
                } else {
                    imglike.setBackgroundResource(R.drawable.unlike);
                }
            } else if (Integer.parseInt(news_like) == 0) {
                txtlike.setText(" Like");
                imglike.setBackgroundResource(R.drawable.unlike);
//                imglike.setBackgroundResource(R.drawable.unlike);
            } else {
                txtlike.setText(news_like + " Likes");
                if (Like_status) {
                    imglike.setBackgroundResource(R.drawable.like);
                } else {
                    imglike.setBackgroundResource(R.drawable.unlike);
                }
            }

            newsaudio = NewsTitle + " " + NewsSummary;

//            if (h.getNewsImage().equalsIgnoreCase("")) {
//                newsimages.setBackgroundResource(R.drawable.aaa);
////                rlmediaplayer.setVisibility(View.GONE);
//                ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.WHITE);
//            } else {
//
//                BitmapDrawable ob = new BitmapDrawable(getResources(), myBitmap);
//                newsimages.setBackgroundDrawable(ob);
//                ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.argb(200, 255, 255, 255));
////            newsimage.setImageBitmap(bitmap);
//            }
            if (h.getNewsVideo().equalsIgnoreCase("")) {
                Video_play.setVisibility(View.GONE);
            } else {
                Video_play.setVisibility(View.VISIBLE);
                NewsVideo = h.getNewsVideo();

            }
//            }
//        mydb = new DBHelper(getActivity());
//        ArrayList array_list = mydb.getbookmarkdata();
//        int arraylength = array_list.size();
//        allreadybookmark = false;
//        for (int i = 0; i < arraylength; i++) {
////    String aaa=array_list.get(i);
//            if (NewsId.equals(array_list.get(i))) {
//                allreadybookmark = true;
//                break;
//            }
//        }
//
//        if (allreadybookmark) {
//            title.setTextColor(Color.RED);
//        }
//        else
//        {
//            title.setTextColor(getResources().getColor(R.color.textColorPrimary));
//
//        }

            title.setText(capitalizeAllWords(NewsTitle));
//        ScreenSlidePagerActivity.NUM_PAGES = 10;
            String subSummary = NewsSummary.replaceAll("\r\n\r\n", "\r\n");
            mydb = new DBHelper(getActivity());
//            mydb.update(NewsId, "read", Cattype);
            mydb.readupdate(NewsId, "read", "All News");
//            mydb.updateunread(NewsId, "read");

//            if (CateName.equalsIgnoreCase("Unread")) {
//                mydb.update(NewsId, "read", "Unread");
//            }
//            mydb = new DBHelper(getActivity());
//            unreadarray_list = mydb.getunread(CateName);
//            totalunread = unreadarray_list.size();
//
//            c = mydb.getunreadData();
//
//            for(int i=0;i<Splash_Page.letest_newslist_id.size();i++)
//            {
//                c.moveToFirst();
//                for(int j=0;i<totalunread;j++)
//                {
//                    if (Integer.parseInt(c.getString(c.getColumnIndex("newsid"))) == Integer.parseInt(Splash_Page.letest_newslist_id.get(i).getNewsId())) {
//                        Splash_Page.count = Splash_Page.count - 1;
//
//
//                    }
//                    c.moveToNext();
//                }
//            }
//            if(Splash_Page.count>0)
//            {
//                ScreenSlidePagerActivity.txtnew_news.setText("" + Splash_Page.count + " New");
//            }

//            news.setText(subSummary);
/*++++++++++++++++++++++++++++++ remove all html teg from string++++++++++++++++++++*/
            String ss=  Html.fromHtml(subSummary).toString().replaceAll("\n", "").trim();
            news.setText(ss);


//        shortby.setText(published_by);
            shortby.setText(" by Synopsis Team");
            String a = getDisplayableTime(h.getNewsDate());
            txtdatetime.setText(" / " + a);
            source.setText(News_source);
            Video_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), VideoPlayerActivity.class);
                    in.putExtra("video_url", NewsVideo);
                    in.putExtra("title", NewsTitle);
                    in.putExtra("news", NewsSummary);
                    in.putExtra("shortby", News_source);
                    startActivity(in);
                }
            });
//            rlaudio.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isChecked) {
//                        // String toSpeak = ed1.getText().toString();
////                    Toast.makeText(getActivity(), toSpeak,Toast.LENGTH_SHORT).show();
//                        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
//                            @Override
//                            public void onInit(int status) {
//                                if (status != TextToSpeech.ERROR) {
//                                    t1.setLanguage(Locale.UK);
//                                    t1.speak(NewsTitle, TextToSpeech.QUEUE_ADD, null);
//                                    isChecked = false;
//                                    audio_play_stop.setBackgroundResource(R.drawable.audiostaop);
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            t1.speak(NewsSummary, TextToSpeech.QUEUE_ADD, null);
//                                        }
//                                    }, 1000);
//                                }
//                            }
//                        });
////                    t1.speak(newsaudio, TextToSpeech.QUEUE_ADD, null);
////                    isChecked=false;
////                    audio_play_stop.setBackgroundResource(R.drawable.audiostaop);
//
//                    } else {
//                        if (t1 != null) {
//                            t1.stop();
//                            // t1.shutdown();
//                        }
//                        isChecked = true;
//                        audio_play_stop.setBackgroundResource(R.drawable.audioplay);
//                    }
//                }
//            });
//            String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";
            rlaudios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScreenSlidePagerActivity.txtspeechlisten = true;
                    if (isChecked) {
                        isChecked = false;
                        if (t1 != null) {
                            t1.stop();
                            // t1.shutdown();
                        }
//                        ScreenSlidePagerActivity.txtspeechlisten = true;
                        // String toSpeak = ed1.getText().toString();
//                    Toast.makeText(getActivity(), toSpeak,Toast.LENGTH_SHORT).show();
                        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    t1.setLanguage(Locale.US);
                                    t1.speak(NewsTitle, TextToSpeech.QUEUE_ADD, null);
//                                    isChecked = false;
                                    audio_play_stop.setBackgroundResource(R.drawable.audiostaop);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isChecked) {
                                                t1.speak(NewsSummary, TextToSpeech.QUEUE_ADD, null);
                                            } else {
                                                t1.stop();
                                            }
                                        }
                                    }, 1000);
                                }

                            }
                        });

//                    t1.speak(newsaudio, TextToSpeech.QUEUE_ADD, null);
//                    isChecked=false;
//                    audio_play_stop.setBackgroundResource(R.drawable.audiostaop);

                    } else {
                        if (t1 != null) {
                            t1.stop();
                            isChecked = true;
                            if (nightmode) {
                                audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                            } else {
                                audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                            }

                            // t1.shutdown();
                        }

                    }
                }
            });

//            txtlisten.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isChecked) {
//                        // String toSpeak = ed1.getText().toString();
////                    Toast.makeText(getActivity(), toSpeak,Toast.LENGTH_SHORT).show();
//                        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
//                            @Override
//                            public void onInit(int status) {
//                                if (status != TextToSpeech.ERROR) {
//                                    t1.setLanguage(Locale.UK);
//                                    t1.speak(NewsTitle, TextToSpeech.QUEUE_ADD, null);
//                                    isChecked = false;
//                                    audio_play_stop.setBackgroundResource(R.drawable.audiostaop);
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            t1.speak(NewsSummary, TextToSpeech.QUEUE_ADD, null);
//                                        }
//                                    }, 1000);
//                                }
//                            }
//                        });
////                    t1.speak(newsaudio, TextToSpeech.QUEUE_ADD, null);
////                    isChecked=false;
////                    audio_play_stop.setBackgroundResource(R.drawable.audiostaop);
//
//                    } else {
//                        if (t1 != null) {
//                            t1.stop();
//                            // t1.shutdown();
//                        }
//                        isChecked = true;
//                        audio_play_stop.setBackgroundResource(R.drawable.audioplay);
//                    }
//                }
//            });
//        audio_play_stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////==================for media player==============
////                if (isChecked) {
////                    final File sdCard = Environment.getExternalStorageDirectory();
////                    soundFilename = sdCard.getAbsolutePath() + "/Synopsis/news.mp3";
////
////                    //soundFilename = filename.getText().toString();
////                    soundFile = new File(soundFilename);
////                    if (soundFile.exists())
////                        soundFile.delete();
////
////                    if (ScreenSlidePagerActivity.mTts.synthesizeToFile(newssummary, null, soundFilename) == TextToSpeech.SUCCESS) {
////                        Toast.makeText(getActivity(), "Sound file created", Toast.LENGTH_SHORT).show();
////
////
////                        handler = new Handler();
////
////                        final Runnable r = new Runnable() {
////                            public void run() {
////                                try {
////                                    player = new MediaPlayer();
////                                    player.setDataSource(soundFilename);
////                                    player.prepare();
////                                    player.start();
////                                } catch (Exception e) {
////                                    Toast.makeText(getActivity(), "try again.", Toast.LENGTH_SHORT).show();
////                                    e.printStackTrace();
////                                }
////                            }
////                        };
////
////                        handler.postDelayed(r, 10000);
////                    } else {
////                        Toast.makeText(getActivity(), "Oops! Sound file not created", Toast.LENGTH_SHORT).show();
////                    }
////                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE
////                    );
////                    View popupView = layoutInflater.inflate(R.layout.audio_popup, null);
////                    popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////
////                    Button next = (Button) popupView.findViewById(R.id.audio_next);
////                    ToggleButton play_stop = (ToggleButton) popupView.findViewById(R.id.audio_play);
////                    TextView title = (TextView) popupView.findViewById(R.id.newstitle);
////                    title.setText(newstitle);
////
////
////                    play_stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                        @Override
////                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                            if (isChecked) {
////
////                            } else {
////
////                            }
////                        }
////                    });
////
////                    popupWindow.showAsDropDown(Video_play, 50, -30);
////                } else {
////                    popupWindow.dismiss();
////                }
//                //==============================for media player==============
//
//                if (isChecked) {
//                    // String toSpeak = ed1.getText().toString();
////                    Toast.makeText(getActivity(), toSpeak,Toast.LENGTH_SHORT).show();
//                    t1.speak(newsaudio, TextToSpeech.QUEUE_FLUSH, null);
//                } else {
//                    if (t1 != null) {
//                        t1.stop();
//                        // t1.shutdown();
//                    }
//                }
//
//
//            }
//        });
            share.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
                    if (!cd.isConnectingToInternet()) {
                        Toast.makeText(getActivity(), "Internet connection loss.", Toast.LENGTH_SHORT).show();
                    } else {
                        rllike.setVisibility(View.GONE);
                        rlfooter.setVisibility(View.GONE);
                        rlbottom.setVisibility(View.GONE);
                        try {


                            File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + "news.jpg");
                            boolean deleted = file.delete();

                            // image naming and path  to include sd card  appending name you choose for file
                            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "news.jpg";

                            // create bitmap screen capture
                            View v1 = getActivity().getWindow().getDecorView().getRootView();
                            v1.setDrawingCacheEnabled(true);
                            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                            v1.setDrawingCacheEnabled(false);

                            File imageFile = new File(mPath);

                            FileOutputStream outputStream = new FileOutputStream(imageFile);
                            int quality = 100;
                            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                            outputStream.flush();
                            outputStream.close();

                            initShareIntent("face");
                        } catch (Throwable e) {
                            // Several error may come out with file handling or OOM
                            e.printStackTrace();
                        }
                        rllike.setVisibility(View.VISIBLE);
                        rlfooter.setVisibility(View.VISIBLE);
                        rlbottom.setVisibility(View.VISIBLE);
                    }
                    return false;
                }

            });

            like.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
//                    if(likeexecutionfinish)
//                    {
//                    likeexecutionfinish = false;
                    if (!cd.isConnectingToInternet()) {
                        Toast.makeText(getActivity(), "Internet connection loss.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Like_status) {
                            if (Integer.parseInt(news_like) == 1) {
                                txtlike.setText(" Like");
                                news_like = "0";
                            } else if (Integer.parseInt(news_like) == 0) {
                                txtlike.setText(" Like");
                                news_like = "0";
                            } else if (Integer.parseInt(news_like) == 2) {
                                txtlike.setText("" + (Integer.parseInt(news_like) - 1) + " Like");
                                news_like = "" + (Integer.parseInt(news_like) - 1);
                            } else if (Integer.parseInt(news_like) > 2) {

                                txtlike.setText("" + (Integer.parseInt(news_like) - 1) + " Likes");
                                news_like = "" + (Integer.parseInt(news_like) - 1);
                            }
                            imglike.setBackgroundResource(R.drawable.unlike);
//                            current_news_like=txtlike.getText().toString().substring(0,txtlike.getText().toString().indexOf(" "));
                            Like_status = false;
                        } else {
                            if (Integer.parseInt(news_like) == 1) {
                                txtlike.setText("2 Likes");
                                news_like = "2";
                            } else if (Integer.parseInt(news_like) == 0) {
                                txtlike.setText("1 Like");
                                news_like = "1";
                            } else if (Integer.parseInt(news_like) > 1) {
//                                int a=Integer.parseInt(news_like)+1;
                                txtlike.setText("" + (Integer.parseInt(news_like) + 1) + " Likes");
                                news_like = "" + (Integer.parseInt(news_like) + 1);
                            }
                            imglike.setBackgroundResource(R.drawable.like);
//                            current_news_like=txtlike.getText().toString().substring(0,txtlike.getText().toString().indexOf(" "));
                            Like_status = true;
                        }
//                        if (likeexecutionfinish) {

                        mydb.updateLike(NewsId, news_like, Like_status, "All News");

                        Holder h = new Holder();


                        h.setNewsId(NewsId);
                        h.setNewsTitle(NewsTitle);
                        h.setNewsSummary(NewsSummary);
                        h.setNewsVideo(NewsVideo);
                        h.setNewsImage(NewsImageUrl);
                        h.setNewsDate(NewsDate);
                        h.setNews_source(News_source);
                        h.setNews_url(News_url);
                        h.setShare_url(Share_url);
                        h.setNews_like(news_like);
                        h.setLike_status(Like_status);
                        h.setPublished_by(published_by);
                        h.setCattype(Cattype);
                        h.setNewsbitmap(BitMap);

                        for (int k = 0; k < Splash_Page.newslist.size(); k++) {
                            Holder hol = Splash_Page.newslist.get(k);
                            String id = hol.getNewsId();
                            if (id.equalsIgnoreCase(NewsId)) {
                                m = k;
                                break;
                            }
                        }


                        Splash_Page.newslist.add(m, h);
                        Splash_Page.newslist.remove(m + 1);

                        loginprogress = new LikeProgressBar();
                        loginprogress.execute();
//                        }
                    }

//                    }


                    return false;
                }
            });
//        like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new LikeProgressBar().execute();
//            }
//        });
            rl.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(getActivity(), "toSpeak",Toast.LENGTH_SHORT).show();
                    if (t1 != null) {
                        t1.stop();
                        isChecked = true;
                        if (nightmode) {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                        } else {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                        }
                        // t1.shutdown();
                    }
//                    ScreenSlidePageFragment.isChecked = true;
//                    audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                    return false;
                }
            });
            rl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (t1 != null) {
                        t1.stop();
                        isChecked = true;
                        if (nightmode) {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                        } else {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                        }
                        // t1.shutdown();
                    }
                    return false;
                }
            });
            news.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (t1 != null) {
                        t1.stop();
                        isChecked = true;
                        if (nightmode) {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                        } else {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                        }
                        // t1.shutdown();
                    }
//                    ScreenSlidePageFragment.isChecked = true;
//                    audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                    return false;
                }
            });
            news.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (t1 != null) {
                        t1.stop();
                        isChecked = true;
                        if (nightmode) {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                        } else {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                        }
                        // t1.shutdown();
                    }
                    return false;
                }
            });
            rlmediaplayer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (t1 != null) {
                        t1.stop();
                        isChecked = true;
                        if (nightmode) {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                        } else {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                        }
                        // t1.shutdown();
                    }
//                    ScreenSlidePageFragment.isChecked = true;
//                    audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                    return false;
                }
            });
            rlmediaplayer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (t1 != null) {
                        t1.stop();
                        isChecked = true;
                        if (nightmode) {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                        } else {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                        }
                        // t1.shutdown();
                    }
                    return false;
                }
            });
//        rlmediaplayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleActionBar();
////                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
////              String  CateName = sharedpreferences.getString("CateName", "");
//                mydb = new DBHelper(getActivity());
//                unreadarray_list = mydb.getunread(CateName);
//                totalunread = unreadarray_list.size();
//                String msg;
//                if (totalunread > 1) {
//                    msg = totalunread + " unread stories below";
//                } else if (totalunread == 1) {
//                    msg = totalunread + " unread story below";
//                } else {
//                    msg = "No unread stories below";
//                }
//                LayoutInflater inflater = getActivity().getLayoutInflater();
//                // Inflate the Layout
//                View layout = inflater.inflate(R.layout.my_custom_toast,
//                        (ViewGroup) rootView.findViewById(R.id.custom_toast_layout));
//
//                TextView text = (TextView) layout.findViewById(R.id.textToShow);
//                // Set the Text to show in TextView
//                text.setText(msg);
//
//                Toast toast = new Toast(getActivity().getApplicationContext());
//                toast.setGravity(Gravity.BOTTOM, 0, 15);
//                toast.setDuration(Toast.LENGTH_SHORT);
//                toast.setView(layout);
//                toast.show();
//            }
//        });
            rlmediaplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleActionBar();
//                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//              String  CateName = sharedpreferences.getString("CateName", "");
                    if (CateName.equalsIgnoreCase("Saved Article/Bookmarks")) {

                    } else {
                        mydb = new DBHelper(getActivity());
                        unreadarray_list = mydb.getunread(CateName);
                        totalunread = unreadarray_list.size();
                        String msg;
                        if (totalunread > 1) {
                            msg = totalunread + " unread stories below";
                        } else if (totalunread == 1) {
                            msg = totalunread + " unread story below";
                        } else {
                            msg = "No unread stories below";
                        }
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        // Inflate the Layout
                        View layout = inflater.inflate(R.layout.my_custom_toast,
                                (ViewGroup) rootView.findViewById(R.id.custom_toast_layout));

                        TextView text = (TextView) layout.findViewById(R.id.textToShow);
                        // Set the Text to show in TextView
                        text.setText(msg);

                        Toast toast = new Toast(getActivity().getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 15);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        if (CateName.equalsIgnoreCase("Saved Article/Bookmarks")) {

                        } else {
                            toast.show();
                        }
                    }


//                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
            news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleActionBar();
                    if (CateName.equalsIgnoreCase("Saved Article/Bookmarks")) {

                    } else {
                        mydb = new DBHelper(getActivity());
                        unreadarray_list = mydb.getunread(CateName);
                        totalunread = unreadarray_list.size();
                        String msg;
                        if (totalunread > 1) {
                            msg = totalunread + " unread stories below";
                        } else if (totalunread == 1) {
                            msg = totalunread + " unread story below";
                        } else {
                            msg = "No unread stories below";
                        }
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        // Inflate the Layout
                        View layout = inflater.inflate(R.layout.my_custom_toast,
                                (ViewGroup) rootView.findViewById(R.id.custom_toast_layout));

                        TextView text = (TextView) layout.findViewById(R.id.textToShow);
                        // Set the Text to show in TextView
                        text.setText(msg);

                        Toast toast = new Toast(getActivity().getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 15);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        if (CateName.equalsIgnoreCase("Saved Article/Bookmarks")) {

                        } else {
                            toast.show();
                        }
                    }

                }
            });

            source.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (t1 != null) {

                        t1.stop();
                        isChecked = true;
                        if (nightmode) {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
                        } else {
                            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
                        }
                        // t1.shutdown();
                    }

                    ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
                    if (!cd.isConnectingToInternet()) {
                        Toast.makeText(getActivity(), "Internet connection loss.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent in = new Intent(getActivity(), MoreAt.class);
                        in.putExtra("URL", News_url);
                        startActivity(in);
                    }

                }
            });


            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mydb = new DBHelper(getActivity());
                    ArrayList array_list = mydb.getbookmarkdata();
                    int arraylength = array_list.size();
                    allreadybookmark = false;
                    for (int i = 0; i < arraylength; i++) {
                        if (NewsId.equals(array_list.get(i))) {
                            allreadybookmark = true;
                            break;
                        }
                    }
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    // Inflate the Layout
                    View layout = inflater.inflate(R.layout.my_custom_toast,
                            (ViewGroup) rootView.findViewById(R.id.custom_toast_layout));

                    TextView text = (TextView) layout.findViewById(R.id.textToShow);
                    // Set the Text to show in TextView


                    Toast toast = new Toast(getActivity().getApplicationContext());
                    toast.setGravity(Gravity.BOTTOM, 0, 15);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                    String msg;

                    if (allreadybookmark) {
                        mydb.deleteBookmark(Integer.parseInt(NewsId));
                        title.setTextColor(getResources().getColor(R.color.textColorPrimary));
                        msg = "Bookmark removed";
                    } else {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Insert_DateTime = sdf.format(c.getTime());
                        mydb.insertbookmarks(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImageUrl, NewsDate, News_source, news_like, News_url, Share_url, Like_status, published_by, "Saved Article/Bookmarks", "read", Insert_DateTime, BitMap);

                        title.setTextColor(Color.RED);
                        msg = "News bookmarked";
                    }
                    text.setText(msg);
                }
            });
        } catch (Exception e) {
            if (NewsId.equalsIgnoreCase("") || (NewsTitle.equalsIgnoreCase(""))) {
                title.setText("");
                news.setText("");
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Reload the app");
                alertDialog.setIcon(R.drawable.launchicon);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                    Splash_Page.this.finish();
//                        getActivity().finish();
                        getActivity().finishAffinity();
                    }
                });
                alertDialog.show();
            }
        }
//        if(videofullscreen)
//        {
//            ScreenSlidePagerActivity.ListPosition++;
//        }
        return rootView;
    }

    public class LikeProgressBar extends AsyncTask<String, String, String> {
        ProgressDialog pdia;
        String likecount;
        boolean news_like_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            popupWindow.showAtLocation(layout, Gravity.BOTTOM, -0, 100);
//            popupWindow.setFocusable(true);
//            popupWindow.update();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "like.php?id=" + NewsId + "&device_id=" + Splash_Page.android_id);
            try {
                // Getting Array of Employee
                total_like = json.getJSONArray("total_like");


                JSONObject c = total_like.getJSONObject(0);

                likecount = c.getString("count");
                news_like_status = c.getBoolean("news_like");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//        pdia.dismiss();
//            current_news_like = likecount;
//            News_like_current_Status = news_like_status;
//            if (Integer.parseInt(likecount) == 1) {
//                txtlike.setText(likecount + " Like");
//            } else if (Integer.parseInt(likecount) == 0) {
//                txtlike.setText(" Like");
//                imglike.setBackgroundResource(R.drawable.unlike);
//            } else {
//                txtlike.setText(likecount + " Likes");
//            }
//            if (news_like_status) {
//                imglike.setBackgroundResource(R.drawable.like);
//            } else {
//                imglike.setBackgroundResource(R.drawable.unlike);
//            }
//            mydb.updateLike(NewsId, likecount, news_like_status, "All News");
//
//            Holder h = new Holder();
//
//
//            h.setNewsId(NewsId);
//            h.setNewsTitle(NewsTitle);
//            h.setNewsSummary(NewsSummary);
//            h.setNewsVideo(NewsVideo);
//            h.setNewsImage(NewsImage);
//            h.setNewsDate(NewsDate);
//            h.setNews_source(News_source);
//            h.setNews_url(News_url);
//            h.setShare_url(Share_url);
//            h.setNews_like(likecount);
//            h.setLike_status(news_like_status);
//            h.setPublished_by(published_by);
//            h.setCattype(Cattype);
//            h.setNewsbitmap(BitMap);
//
//            for (int k=0;k<Splash_Page.newslist.size();k++)
//            {
//                Holder hol=Splash_Page.newslist.get(k);
//                String id=hol.getNewsId();
//                if(id.equalsIgnoreCase(NewsId))
//                {
//                    m=k;
//                 break;
//                }
//            }
//
//
//
//            Splash_Page.newslist.add(m, h);
//            Splash_Page.newslist.remove(m+1);


//            mydb = new DBHelper(getActivity());
//            sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//            CateName = sharedpreferences.getString("CateName", "");
//            CatId = sharedpreferences.getString("CatId", "");
//            if (CateName.equalsIgnoreCase("All News")) {
//                c = mydb.getallnewsData();
//            } else if (CateName.equalsIgnoreCase("Local")) {
//                c = mydb.getlocalData();
//            } else if (CateName.equalsIgnoreCase("Top Stories")) {
//                c = mydb.gettopstoryData();
//            } else if (CateName.equalsIgnoreCase("Trending")) {
//                c = mydb.gettrendingData();
//            } else if (CateName.equalsIgnoreCase("International")) {
//                c = mydb.getinternationalData();
//            } else if (CateName.equalsIgnoreCase("Business and Finance")) {
//                c = mydb.getbusinessandfinanceData();
//            } else if (CateName.equalsIgnoreCase("Politics")) {
//                c = mydb.getpoliticsData();
//            } else if (CateName.equalsIgnoreCase("Sports")) {
//                c = mydb.getsportsData();
//            } else if (CateName.equalsIgnoreCase("Property")) {
//                c = mydb.getpropertyData();
//            } else if (CateName.equalsIgnoreCase("Technology")) {
//                c = mydb.gettechnologyData();
//            } else if (CateName.equalsIgnoreCase("Entertainment & Gossip")) {
//                c = mydb.getentertainmentData();
//            } else if (CateName.equalsIgnoreCase("Movies and Series")) {
//                c = mydb.getmoviesData();
//            } else if (CateName.equalsIgnoreCase("Health/Science")) {
//                c = mydb.gethealthData();
//            } else if (CateName.equalsIgnoreCase("Lifestyle")) {
//                c = mydb.getlifestyleData();
//            } else if (CateName.equalsIgnoreCase("Trivia")) {
//                c = mydb.gettriviaData();
//            } else if (CateName.equalsIgnoreCase("Jobs")) {
//                c = mydb.getjobsData();
//            } else if (CateName.equalsIgnoreCase("Unread")) {
//                c = mydb.getunreadData();
//            } else if (CateName.equalsIgnoreCase("Saved Article/Bookmarks")) {
//                c = mydb.getbookmarkData();
//            }
//            executemethod();
//            popupWindow.dismiss();
        }
    }

    private void initShareIntent(String type) {
        boolean found = false;
//===================================only for facebook sharing================
//        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//        share.setType("image/jpeg");
//
//        // gets the list of intents that can be loaded.
//        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(share, 0);
//        if (!resInfo.isEmpty()) {
//            for (ResolveInfo info : resInfo) {
//                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
//                        info.activityInfo.name.toLowerCase().contains(type)) {
//                    share.putExtra(Intent.EXTRA_TEXT, "Elevator Express");
//                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/" + "a.jpg"))); // Optional, just if you wanna share an image.
//                    share.setPackage(info.activityInfo.packageName);
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                Toast.makeText(getActivity(), "Facebook does not exist", Toast.LENGTH_SHORT).show();
//
//                return;
//            }
//            startActivity(Intent.createChooser(share, "Select"));
//        }

//================= for  sharing contants===================

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_SUBJECT, "Synopsis News");
        share.putExtra(Intent.EXTRA_TEXT, "Save time. Download Synopsis,It's highest rated news app, to read news in 80 words.\n" + Share_url);
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/" + "news.jpg"))); // Optional, just if you wanna share an image.
        startActivity(Intent.createChooser(share, "Share image using"));
        rllike.setVisibility(View.GONE);
        rlfooter.setVisibility(View.GONE);
        rlbottom.setVisibility(View.GONE);

        //=============================================
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public Bitmap createBitmapFromLayoutWithText() {
        LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate the layout into a view and configure it the way you like
        RelativeLayout view = new RelativeLayout(getActivity());
        mInflater.inflate(R.layout.img_view, view, true);
        // TextView tv = (TextView)rootView.findViewById(R.id.txtnews);
        //  tv.setText("Beat It!!");

        //Provide it with a layout params. It should necessarily be wrapping the
        //content as we not really going to have a parent for it.
        view.setLayoutParams(new Toolbar.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        //Pre-measure the view so that height and width don't remain null.
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //Assign a size and position to the view and all of its descendants
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        //Create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //Create a canvas with the specified bitmap to draw into
        Canvas c = new Canvas(bitmap);

        //Render this view (and all of its children) to the given Canvas
        view.draw(c);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "v2i.png");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
        }
        return bitmap;
    }

    private void toggleActionBar() {
        if (ScreenSlidePagerActivity.toolbar != null) {
            if (ScreenSlidePagerActivity.toolbar.isActivated()) {
                ScreenSlidePagerActivity.toolbar.setVisibility(View.GONE);
                ScreenSlidePagerActivity.toolbar.setActivated(false);
            } else {
                ScreenSlidePagerActivity.toolbar.setVisibility(View.VISIBLE);
                ScreenSlidePagerActivity.toolbar.setActivated(true);
            }
        }
    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    public static String capitalizeAllWords(String str) {
        String phrase = "";
        boolean capitalize = true;
        for (char c : str.toLowerCase().toCharArray()) {
            if (Character.isLetter(c) && capitalize) {
                phrase += Character.toUpperCase(c);
                capitalize = false;
                continue;
            } else if (c == ' ') {
                capitalize = true;
            }
            phrase += c;
        }
        return phrase;
    }

//    public void executemethod() {
//        likeexecutionfinish = true;
//        Splash_Page.newslength = c.getCount();
//        if (c != null) {
//            Splash_Page.allnewslist.clear();
//            c.moveToFirst();
//        }
//        do {
//            if (Splash_Page.GoCategory) {
//
//                break;
//            }
//            NewsIdd = c.getString(c.getColumnIndex("newsid"));
//            NewsTitled = c.getString(c.getColumnIndex("newstitle"));
//            NewsSummaryd = c.getString(c.getColumnIndex("newssummary"));
//            NewsVideod = c.getString(c.getColumnIndex("newsvideo"));
//            NewsImaged = c.getString(c.getColumnIndex("newsimageurl"));
//            NewsDated = c.getString(c.getColumnIndex("newsdate"));
//            News_sourced = c.getString(c.getColumnIndex("newssource"));
//            news_liked = c.getString(c.getColumnIndex("newslike"));
//            int ab = c.getInt(c.getColumnIndex("likestatus"));
//            if (ab == 1) {
//                like_statusd = true;
//            } else {
//                like_status = false;
//            }
////            like_status = Boolean.parseBoolean(c.getString(c.getColumnIndex("likestatus")));
//            News_urld = c.getString(c.getColumnIndex("newsurl"));
//            Share_urld = c.getString(c.getColumnIndex("shareurl"));
//            published_byd = c.getString(c.getColumnIndex("published_by"));
//            Cattyped = c.getString(c.getColumnIndex("category"));
////            if (NewsImage.equalsIgnoreCase("")) {
////
////            } else {
////                bitmap = BitmapFactory.decodeByteArray(newsimage, 0, newsimage.length);
////            }
//
//            Holder h = new Holder();
//
//
//            h.setNewsId(NewsIdd);
//            h.setNewsTitle(NewsTitled);
//            h.setNewsSummary(NewsSummaryd);
//            h.setNewsVideo(NewsVideod);
//            h.setNewsImage(NewsImaged);
//            h.setNewsDate(NewsDated);
//            h.setNews_source(News_sourced);
//            h.setNews_url(News_urld);
//            h.setShare_url(Share_urld);
//            h.setNews_like(news_liked);
//            h.setLike_status(like_statusd);
//            h.setPublished_by(published_byd);
//            h.setCattype(Cattyped);
//            Splash_Page.allnewslist.add(h);
//        } while (c.moveToNext());
//        Splash_Page.newslist.clear();
//        Splash_Page.newslist.addAll(Splash_Page.allnewslist);
//    }

    @Override
    public void onResume() {
        super.onResume();
//        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
//                @Override
//                public void onInit(int status) {
//                    if (status != TextToSpeech.ERROR) {
//                        t1.setLanguage(Locale.UK);
//                        audio_play_stop.setBackgroundResource(R.drawable.audioplay);
//                    }
//                }
//            });
        audio_play_stop.setBackgroundResource(R.drawable.audioplay);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        nightmode = sharedpreferences.getBoolean("Nightmode", false);
        if (nightmode) {
            rl.setBackgroundColor(getResources().getColor(R.color.n_background));
            title.setTextColor(getResources().getColor(R.color.n_title));
            news.setTextColor(getResources().getColor(R.color.n_news));
            txtshortbyheding.setTextColor(getResources().getColor(R.color.n_shortbyheading));
            txtdatetime.setTextColor(getResources().getColor(R.color.n_shortby));
            shortby.setTextColor(getResources().getColor(R.color.n_shortby));
            txtlike.setTextColor(getResources().getColor(R.color.n_likeshare));
            txtshare.setTextColor(getResources().getColor(R.color.n_likeshare));
            txtmoreat.setTextColor(getResources().getColor(R.color.n_news));
            source.setTextColor(getResources().getColor(R.color.n_moreat));
            txtlisten.setTextColor(getResources().getColor(R.color.n_likeshare));
            audio_play_stop.setBackgroundResource(R.drawable.audioplaynight);
        } else {
            rl.setBackgroundColor(getResources().getColor(R.color.d_background));
            title.setTextColor(getResources().getColor(R.color.d_title));
            news.setTextColor(getResources().getColor(R.color.d_news));
            txtshortbyheding.setTextColor(getResources().getColor(R.color.d_shortbyheading));
            txtdatetime.setTextColor(getResources().getColor(R.color.d_shortby));
            shortby.setTextColor(getResources().getColor(R.color.d_shortby));
            txtlike.setTextColor(getResources().getColor(R.color.d_likeshare));
            txtshare.setTextColor(getResources().getColor(R.color.d_likeshare));
            txtmoreat.setTextColor(getResources().getColor(R.color.d_news));
            source.setTextColor(getResources().getColor(R.color.d_moreat));
            txtlisten.setTextColor(getResources().getColor(R.color.d_likeshare));
            audio_play_stop.setBackgroundResource(R.drawable.audioplay);
        }
        mydb = new DBHelper(getActivity());
        ArrayList array_list = mydb.getbookmarkdata();
        int arraylength = array_list.size();
        allreadybookmark = false;
        for (int i = 0; i < arraylength; i++) {
            if (NewsId.equals(array_list.get(i))) {
                allreadybookmark = true;
                break;
            }
        }

        if (allreadybookmark) {
            title.setTextColor(Color.RED);
        }


    }

    public class Convertimage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            popupWindow.showAtLocation(layout, Gravity.TOP, 0, 200);
            popupWindow.setFocusable(false);
            popupWindow.update();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (NewsImageUrl.equalsIgnoreCase("")) {

                } else {
                    if (NewsImageUrl.contains("base64")) {
                        String imageDataBytes = NewsImageUrl.substring(NewsImageUrl.indexOf(",") + 1);

                        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 1;
                        myBitmap = BitmapFactory.decodeStream(stream, null, opts);
                    } else if (NewsImageUrl.contains("http://images")) {

                    } else {
                        URL url = new URL(NewsImageUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 1;
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        connection.disconnect();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                catch_check = true;

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            popupWindow.dismiss();
            if (catch_check) {
                Toast.makeText(getActivity(), "Internet connection lost.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    if (h.getNewsImage().equalsIgnoreCase("")) {
                        newsimages.setBackgroundResource(R.drawable.aaa);
//                rlmediaplayer.setVisibility(View.GONE);
                        ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.WHITE);
                    } else {

                        BitmapDrawable ob = new BitmapDrawable(getResources(), myBitmap);
                        newsimages.setBackgroundDrawable(ob);
                        ScreenSlidePagerActivity.toolbar.setBackgroundColor(Color.argb(200, 255, 255, 255));
//            newsimage.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {

                }
            }

        }
    }

    public static String getDisplayableTime(String datestring) {
        long delta = 0, mDate = 0, difference = 0;
        int p_date = 0, c_date = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            Date date = sdf.parse(datestring);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 5);
            calendar.add(Calendar.MINUTE, 30);
            date = calendar.getTime();
            delta = date.getTime();
            p_date = date.getDate();

            mDate = java.lang.System.currentTimeMillis();
            String cu_dateString = sdf.format(mDate);
            Date cu_date = sdf.parse(cu_dateString);
            c_date = cu_date.getDate();


        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            } else if (seconds < 60) {
                return seconds == 1 ? "one second ago" : seconds + " seconds ago";
            } else if (seconds < 120) {
                return "a minute ago";
            } else if (seconds < 2700) // 45 * 60
            {
                return minutes + " minutes ago";
            } else if (seconds < 5400) // 90 * 60
            {
                return "an hour ago";
            } else if (seconds < 86400) // 24 * 60 * 60
            {

                if (p_date == c_date) {
                    if (hours > 10) {
                        return "Today";
                    } else {
                        return hours + " hours ago";
                    }
                } else {
                    return "yesterday";
                }


            } else if (seconds < 172800) // 48 * 60 * 60
            {
                return "yesterday";
            } else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " days ago";
            } else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return null;
    }
}