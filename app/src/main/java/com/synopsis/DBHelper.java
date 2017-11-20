package com.synopsis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "MyDBName.db", null, 1);
    }

    String selectQuery;
    Cursor c;
    int tablelength;
    String First_NewsId, D_NewsId, old_NewsId;
    boolean contain = false;
    byte[] img;
    ArrayList array_list;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table allnews " +
                        "(id integer primary key, newsid text, newstitle text, newssummary text,newsvideo text,newsimageurl text," +
                        "newsdate date,newssource text, newslike text ,newsurl text ,shareurl text,likestatus boolean,published_by text," +
                        "readtype text,category txt,datetime txt,newsimage blob,all_cat txt,trending_cat txt,topstory_cat txt,local_cat txt," +
                        "international_cat txt,business_cat txt,politics_cat txt,sport_cat txt,property_cat txt,technology_cat txt,entertainment_cat txt," +
                        "movie_cat txt,health_cat txt,lifestyle_cat txt,trivia_cat txt,jobs_cat txt,from_notification txt,convert txt)"
        );

        db.execSQL(
                "create table bookmark " +
                        "(id integer primary key, newsid text, newstitle text, newssummary text,newsvideo text,newsimageurl text, newsdate text,newssource text, newslike text ,newsurl text,shareurl text ,likestatus boolean,published_by text,readtype text,category txt,datetime txt,newsimage blob)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertall(String NewsId, String NewsTitle, String NewsSummary, String NewsVideo, String NewsImageUrl, String NewsDate, String News_source, String news_like, String News_url, String Share_url, Boolean Like_status, String published_by, String Cat_Name, String ReadType, String InsertDate, Bitmap NewsImage, String All, String Trending, String Topstory, String Local, String International, String Business, String Politics, String Sports, String Property, String Technology, String Entertainment, String Movies, String Health, String Lifestyle, String Trivia, String Jobs, String From_notification, String Convert) {
        if (NewsImageUrl.equalsIgnoreCase("")) {

        } else {
            if (NewsImage == null) {

            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                NewsImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                img = bos.toByteArray();
            }
        }
        contain = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("newsid", NewsId);
        values.put("newstitle", NewsTitle);
        values.put("newssummary", NewsSummary);
        values.put("newsvideo", NewsVideo);
        values.put("newsimageurl", NewsImageUrl);
        values.put("newsdate", NewsDate);
        values.put("newssource", News_source);
        values.put("newslike", news_like);
        values.put("newsurl", News_url);
        values.put("shareurl", Share_url);
        values.put("likestatus", Like_status);
        values.put("published_by", published_by);
        values.put("readtype", ReadType);
        values.put("category", Cat_Name);
        values.put("datetime", InsertDate);
        values.put("newsimage", img);
        values.put("all_cat", All);
        values.put("trending_cat", Trending);
        values.put("topstory_cat", Topstory);
        values.put("local_cat", Local);
        values.put("international_cat", International);
        values.put("business_cat", Business);
        values.put("politics_cat", Politics);
        values.put("sport_cat", Sports);
        values.put("property_cat", Property);
        values.put("technology_cat", Technology);
        values.put("entertainment_cat", Entertainment);
        values.put("movie_cat", Movies);
        values.put("health_cat", Health);
        values.put("lifestyle_cat", Lifestyle);
        values.put("trivia_cat", Trivia);
        values.put("jobs_cat", Jobs);
        values.put("from_notification", From_notification);
        values.put("convert", Convert);
        if (Cat_Name.equalsIgnoreCase("All News")) {
            c = getallnewsDatamatch(NewsId);
            if (c != null) {
                c.moveToFirst();
            }
            if (c.getCount() > 0) {
                do {
                    D_NewsId = c.getString(c.getColumnIndex("newsid"));
                    if (NewsId.equalsIgnoreCase(D_NewsId)) {
                        contain = true;
                    }
                } while (c.moveToNext());

                if (!contain) {

                    db.insert("allnews", null, values);
                } else {
                    db.update("allnews", values, "newsid=" + NewsId, null);
                }

            } else {
                db.insert("allnews", null, values);
            }




//            db.insert("allnews", null, values);
            array_list = getalldatadate();
            for (int i = 0; i < array_list.size(); i++) {
                db.delete("allnews", "newsid = ? ", new String[]{(String) array_list.get(i)});
            }

        }
        return true;
    }


    public boolean insertbookmarks(String NewsId, String NewsTitle, String NewsSummary, String NewsVideo, String NewsImageUrl, String NewsDate, String News_source, String news_like, String News_url, String Share_url, Boolean Like_status, String published_by, String Cat_Name, String ReadType, String InsertDate, Bitmap NewsImage) {
        if (NewsImageUrl.equalsIgnoreCase("")) {

        } else {
            if (NewsImage == null) {

            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                NewsImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                img = bos.toByteArray();
            }
        }
        contain = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("newsid", NewsId);
        values.put("newstitle", NewsTitle);
        values.put("newssummary", NewsSummary);
        values.put("newsvideo", NewsVideo);
        values.put("newsimageurl", NewsImageUrl);
        values.put("newsdate", NewsDate);
        values.put("newssource", News_source);
        values.put("newslike", news_like);
        values.put("newsurl", News_url);
        values.put("shareurl", Share_url);
        values.put("likestatus", Like_status);
        values.put("published_by", published_by);
        values.put("readtype", ReadType);
        values.put("category", Cat_Name);
        values.put("datetime", InsertDate);
        values.put("newsimage", img);
        if (Cat_Name.equalsIgnoreCase("Saved Article/Bookmarks")) {
            c = getbookmarkData();
            tablelength = c.getCount();
            if (tablelength > 100) {
                c.moveToFirst();
                First_NewsId = c.getString(c.getColumnIndex("newsid"));
                db.delete("bookmark", "newsid = ? ", new String[]{First_NewsId});
            }

            db.insert("bookmark", null, values);
        }

        return true;
    }

    public Cursor getallnewsDatamatch(String NewsId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from allnews where newsid='"+NewsId+"'", null);
        return res;
    }


    public Cursor getallnewsData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from allnews where from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getallnewsDataconverted() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from allnews where from_notification='true' and convert='false' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor gettrendingData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from allnews where trending_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor gettopstoryData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from topstory ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where topstory_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getbookmarkData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from bookmark", null);
        return res;
    }

    public Cursor getunreadData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from unread ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where readtype='Unread' and from_notification='true' ORDER BY  newsdate DESC", null);

        return res;
    }

    public Cursor getlocalData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from local ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where local_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getinternationalData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from international ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where international_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getbusinessandfinanceData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from businessandfinance ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where business_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getpoliticsData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from politics ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where politics_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getsportsData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from sports ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where sport_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getpropertyData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from property", null);
        Cursor res = db.rawQuery("select * from allnews where property_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor gettechnologyData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from technology ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where technology_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getentertainmentData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from entertainment ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where entertainment_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getmoviesData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from movies ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where movie_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor gethealthData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from health ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where health_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getlifestyleData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from lifestyle ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where lifestyle_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor gettriviaData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from trivia ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where trivia_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public Cursor getjobsData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from jobs ORDER BY  newsdate DESC", null);
        Cursor res = db.rawQuery("select * from allnews where jobs_cat='true' and from_notification='true' ORDER BY  newsdate DESC", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "contacts");
        return numRows;
    }

    public boolean readupdate(String newsid, String readtype, String Cat_Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("readtype", readtype);
        if (Cat_Name.equalsIgnoreCase("All News")) {
            db.update("allnews", contentValues, "newsid=" + newsid, null);
        }

        return true;
    }
//    public boolean update(String newsid, String readtype, String Cat_Name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("readtype", readtype);
//        if (Cat_Name.equalsIgnoreCase("All News")) {
//            db.update("allnews", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Trending")) {
//            db.update("trending", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Top Stories")) {
//            db.update("topstory", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Saved Article/Bookmarks")) {
//            db.update("bookmark", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Unread")) {
//            db.update("unread", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Local")) {
//            db.update("local", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("International")) {
//            db.update("international", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Business and Finance")) {
//            db.update("businessandfinance", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Politics")) {
//            db.update("politics", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Sports")) {
//            db.update("sports", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Property")) {
//            db.update("property", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Technology")) {
//            db.update("technology", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Entertainment & Gossip")) {
//            db.update("entertainment", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Movies and Series")) {
//            db.update("movies", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Health/Science")) {
//            db.update("health", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Lifestyle")) {
//            db.update("lifestyle", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Trivia")) {
//            db.update("trivia", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Jobs")) {
//            db.update("jobs", contentValues, "newsid=" + newsid, null);
//        }
//
//        return true;
//    }

    public boolean updateBitmap(String newsid, String Cat_Name, Bitmap imagebitmap,String Convert) {
        if (imagebitmap == null) {

        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            img = bos.toByteArray();
        }


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("newsimage", img);
        contentValues.put("convert",Convert);
        if (Cat_Name.equalsIgnoreCase("All News")) {
            db.update("allnews", contentValues, "newsid=" + newsid, null);
        }
//        else if (Cat_Name.equalsIgnoreCase("Trending")) {
//            db.update("trending", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Top Stories")) {
//            db.update("topstory", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Saved Article/Bookmarks")) {
//            db.update("bookmark", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Unread")) {
//            db.update("unread", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Local")) {
//            db.update("local", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("International")) {
//            db.update("international", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Business and Finance")) {
//            db.update("businessandfinance", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Politics")) {
//            db.update("politics", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Sports")) {
//            db.update("sports", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Property")) {
//            db.update("property", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Technology")) {
//            db.update("technology", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Entertainment & Gossip")) {
//            db.update("entertainment", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Movies and Series")) {
//            db.update("movies", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Health/Science")) {
//            db.update("health", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Lifestyle")) {
//            db.update("lifestyle", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Trivia")) {
//            db.update("trivia", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Jobs")) {
//            db.update("jobs", contentValues, "newsid=" + newsid, null);
//        }

        return true;
    }

    //    public boolean updateunread(String newsid, String readtype) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("readtype", readtype);
//        db.update("unread", contentValues, "newsid=" + newsid, null);
//
//        return true;
//    }
    public boolean updateLike(String newsid, String like, boolean like_status, String Cat_Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("likestatus", like_status);
        contentValues.put("newslike", like);
        if (Cat_Name.equalsIgnoreCase("All News")) {
            db.update("allnews", contentValues, "newsid=" + newsid, null);
        }
//        else if (Cat_Name.equalsIgnoreCase("Trending")) {
//            db.update("trending", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Top Stories")) {
//            db.update("topstory", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Saved Article/Bookmarks")) {
//            db.update("bookmark", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Unread")) {
//            db.update("unread", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Local")) {
//            db.update("local", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("International")) {
//            db.update("international", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Business and Finance")) {
//            db.update("businessandfinance", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Politics")) {
//            db.update("politics", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Sports")) {
//            db.update("sports", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Property")) {
//            db.update("property", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Technology")) {
//            db.update("technology", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Entertainment & Gossip")) {
//            db.update("entertainment", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Movies and Series")) {
//            db.update("movies", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Health/Science")) {
//            db.update("health", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Lifestyle")) {
//            db.update("lifestyle", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Trivia")) {
//            db.update("trivia", contentValues, "newsid=" + newsid, null);
//        } else if (Cat_Name.equalsIgnoreCase("Jobs")) {
//            db.update("jobs", contentValues, "newsid=" + newsid, null);
//        }

        return true;
    }

    public Integer deleteBookmark(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("bookmark",
                "newsid = ? ",
                new String[]{Integer.toString(id)});
    }

//    public Integer deleteletest(Integer id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("letest",
//                "newsid = ? ",
//                new String[]{Integer.toString(id)});
//    }

    //    public ArrayList<String> getunread(){
//    ArrayList<String> array_list = new ArrayList<String>();
//    SQLiteDatabase db = this.getReadableDatabase();
//        String selectQuery = "SELECT readtype FROM local where readtype='"+"Unread"+"'";
//    Cursor res = db.rawQuery(selectQuery, null);
//    res.moveToFirst();
//
//    while (res.isAfterLast() == false) {
//        array_list.add(res.getString(res.getColumnIndex("readtype")));
//        res.moveToNext();
//    }
//    return array_list;
//}
    public ArrayList<String> getunread(String Cat_Name) {
        if (Cat_Name.equalsIgnoreCase("All News")) {
//            selectQuery = "SELECT readtype FROM allnews where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where all_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Trending")) {
//            selectQuery = "SELECT readtype FROM trending where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where trending_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Top Stories")) {
//            selectQuery = "SELECT readtype FROM topstory where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where topstory_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Saved Article/Bookmarks")) {
//            selectQuery = "SELECT readtype FROM bookmark where readtype='" + "Unread" + "'";
//            selectQuery = "SELECT readtype FROM allnews where trending_cat='true' and readtype='" + "Unread" + "'";
        } else if (Cat_Name.equalsIgnoreCase("Unread")) {
//            selectQuery = "SELECT readtype FROM unread where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where all_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Local")) {
//            selectQuery = "SELECT readtype FROM local where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where local_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("International")) {
//            selectQuery = "SELECT readtype FROM international where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where international_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Business and Finance")) {
//            selectQuery = "SELECT readtype FROM businessandfinance where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where business_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Politics")) {
//            selectQuery = "SELECT readtype FROM politics where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where politics_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Sports")) {
//            selectQuery = "SELECT readtype FROM sports where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where sport_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Property")) {
//            selectQuery = "SELECT readtype FROM property where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where property_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Technology")) {
//            selectQuery = "SELECT readtype FROM technology where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where technology_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Entertainment & Gossip")) {
//            selectQuery = "SELECT readtype FROM entertainment where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where entertainment_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Movies and Series")) {
//            selectQuery = "SELECT readtype FROM movies where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where movie_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Health/Science")) {
//            selectQuery = "SELECT readtype FROM health where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where health_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Lifestyle")) {
//            selectQuery = "SELECT readtype FROM lifestyle where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where lifestyle_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Trivia")) {
//            selectQuery = "SELECT readtype FROM trivia where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where trivia_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        } else if (Cat_Name.equalsIgnoreCase("Jobs")) {
//            selectQuery = "SELECT readtype FROM jobs where readtype='" + "Unread" + "'";
            selectQuery = "SELECT readtype FROM allnews where jobs_cat='true' and readtype='" + "Unread" + "'" + "and from_notification='true'";
        }

        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("readtype")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getalldatadate() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();

        if (!Splash_Page.Serverdatetime.equalsIgnoreCase("")) {
            try {
//                Splash_Page.Serverdatetime=Splash_Page.Serverdatetime.substring(0,Splash_Page.Serverdatetime.indexOf(" "));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = formatter.parse(Splash_Page.Serverdatetime);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, -5);
                Date date1 = calendar.getTime();
                Splash_Page.Serverdatetime = formatter.format(date1);

            } catch (ParseException e1) {
                e1.printStackTrace();
            }


            Cursor res = db.rawQuery("select newsid from allnews where from_notification='true' and newsdate < '" + Splash_Page.Serverdatetime + "'", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("newsid")));
                res.moveToNext();
            }
        }
        return array_list;
    }


    public ArrayList<String> getalldata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select newsid from allnews where all_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

//    public ArrayList<String> getletestdataString(String Cat_Name) {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        Date date = new Date(System.currentTimeMillis() - (20 * 60 * 60 * 1000));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//         String datetime = sdf.format(date);
//        Cursor res = null;
//        SQLiteDatabase db = this.getReadableDatabase();
//        if (Cat_Name.equalsIgnoreCase("All News")) {
//             res = db.rawQuery("select newsid from allnews where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Trending")) {
//            res = db.rawQuery("select newsid from trending where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Top Stories")) {
//            res = db.rawQuery("select newsid from topstory where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Saved Article/Bookmarks")) {
//            res = db.rawQuery("select newsid from bookmark where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Unread")) {
//            res = db.rawQuery("select newsid from unread where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Local")) {
//            res = db.rawQuery("select newsid from local where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("International")) {
//            res = db.rawQuery("select newsid from international where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Business and Finance")) {
//            res = db.rawQuery("select newsid from businessandfinance where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Politics")) {
//            res = db.rawQuery("select newsid from politics where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Sports")) {
//            res = db.rawQuery("select newsid from sports where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Property")) {
//            res = db.rawQuery("select newsid from property where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Technology")) {
//            res = db.rawQuery("select newsid from technology where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Entertainment & Gossip")) {
//            res = db.rawQuery("select newsid from entertainment where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Movies and Series")) {
//            res = db.rawQuery("select newsid from movies where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Health/Science")) {
//            res = db.rawQuery("select newsid from health where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Lifestyle")) {
//            res = db.rawQuery("select newsid from lifestyle where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Trivia")) {
//            res = db.rawQuery("select newsid from trivia where datetime >=datetime", null);
//        } else if (Cat_Name.equalsIgnoreCase("Jobs")) {
//            res = db.rawQuery("select newsid from jobs where datetime >=datetime", null);
//        }
//
//        res.moveToFirst();
//
//        while (res.isAfterLast() == false) {
//            array_list.add(res.getString(res.getColumnIndex("newsid")));
//            res.moveToNext();
//        }
//        return array_list;
//    }

    public ArrayList<String> gettrendingdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from trending", null);
        Cursor res = db.rawQuery("select newsid from allnews where trending_cat='true' and from_notification='true'", null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> gettopstorydata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from topstory", null);
        Cursor res = db.rawQuery("select newsid from allnews where topstory_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getbookmarkdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select newsid from bookmark", null);
//        Cursor res = db.rawQuery("select newsid from allnews where trending_cat='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

//    public ArrayList<String> getunreaddata() {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from unread", null);
//        res.moveToFirst();
//
//        while (res.isAfterLast() == false) {
//            array_list.add(res.getString(res.getColumnIndex("newsid")));
//            res.moveToNext();
//        }
//        return array_list;
//    }

    public ArrayList<String> getlocaldata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from local", null);
        Cursor res = db.rawQuery("select newsid from allnews where local_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getinternationaldata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from international", null);
        Cursor res = db.rawQuery("select newsid from allnews where international_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getbusinessdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from businessandfinance", null);
        Cursor res = db.rawQuery("select newsid from allnews where business_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getpoliticsdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from politics", null);
        Cursor res = db.rawQuery("select newsid from allnews where politics_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getsportsdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from sports", null);
        Cursor res = db.rawQuery("select newsid from allnews where sport_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getpropertydata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from property", null);
        Cursor res = db.rawQuery("select newsid from allnews where property_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> gettechnologydata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from technology", null);
        Cursor res = db.rawQuery("select newsid from allnews where technology_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getentertainmentdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from entertainment", null);
        Cursor res = db.rawQuery("select newsid from allnews where entertainment_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getmoviesdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from movies", null);
        Cursor res = db.rawQuery("select newsid from allnews where movie_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> gethealthdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from health", null);
        Cursor res = db.rawQuery("select newsid from allnews where health_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getlifestyledata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from lifestyle", null);
        Cursor res = db.rawQuery("select newsid from allnews where lifestyle_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> gettriviadata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from trivia", null);
        Cursor res = db.rawQuery("select newsid from allnews where trivia_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getjobsdata() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select newsid from jobs", null);
        Cursor res = db.rawQuery("select newsid from allnews where jobs_cat='true' and from_notification='true'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("newsid")));
            res.moveToNext();
        }
        return array_list;
    }


}