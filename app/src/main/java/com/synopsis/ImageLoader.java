package com.synopsis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader{
    Context c;
    public static MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler=new Handler();//handler to display images in UI thread
 Bitmap bitmapimage;
    DBHelper mydb;
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        c=context;
        executorService= Executors.newFixedThreadPool(5);
    }
    
    final int stub_id=R.drawable.aaa;
    public void DisplayImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {

            queuePhoto(url, imageView);
//            imageView.setImageResource(stub_id);
            imageView.setBackgroundResource(R.drawable.aaa);
        }
    }
        
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(String url)
    {
        File f=fileCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
//            ConnectionDetector cd = new ConnectionDetector(ImageLoader.this);
//            if (!cd.isConnectingToInternet()) {
//                return bitmap;
//            }
//            else {
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                Utils.CopyStream(is, os);
                os.close();
                conn.disconnect();
                bitmap = decodeFile(f);
                return bitmap;
//            }
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize=scale;
            o2.inSampleSize=1;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap= BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            Bitmap bmp;
            try{
                if(imageViewReused(photoToLoad))
                    return;
                if(ScreenSlidePagerActivity.ListPosition>2)
                {
                    ScreenSlidePagerActivity.firstimage=false;
                    ScreenSlidePagerActivity.secondimage=false;
                }



                if(ScreenSlidePagerActivity.firstimage)
                {
                    ScreenSlidePagerActivity.firstimage=false;
                     bmp=getBitmap(ScreenSlidePageFragment.f_imgUrl);
                    memoryCache.put(ScreenSlidePageFragment.f_imgUrl, bmp);

                }
                else if(ScreenSlidePagerActivity.secondimage)
                {
                    ScreenSlidePagerActivity.firstimage=true;
                     bmp=getBitmap(ScreenSlidePageFragment.s_imgUrl);
                    memoryCache.put(ScreenSlidePageFragment.s_imgUrl, bmp);

                }
                else
                {
//                     bmp=getBitmap(ScreenSlidePageFragment.N_imgUrl);
//                    memoryCache.put(ScreenSlidePageFragment.N_imgUrl, bmp);
                     bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                }

//                Bitmap bmp=getBitmap(photoToLoad.url);
//                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
//        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p)
        {
            bitmapimage=b;
            photoToLoad=p;
        }
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmapimage!=null) {
                mydb = new DBHelper(c);
                BitmapDrawable ob;
//                photoToLoad.imageView.setImageBitmap(bitmap);
                if(ScreenSlidePagerActivity.ListPosition>2)
                {
                    ScreenSlidePagerActivity.firstimage=false;
                    ScreenSlidePagerActivity.secondimage=false;
                }

                if(ScreenSlidePagerActivity.firstimage)
                {
                    ScreenSlidePagerActivity.firstimage=false;
//                    mydb.updateBitmap(ScreenSlidePageFragment.f_N_id,ScreenSlidePageFragment.f_Cat_type,bitmapimage);
                     ob = new BitmapDrawable(c.getResources(), bitmapimage);
                    photoToLoad.imageView.setBackgroundDrawable(ob);
                }
                else if(ScreenSlidePagerActivity.secondimage)
                {
                    ScreenSlidePagerActivity.secondimage=false;
//                    mydb.updateBitmap(ScreenSlidePageFragment.s_N_id,ScreenSlidePageFragment.s_Cat_type,bitmapimage);
                     ob = new BitmapDrawable(c.getResources(), bitmapimage);
                    photoToLoad.imageView.setBackgroundDrawable(ob);
                }
                else
                {
//                    mydb.updateBitmap(ScreenSlidePageFragment.N_id,ScreenSlidePageFragment.Cat_type,bitmapimage);
                     ob = new BitmapDrawable(c.getResources(), bitmapimage);
                    photoToLoad.imageView.setBackgroundDrawable(ob);
                }
//                mydb.updateBitmap(ScreenSlidePageFragment.N_id,ScreenSlidePageFragment.Cat_type,bitmapimage);
//                BitmapDrawable ob = new BitmapDrawable(c.getResources(), bitmapimage);
//                photoToLoad.imageView.setBackgroundDrawable(ob);
            }
            else {
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
