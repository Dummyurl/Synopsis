package com.synopsis;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import static com.synopsis.CommonUtilities.SENDER_ID;
import static com.synopsis.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    AsyncTask<Void, Void, Void> mRegisterTask;
    String regId;
    DBHelper mydb;
    Cursor c;
    String NewsId = "", NewsTitle = "", NewsSummary = "", NewsVideo = "", NewsImage = "", NewsDate = "", News_source = "", news_like, News_url, Share_url, published_by = "", readtype, cattype, related_category;
    Boolean like_status;
    InputStream input;
    Bitmap myBitmap;
    String[] playlists;
    int releted_categoryid;
    String Insert_DateTime;
    byte[] imagebitmap;
    String all = "false", trending = "false", topstory = "false", local = "false", international = "false", business = "false", politics = "false", sports = "false", property = "false", technology = "false", entertainment = "false", movies = "false", health = "false", lifestyle = "false", trivia = "false", jobs = "false", from_notification = "false";

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM", "Your device registred with GCM");
        mydb = new DBHelper(context);
        // ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);
        regId = registrationId;
        mRegisterTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                // Register on our server
                // On server creates a new user
                //ServerUtilities.register(context, name, email, regId);
                JSONParser jParser = new JSONParser();
                // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
                JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "addregistration.php?device_id=" + Splash_Page.android_id + "&reg_id=" + regId+"&location="+Splash_Page.country_name);


                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mRegisterTask = null;
                // Toast.makeText(getApplicationContext(), "New Message: " + regId, Toast.LENGTH_LONG).show();
            }

        };
        mRegisterTask.execute(null, null, null);
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered), getString(R.string.gcm_unregistered));
        //ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getStringExtra("message");
        String title = intent.getStringExtra("title");
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Insert_DateTime = sdf.format(calendar.getTime());
//
//


        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Log.d("df", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName() + "   Package Name :  " + componentInfo.getPackageName());

//        if (taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.synopsis.ScreenSlidePagerActivity")) {
//
//        }
//        else
//        {
        mydb = new DBHelper(context);
        String json = "";
        try {
            InputStream is = new ByteArrayInputStream(message.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        JSONObject jObj = null;
        JSONArray news = null;
        try {

            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        try {
            news = jObj.getJSONArray("news");
        } catch (Exception e) {

        }
//        Splash_Page.newslength = news.length();
        // looping of List
//            if (Splash_Page.newslength != 0)
//                Splash_Page.newslist.clear();
        for (int i = 0; i < news.length(); i++) {
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
//                NewsDate=NewsDate.substring(0,NewsDate.indexOf(" "));
                News_source = c.getString("news_source");
                news_like = c.getString("news_like");
                like_status = c.getBoolean("like_status");
                News_url = c.getString("news_url");
                Share_url = c.getString("share_url");
                published_by = c.getString("published_by");
                related_category = c.getString("related_category");
                Splash_Page.Serverdatetime=c.getString("current_date");
                playlists = related_category.split(",");
                try {


                    if (NewsImage.equalsIgnoreCase("")) {

                    } else {
                        if (NewsImage.contains("base64")) {
                            myBitmap = null;
                            String imageDataBytes = NewsImage.substring(NewsImage.indexOf(",") + 1);

                            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            // opts.inJustDecodeBounds = true;
                            opts.inSampleSize = 1;
                            myBitmap = BitmapFactory.decodeStream(stream, null, opts);
                        } else if (NewsImage.contains("http://images")) {

                        } else {
                            myBitmap = null;
                            URL url = new URL(NewsImage);
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
                if (taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.synopsis.ScreenSlidePagerActivity")) {
                    from_notification = "false";
                } else {
                    from_notification = "true";
                }
                String convert="true";
                mydb.insertall(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "All News", "Unread", Insert_DateTime, myBitmap, all, trending, topstory, local, international, business, politics, sports, property, technology, entertainment, movies, health, lifestyle, trivia, jobs,from_notification,convert);

            } catch (JSONException e) {
                e.printStackTrace();
            }
//
        }
//        }
        SharedPreferences sharedpreferences;
        String MyPREFERENCES = "MyPrefs";
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Boolean notification_check = sharedpreferences.getBoolean("Notification", false);

        if (notification_check) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();

            if (isScreenOn == false) {
                PowerManager pma = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = pma.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                wl.acquire(15000);
                PowerManager.WakeLock wl1 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
                wl1.acquire(10000);
            /* WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
             wl_cpu.acquire(10000);*/
            }
            displayMessage(context, message, title);
            // notifies user
            generateNotification(context, message, title);
        }
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        boolean isScreenOn = pm.isScreenOn();
//
//        if (isScreenOn == false) {
//            PowerManager pma = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            PowerManager.WakeLock wl = pma.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
//            wl.acquire(15000);
//            PowerManager.WakeLock wl1 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
//            wl1.acquire(10000);
//            /* WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
//             wl_cpu.acquire(10000);*/
//        }
//        displayMessage(context, message, title);
//        // notifies user
//        generateNotification(context, message, title);
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        String title = getString(R.string.gcm_deleted, total);

        displayMessage(context, message, title);
        // notifies user
        generateNotification(context, message, title);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId), getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId), getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message, String title) {
        SharedPreferences sharedpreferences;
        String MyPREFERENCES = "MyPrefs";
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Boolean notification_check = sharedpreferences.getBoolean("Notification", false);

        if (notification_check) {
            int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.launchicon : R.drawable.launchicon;
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            int icon2 = R.drawable.launchicon;
//            long when = System.currentTimeMillis();
            Intent intent = new Intent(context, Splash_Page.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, m, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
            Notification.Builder b = new Notification.Builder(context);
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Synopsis");
            inboxStyle.setSummaryText(title);
//            String[] events = new String[6];
//            for (int i=0; i < events.length; i++) {
//                inboxStyle.addLine(events[i]);
//            }
            b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(icon)
                    .setTicker(title)
                    .setContentTitle("Synopsis")
                    .setContentText(title)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent)
//                    .setFullScreenIntent(contentIntent,true)
                    .setContentInfo("Info")
                    .setPriority(Notification.PRIORITY_HIGH);
//                    .setStyle(inboxStyle);
            Notification notification = new Notification.BigTextStyle(b)
                    .bigText(title).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);


////new7888888888888888888888888
//            int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.launchicon : R.drawable.launchicon;
//            long when = System.currentTimeMillis();
//            Notification notification = new Notification(icon, "Custom Notification", when);
//
//            NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
//            contentView.setImageViewResource(R.id.image, R.drawable.launchicon);
////            contentView.setImageViewBitmap(R.id.image, myBitmap);
//            contentView.setTextViewText(R.id.title, "Synopsis");
//            contentView.setTextViewText(R.id.text, title);
//            notification.contentView = contentView;
//
//            Intent notificationIntent = new Intent(this, Splash_Page.class);
//            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//            notification.contentIntent = contentIntent;
//
//            notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
//            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
//            notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
//            notification.defaults |= Notification.DEFAULT_SOUND; // Sound
//            notification.defaults |= Notification.PRIORITY_HIGH;
//            notification.defaults |= Notification.PRIORITY_HIGH;
//            mNotificationManager.notify(1, notification);



//            JSONObject jObj = null;
//            JSONArray news = null;
//            try {
//
//
//                jObj = new JSONObject(message);
//            } catch (JSONException e) {
//                Log.e("JSON Parser", "Error parsing data " + e.toString());
//            }
//            try {
//                news = jObj.getJSONArray("news");
//            } catch (Exception e) {
//
//            }
//            Splash_Page.newslength = news.length();
//            // looping of List
//            if (Splash_Page.newslength != 0)
//                Splash_Page.newslist.clear();
//            for (int i = 0; i < Splash_Page.newslength; i++) {
//                try {
//                    JSONObject c = news.getJSONObject(i);
//                    NewsId = c.getString("id");
//                    NewsTitle = c.getString("title");
//                    NewsSummary = c.getString("summary");
//                    NewsVideo = c.getString("video");
//                    NewsImage = c.getString("image");
//                    NewsDate = c.getString("post_date");
//                    News_source = c.getString("news_source");
//                    news_like = c.getString("news_like");
//                    like_status = c.getBoolean("like_status");
//                    News_url = c.getString("news_url");
//                    Share_url = c.getString("share_url");
//                    published_by = c.getString("published_by");
//
//                    related_category = c.getString("related_category");
//                    playlists = related_category.split(",");
//                    try {
//
//
//                        if (NewsImage.equalsIgnoreCase("")) {
//
//                        } else {
//                            if (NewsImage.contains("base64")) {
//                                myBitmap = null;
//                                String imageDataBytes = NewsImage.substring(NewsImage.indexOf(",") + 1);
//
//                                InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
//                                BitmapFactory.Options opts = new BitmapFactory.Options();
//                                // opts.inJustDecodeBounds = true;
//                                opts.inSampleSize = 1;
//                                myBitmap = BitmapFactory.decodeStream(stream, null, opts);
//                            } else if (NewsImage.contains("http://images")) {
//
//                            } else {
//                                myBitmap = null;
//                                URL url = new URL(NewsImage);
//                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                                connection.setDoInput(true);
//                                connection.setInstanceFollowRedirects(false);
//                                connection.setRequestMethod("GET");
//                                connection.connect();
//                                input = connection.getInputStream();
//
//                                BitmapFactory.Options opts = new BitmapFactory.Options();
//                                // opts.inJustDecodeBounds = true;
//                                opts.inSampleSize = 2;
//                                myBitmap = BitmapFactory.decodeStream(input, null, opts);
//                                connection.disconnect();
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//
//                    }
//                    for (int j = 0; j < playlists.length; j++) {
//                        releted_categoryid = Integer.parseInt(playlists[j]);
//
//                        if (releted_categoryid == 4) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Trending", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 3) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Top Stories", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 2) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Local", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 5) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "International", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 6) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Business and Finance", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 7) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Politics", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 8) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Sports", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 9) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Property", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 10) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Technology", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 199) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Entertainment & Gossip", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 200) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Movies and Series", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 201) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Health/Science", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 202) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Lifestyle", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 203) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Trivia", "Unread", Insert_DateTime, myBitmap);
//                        } else if (releted_categoryid == 204) {
//                            mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Jobs", "Unread", Insert_DateTime, myBitmap);
//                        }
//                    }
//                    mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "All News", "Unread", Insert_DateTime, myBitmap);
//                    mydb.insert(NewsId, NewsTitle, NewsSummary, NewsVideo, NewsImage, NewsDate, News_source, news_like, News_url, Share_url, like_status, published_by, "Unread", "Unread", Insert_DateTime, myBitmap);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }

//            Methodexecute();

//            mydb = new DBHelper(context);
//            mydb.insert("1",title, message ,"fntjhy", "hjmn", "hjn", "gfn", "jh,mj", "", "mguj",true , "jhu", "All News", "Unread", "j,mhjb", bitmap);


//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putBoolean("firstscreen", true);
//            editor.putBoolean("second", true);
//            editor.putString("CatId", "0");
//            editor.putString("CateName", "All News");
//            editor.commit();

        } else {

        }

    }

    public void Methodexecute() {
        c = mydb.getallnewsData();
        Splash_Page.newslength = c.getCount();
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
                Splash_Page.newslist.add(h);

            } catch (IndexOutOfBoundsException e) {

            } catch (OutOfMemoryError e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (c.moveToNext());
//
//        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        int LoginId = sharedpreferences.getInt("LoginId", 0);
//        if (LoginId == 0) {
//            Intent mainIntent = new Intent(Splash_Page.this, SignInPage.class);
//            startActivity(mainIntent);
//            Splash_Page.this.finish();
//        } else {
//            Intent in = new Intent(Splash_Page.this, ScreenSlidePagerActivity.class);
//            startActivity(in);
//            Splash_Page.this.finish();
//        }
    }

}
