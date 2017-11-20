package com.synopsis;

//import android.support.v4.view.ViewPager;
//import android.view.View;
//
//public class DepthPageTransformer implements ViewPager.PageTransformer {
//    private static float MIN_SCALE = 0.75f;
//
//    public void transformPage(View view, float position) {
//        int pageWidth = view.getWidth();
//        int pageHeight = view.getHeight();
//
//        ScreenSlidePagerActivity.toolbar.setVisibility(View.GONE);
//        ScreenSlidePagerActivity.toolbar.setActivated(false);
////        ScreenSlidePageFragment.popupWindow.dismiss();
////        ScreenSlidePageFragment.video_player_view.setMediaController(null);
////        ScreenSlidePageFragment.video_player_view.stopPlayback();
//        //ScreenSlidePageFragment.media_Controller.setVisibility(View.GONE);
////        if (position >= -1 && position <= 1) { // [-1,1]
////            // Counteract the default slide transition
////            view.setTranslationX(pageWidth * -position);
////
////            //set Y position to swipe in from top
////            float yPosition = position * pageHeight;
////            view.setTranslationY(yPosition);
////            view.setScaleX(1);
////            view.setScaleY(1);
////        }
//
//
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            view.setAlpha(0);
//
//        } else if (position <= 0) { // [-1,0]
//            // Use the default slide transition when moving to the left page
//            view.setAlpha(1);
//            view.setTranslationX(0);
//
//
//        } else if (position <= 1) { // (0,1]
//            // Fade the page out.
//            //  ScreenSlidePageFragment.player.pause();
//            view.setAlpha(1 - position);
//
//            // Counteract the default slide transition
//            view.setTranslationX(pageWidth * -position);
//
//            // Scale the page down (between MIN_SCALE and 1)
//            float scaleFactor = MIN_SCALE
//                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//            float yPosition = position * view.getHeight();
//            view.setTranslationY(yPosition);
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setAlpha(0);
//        }
//    }
//
////public void transformPage(View view, float position) {
////            int pageWidth = view.getWidth();
////
////            if (position < -1) { // [-Infinity,-1)
////                // This page is way off-screen to the left.
////                view.setAlpha(0);
////
////            } else if (position <= 0) { // [-1,0]
////                // Use the default slide transition when moving to the left page
////                view.setAlpha(1);
////                view.setTranslationX(0);
////                view.setScaleX(1);
////                view.setScaleY(1);
////
////            } else if (position <= 1) { // (0,1]
////                // Fade the page out.
////                view.setAlpha(1 - position);
////
////                // Counteract the default slide transition
////                view.setTranslationX(pageWidth * -position);
////
////                // Scale the page down (between MIN_SCALE and 1)
////                float scaleFactor = MIN_SCALE
////                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
////                view.setScaleX(scaleFactor);
////                view.setScaleY(scaleFactor);
////
////            } else { // (1,+Infinity]
////                // This page is way off-screen to the right.
////                view.setAlpha(0);
////            }
////
////    }
//
//}
import android.support.v4.view.ViewPager;
import android.view.View;

public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        ScreenSlidePagerActivity.toolbar.setVisibility(View.GONE);
        ScreenSlidePagerActivity.toolbar.setActivated(false);
//        if (ScreenSlidePageFragment.t1 != null) {
//            ScreenSlidePageFragment.t1.stop();
//            // t1.shutdown();
//        }
//       ScreenSlidePageFragment.isChecked=true;
//        float alpha = 0;
//        if (0 <= position && position <= 1)
//        {
//            view.setTranslationX(view.getWidth() * -position);
//            alpha = 1 - position;
//            float scaleFactor = MIN_SCALE
//                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//            view.setAlpha(alpha);
//        }
//        else if (-1 < position && position < 0)
//        {
//
//            alpha = position + 1;
//            view.setTranslationY(position * view.getHeight());
//            view.setTranslationX(view.getWidth() * -position);
//            view.setScaleX(1);
//            view.setScaleY(1);
////            view.setAlpha(alpha);
//        }
        view.setTranslationY(position * view.getHeight());
            view.setTranslationX(view.getWidth() * -position);
//        view.setAlpha(alpha);
//        view.setTranslationX(view.getWidth() * -position);
//        float yPosition = position * view.getHeight();
//        view.setTranslationY(yPosition);




//        int pageWidth = view.getWidth();
//
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            view.setAlpha(0);
//
//        } else if (position <= 0) { // [-1,0]
//            // Use the default slide transition when moving to the left page
//            view.setAlpha(1);
//            view.setTranslationX(0);
//            view.setScaleX(1);
//            view.setScaleY(1);
//
//        } else if (position <= 1) { // (0,1]
//            // Fade the page out.
//            view.setAlpha(1 - position);
//
//            // Counteract the default slide transition
//            view.setTranslationX(pageWidth * -position);
//
//            // Scale the page down (between MIN_SCALE and 1)
//            float scaleFactor = MIN_SCALE
//                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setAlpha(0);
//        }
    }
}