package org.devio.rn.splashscreen;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.lang.ref.WeakReference;

/**
 * SplashScreen

 */
public class SplashScreen {
    private static Dialog mSplashDialog;
    private static WeakReference<Activity> mActivity;
    private static VideoView videoview;

    public static void show(final Activity activity, final int themeResId) {
        if (activity == null) return;
        mActivity = new WeakReference<Activity>(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    mSplashDialog = new Dialog(activity, themeResId);
                    mSplashDialog.setContentView(R.layout.splash_layout);

                    mSplashDialog.getWindow().getAttributes().windowAnimations = R.anim.fade_in_dialog;
                    mSplashDialog.setCancelable(false);
                    videoview = (VideoView) mSplashDialog.findViewById(R.id.videoView);

                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    mSplashDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int h = displaymetrics.heightPixels;
                    int w = displaymetrics.widthPixels;
                    videoview.setLayoutParams(new FrameLayout.LayoutParams(w+600, h+600 ));
                    Uri uri=Uri.parse("android.resource://"+mSplashDialog.getContext().getPackageName()+"/"+R.raw.dropvideo);
                    videoview.setVideoURI(uri);
                    //videoview.animate().alpha(0);
                    //videoview.seekTo(0);
                    //videoview.setBackgroundColor(Color.TRANSPARENT); // Your color.
                    videoview.start();


                    if (!mSplashDialog.isShowing()) {
                        mSplashDialog.getWindow().getAttributes().windowAnimations = R.anim.fade_in_dialog;
                        mSplashDialog.show();
                    }
                }
            }
        });
    }


    public static void show(final Activity activity, final boolean fullScreen) {
        int resourceId = fullScreen ? R.style.SplashScreen_Fullscreen : R.style.SplashScreen_SplashTheme;
        show(activity, resourceId);
    }

    public static void show(final Activity activity) {
        show(activity, false);
    }

    public static void hide(Activity activity) {
        if (activity == null) {
            if (mActivity == null) {
                return;
            }
            activity = mActivity.get();
        }

        if (activity == null) return;

        final Activity _activity = activity;

        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSplashDialog != null && mSplashDialog.isShowing()) {
                    boolean isDestroyed = false;
                    mSplashDialog.getWindow().getAttributes().windowAnimations = R.anim.fade_out_dialog;
                            //.setWindowAnimations(
                            //R.style.dialog_animation_fade);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                       // mSplashDialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
                        mSplashDialog.getWindow().getAttributes().windowAnimations = R.anim.fade_out_dialog;
                        isDestroyed = _activity.isDestroyed();
                    }

                    if (!_activity.isFinishing() && !isDestroyed) {
                        ///
                       // mSplashDialog.cancel();
                       // mSplashDialog.getWindow().setWindowAnimations(R.style.dialog_animation_fade);
                        mSplashDialog.getWindow().getAttributes().windowAnimations = R.anim.fade_out_dialog;
                        mSplashDialog.dismiss();
                    }
                    mSplashDialog = null;
                }
            }
        });
    }
}
