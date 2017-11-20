package com.synopsis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class InstructionsPage extends AppCompatActivity {
    RelativeLayout rlback;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String NewsId = "", NewsTitle = "", NewsSummary = "", NewsVideo = "", NewsImage = "", NewsDate = "", News_source = "", news_like, News_url,Share_url, published_by = "",cattype;
    Boolean like_status;
    Cursor c;
    DBHelper mydb;
    float x1,x2;
    float y1, y2;
    byte[] imagebitmap;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_page);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        rlback = (RelativeLayout) findViewById(R.id.rlback);
        mydb = new DBHelper(InstructionsPage.this);
        c = mydb.getallnewsData();
        executemethod();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                Intent in = new Intent(InstructionsPage.this, ScreenSlidePagerActivity.class);
////                startActivity(in);
//            }
//        }, 1000);
    }
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                //if left to right sweep event on screen
                Intent in = new Intent(InstructionsPage.this, ScreenSlidePagerActivity.class);
                if (x1 < x2)
                {

                startActivity(in);
                    InstructionsPage.this.finish();
//                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if right to left sweep event on screen
                else if (x1 > x2)
                {
                    startActivity(in);
                    InstructionsPage.this.finish();
//                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if UP to Down sweep event on screen
                else if (y1 < y2)
                {
                    startActivity(in);
                    InstructionsPage.this.finish();
//                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }

                //if Down to UP sweep event on screen
                else if (y1 > y2)
                {
                    startActivity(in);
                    InstructionsPage.this.finish();
//                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }


    public void executemethod() {
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
            like_status = Boolean.parseBoolean(c.getString(c.getColumnIndex("likestatus")));
            News_url = c.getString(c.getColumnIndex("newsurl"));
            Share_url= c.getString(c.getColumnIndex("shareurl"));
            published_by = c.getString(c.getColumnIndex("published_by"));
            cattype=c.getString(c.getColumnIndex("category"));
            imagebitmap=c.getBlob(c.getColumnIndex("newsimage"));
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
            Splash_Page.newslist.add(h);
        } while (c.moveToNext());

    }
}
