package com.synopsis;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    public static ToggleButton play_stop;
    public static LinearLayout ll;
    RelativeLayout rlbelow;
//    public static RelativeLayout rlfooter, rlbottom ,rllike;
    String V_url;
//    String newstitle, newssummary, newsaudio, Video_url,news_sortby;
//    TextView title, news, share,shortby,source;
    ProgressBar progressBar;
    ImageView imgclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ll = (LinearLayout) findViewById(R.id.ll);
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        play_stop = (ToggleButton) findViewById(R.id.play_stop);
        rlbelow=(RelativeLayout)findViewById(R.id.rlbelow);
//        rlfooter = (RelativeLayout) findViewById(R.id.rlfooter);
//        rlbottom = (RelativeLayout) findViewById(R.id.rlbottom);
//        rllike = (RelativeLayout) findViewById(R.id.rllike);
//        title = (TextView)findViewById(R.id.txttitle);
//        news = (TextView) findViewById(R.id.txtnews);
//        shortby=(TextView)findViewById(R.id.txtshortby);
//        source=(TextView)findViewById(R.id.textsource);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        imgclose=(ImageView)findViewById(R.id.imgclose);
        Intent in = getIntent();
        V_url = in.getStringExtra("video_url");
//        newstitle = in.getStringExtra("title");
//        newssummary = in.getStringExtra("news");
//        news_sortby = in.getStringExtra("shortby");
        play_stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    player.start();
                    play_stop.setVisibility(View.GONE);
                } else {
                    player.pause();
//                    play_stop.setVisibility(View.VISIBLE);

                }


            }
        });
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, Uri.parse(V_url));//"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        title.setText(newstitle);
//        news.setText(newssummary);
//        shortby.setText(news_sortby);
//        source.setText(news_sortby);


        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        VideoPlayerActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        if (tabletSize) {


//            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            ViewGroup.LayoutParams params = VideoPlayerActivity.ll.getLayoutParams();
            params.height = 450;
            params.width = width;
            VideoPlayerActivity.ll.setLayoutParams(params);
        }
        else
        {
            int width = displaymetrics.widthPixels;
            ViewGroup.LayoutParams params = VideoPlayerActivity.ll.getLayoutParams();
            params.height = 500;
            params.width = width;
            VideoPlayerActivity.ll.setLayoutParams(params);
        }

//        rlbelow.setBackgroundColor(Color.argb(200, 255, 255, 255));
        imgclose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                VideoPlayerActivity.this.finish();
                return true;
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        player.pause();
////        play_stop.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        player.pause();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
        play_stop.setVisibility(View.GONE);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        player.stop();
        play_stop.setVisibility(View.VISIBLE);

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
        progressBar.setVisibility(View.GONE);
        play_stop.setChecked(true);

    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }
    // End VideoMediaController.MediaPlayerControl

}
