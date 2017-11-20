package com.synopsis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.synopsis.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.synopsis.CommonUtilities.EXTRA_MESSAGE;
import static com.synopsis.CommonUtilities.SENDER_ID;
import static com.synopsis.CommonUtilities.SERVER_URL;

public class Splash_Page extends AppCompatActivity {
    public static String country_name, android_id;
    AlertDialogManager alert = new AlertDialogManager();
    AsyncTask<Void, Void, Void> mRegisterTask;
    String regId;
    JSONArray total_news = null;
    public static int count;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    //    SQLiteDatabase db;
    DBHelper mydb;
    public static int newslength;
    public static ArrayList<Holder> newslist = new ArrayList<Holder>();

    //public static ArrayList<Holder> allnewslist = new ArrayList<Holder>();


    public static ArrayList<Holder> letest_newslist_id = new ArrayList<Holder>();
    String NewsId = "", NewsTitle = "", NewsSummary = "", NewsVideo = "", NewsImage = "", NewsDate = "", News_source = "", news_like, News_url, Share_url, published_by = "", cattype, related_category;
    JSONArray news = null;
    byte[] imagebitmap;
    Boolean like_status;
    JSONArray total_latest_news = null;
    Cursor c;
    String[] playlists;
    boolean internet = false;
    String Insert_DateTime;
    int releted_categoryid;
    String all = "false", trending = "false", topstory = "false", local = "false", international = "false", business = "false", politics = "false", sports = "false", property = "false", technology = "false", entertainment = "false", movies = "false", health = "false", lifestyle = "false", trivia = "false", jobs = "false", from_notification = "false";
    public static String Serverdatetime="";
    public static boolean Go_Category_page = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        setContentView(R.layout.splash__page);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("cat_list_position", 0);
        editor.putBoolean("firstscreen", true);
        editor.putBoolean("second", true);
        editor.putString("CatId", "0");
        editor.putString("CateName", "All News");
        editor.commit();
        mydb = new DBHelper(this);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
//            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//            alertDialog.setTitle("Warning !");
//            alertDialog.setMessage("No internet connection");
//            alertDialog.setIcon(R.drawable.launchicon);
//            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
////                    Splash_Page.this.finish();
//                    Intent in = new Intent(Splash_Page.this, CategoryandSetting.class);
//                    startActivity(in);
//                    Splash_Page.this.finish();
//                }
//            });
//            alertDialog.show();

            c = mydb.getallnewsData();
            newslength = c.getCount();
            if (newslength == 0) {
                Intent in = new Intent(Splash_Page.this, CategoryandSetting.class);
                startActivity(in);
                Splash_Page.this.finish();
            } else {
                Methodexecute();
            }

            return;
        } else {
            internet = true;
        }

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Synopsis");
        directory.mkdirs();


        File dir = new File(Environment.getExternalStorageDirectory() + "/LazyList");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }


//        db = openOrCreateDatabase("MyDb",MODE_PRIVATE,null);
//        db.execSQL("create table if not exists "
//                + "mytable(_id INTEGER PRIMARY KEY AUTOINCREMENT,newid varchar, newstitle varchar, newssummary varchar "
//                + ",newsdetails varchar,newsvideo varchar"
//                +", newsimage varchar,newsdate varchar,newssource varchar"
//                + ", newslike varchar ,newsurl nvarchar ,like_status varchar)");
//        db.close();

//        ArrayList array_list = mydb.getAllCotacts();
//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);


        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(Splash_Page.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
            return;
        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final Intent mainIntent = new Intent(Splash_Page.this, SignInPage.class);
//                startActivity(mainIntent);
//                finish();
//            }
//        }, 1000);
        country_name = getResources().getConfiguration().locale.getCountry();
//         country_name = getResources().getConfiguration().locale.getDisplayCountry();
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        regId = GCMRegistrar.getRegistrationId(this);

        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
//                final Context context = this;
//                mRegisterTask = new AsyncTask<Void, Void, Void>() {
//
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        // Register on our server
//                        // On server creates a new user
//                        //ServerUtilities.register(context, name, email, regId);
//                        JSONParser jParser = new JSONParser();
//                        // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
//                        JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "addregistration.php?device_id=" + android_id + "&reg_id=" + regId);
//
//
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        mRegisterTask = null;
//                        // Toast.makeText(getApplicationContext(), "New Message: " + regId, Toast.LENGTH_LONG).show();
//                        Intent mainIntent = new Intent(Splash_Page.this, SignInPage.class);
//                        startActivity(mainIntent);
//                        finish();
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//                        editor.putBoolean("Notification", true);
//                        editor.commit();
//                    }
//
//                };
//                mRegisterTask.execute(null, null, null);
            }
        }
//        Intent mainIntent = new Intent(Splash_Page.this, SignInPage.class);
//        startActivity(mainIntent);
//        finish();
        c = mydb.getallnewsData();
        newslength = c.getCount();
        if (newslength == 0) {
            new CategoryNewsProgress().execute();
        } else {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            int LoginId = sharedpreferences.getInt("LoginId", 0);
            Boolean Skip = sharedpreferences.getBoolean("skip", false);
            if ((LoginId == 0) && (!Skip)) {
                Intent mainIntent = new Intent(Splash_Page.this, SignInPage.class);
                startActivity(mainIntent);
                Splash_Page.this.finish();
            } else {
                Methodexecute();
            }

        }


    }

    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            //Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    public void Methodexecute() {
        c = mydb.getallnewsData();
        newslength = c.getCount();
        if (c != null) {
            newslist.clear();
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
//            like_status = Boolean.parseBoolean(c.getString(c.getColumnIndex("likestatus")));
                News_url = c.getString(c.getColumnIndex("newsurl"));
                Share_url = c.getString(c.getColumnIndex("shareurl"));
                published_by = c.getString(c.getColumnIndex("published_by"));
                cattype = c.getString(c.getColumnIndex("category"));
                imagebitmap = c.getBlob(c.getColumnIndex("newsimage"));
                Bitmap bitmap = null;
//            if (NewsImage.equalsIgnoreCase("")) {
//
//            } else {
//                if(imagebitmap==null)
//                {
//
//                }
//                else
//                {
//                    bitmap = BitmapFactory.decodeByteArray(imagebitmap, 0, imagebitmap.length);
//                }
//
//            }
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
                h.setShare_url(Share_url);
                h.setNews_like(news_like);
                h.setLike_status(like_status);
                h.setPublished_by(published_by);
                h.setCattype(cattype);
//                h.setNewsbitmap(bitmap);
                h.setImagebitmap(imagebitmap);
                newslist.add(h);
            } catch (IndexOutOfBoundsException e) {

            } catch (OutOfMemoryError e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (c.moveToNext());

//        allnewslist.addAll(newslist);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int LoginId = sharedpreferences.getInt("LoginId", 0);
        Boolean Skip = sharedpreferences.getBoolean("skip", false);
        if ((LoginId == 0) && (!Skip)) {

            Intent mainIntent = new Intent(Splash_Page.this, SignInPage.class);
            startActivity(mainIntent);
            Splash_Page.this.finish();
        } else {
            Intent in = new Intent(Splash_Page.this, ScreenSlidePagerActivity.class);
            startActivity(in);
            Splash_Page.this.finish();
        }
    }

    public class CategoryNewsProgress extends AsyncTask<String, String, String> {
        Bitmap bitmap = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Insert_DateTime = sdf.format(c.getTime());

        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "category.php?cid=" + 0 + "&location=" +country_name+ "&device_id=" + Splash_Page.android_id);
            try {
                news = json.getJSONArray("news");
                Splash_Page.newslength = news.length();
                // looping of List
                Splash_Page.newslist.clear();
            } catch (Exception e) {
                Splash_Page.newslength = 0;
            }

            for (int i = 0; i < Splash_Page.newslength; i++) {
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
                    InputStream input = null;

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
//                    Holder h = new Holder();
//
//
//                    h.setNewsId(NewsId);
//                    h.setNewsTitle(NewsTitle);
//                    h.setNewsSummary(NewsSummary);
//                    h.setNewsVideo(NewsVideo);
//                    h.setNewsImage(NewsImage);
//                    h.setNewsDate(NewsDate);
//                    h.setNews_source(News_source);
//                    h.setNews_like(news_like);
//                    h.setLike_status(like_status);
//                    h.setNews_url(News_url);
//                    h.setPublished_by(published_by);

//                    if (NewsImage.equalsIgnoreCase("")) {
//
//                    } else {
//                        if (NewsImage.contains("base64")) {
//                            String imageDataBytes = NewsImage.substring(NewsImage.indexOf(",") + 1);
//
//                            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
//                            BitmapFactory.Options opts = new BitmapFactory.Options();
//                            // opts.inJustDecodeBounds = true;
//                            opts.inSampleSize = 1;
//                            myBitmap = BitmapFactory.decodeStream(stream, null, opts);
//                            h.setNewsbitmap(myBitmap);
//                        } else if (NewsImage.contains("http://images")) {
//
//                        } else {
//                            URL url = new URL(NewsImage);
//                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                            connection.setDoInput(true);
//                            connection.setInstanceFollowRedirects(false);
//                            connection.setRequestMethod("GET");
//                            connection.connect();
//                            input = connection.getInputStream();
//
//                            BitmapFactory.Options opts = new BitmapFactory.Options();
//                            // opts.inJustDecodeBounds = true;
//                            opts.inSampleSize = 1;
//                            myBitmap = BitmapFactory.decodeStream(input, null, opts);
//                            //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
//                            //myBitmap.recycle();
//
//                            //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                            h.setNewsbitmap(myBitmap);
//                            connection.disconnect();
//                        }
//                    }
//                    newslist.add(h);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                String convert="false";
                mydb.insertall(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "All News", "Unread", Insert_DateTime, bitmap, all, trending, topstory, local, international, business, politics, sports, property, technology, entertainment, movies, health, lifestyle, trivia, jobs, from_notification,convert);

//                mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "All News", "Unread", Insert_DateTime, bitmap);
//                mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Unread", "Unread", Insert_DateTime, bitmap);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pdia.dismiss();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("Notification", true);
            editor.putBoolean("HDImage", true);
            editor.putBoolean("firstscreen", true);
            editor.putBoolean("second", true);
            editor.putString("CatId", "0");
            editor.putString("CateName", "All News");
            editor.commit();
            Methodexecute();
        }
    }
}
