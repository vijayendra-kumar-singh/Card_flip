package com.example.prize;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class FullscreenActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 0;
    private static final int UI_ANIMATION_DELAY = 0;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    double cardWidth, cardHeight;
    float dpWidth, dpHeight;


    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = true;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        hide();

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        cardWidth = 2 * (dpWidth / 3);
        cardHeight = (cardWidth / 63.5) * 88.9;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final ImageView Vmain = findViewById(R.id.voidMain);
        Vmain.getLayoutParams().height = (int) cardHeight;
        Vmain.getLayoutParams().width = (int) cardWidth;
        Vmain.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams Vlayoutparams = (RelativeLayout.LayoutParams) Vmain.getLayoutParams();
        Vlayoutparams.setMargins(0, 0, (int) (dpWidth / 12), 0);
        Vmain.setLayoutParams(Vlayoutparams);

        final ImageView Vmain2 = findViewById(R.id.voidMain2);
        Vmain2.getLayoutParams().height = (int) cardHeight;
        Vmain2.getLayoutParams().width = (int) cardWidth;
        Vmain2.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams Vlayoutparams2 = (RelativeLayout.LayoutParams) Vmain2.getLayoutParams();
        Vlayoutparams2.setMargins((int) (dpWidth / 12), 0, (int) (dpWidth / 12), (int) (dpHeight / 2));
        Vmain2.setLayoutParams(Vlayoutparams2);

        final ImageView Vmain3 = findViewById(R.id.voidMain3);
        Vmain3.getLayoutParams().height = (int) cardHeight;
        Vmain3.getLayoutParams().width = (int) cardWidth;
        Vmain3.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams Vlayoutparams3 = (RelativeLayout.LayoutParams) Vmain3.getLayoutParams();
        Vlayoutparams3.setMargins((int) (dpWidth / 12), 0, 0, 0);
        Vmain3.setLayoutParams(Vlayoutparams3);

        final FrameLayout main = findViewById(R.id.card_back_main_box);
        main.getLayoutParams().height = (int) cardHeight;
        main.getLayoutParams().width = (int) cardWidth;
        RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams) main.getLayoutParams();
        layoutparams.setMargins(0, 0, (int) (dpWidth / 12), 0);
        main.setLayoutParams(layoutparams);
        Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        main.startAnimation(animSlide);

        final FrameLayout main2 = findViewById(R.id.card_back_main2_box);
        main2.getLayoutParams().height = (int) cardHeight;
        main2.getLayoutParams().width = (int) cardWidth;
        RelativeLayout.LayoutParams layoutparams2 = (RelativeLayout.LayoutParams) main2.getLayoutParams();
        layoutparams2.setMargins((int) (dpWidth / 12), 0, (int) (dpWidth / 12), (int) (dpHeight / 2));
        main2.setLayoutParams(layoutparams2);
        Animation animSlide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_bottom);
        main2.startAnimation(animSlide2);

        final FrameLayout main3 = findViewById(R.id.card_back_main3_box);
        main3.getLayoutParams().height = (int) cardHeight;
        main3.getLayoutParams().width = (int) cardWidth;
        RelativeLayout.LayoutParams layoutparams3 = (RelativeLayout.LayoutParams) main3.getLayoutParams();
        layoutparams3.setMargins((int) (dpWidth / 12), 0, 0, 0);
        main3.setLayoutParams(layoutparams3);
        Animation animSlide3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_right);
        main3.startAnimation(animSlide3);

        final Animation goAway = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vmain.setVisibility(View.INVISIBLE);
                Vmain2.setVisibility(View.INVISIBLE);
                Vmain3.setVisibility(View.INVISIBLE);
                main2.startAnimation(goAway);
                main3.startAnimation(goAway);
                main2.setVisibility(View.INVISIBLE);
                main3.setVisibility(View.INVISIBLE);
                main.setEnabled(false);
                main2.setEnabled(false);
                main3.setEnabled(false);
                findViews(R.id.card_front_main, R.id.card_back_main);
                animate_to_center(main);
                loadAnimations();
                changeCameraDistance();
                flipCard();
            }
        });

        main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vmain.setVisibility(View.INVISIBLE);
                Vmain2.setVisibility(View.INVISIBLE);
                Vmain3.setVisibility(View.INVISIBLE);
                main.startAnimation(goAway);
                main3.startAnimation(goAway);
                main.setVisibility(View.INVISIBLE);
                main3.setVisibility(View.INVISIBLE);
                main.setEnabled(false);
                main2.setEnabled(false);
                main3.setEnabled(false);
                findViews(R.id.card_front_main2, R.id.card_back_main2);
                animate_to_center(main2);
                loadAnimations();
                changeCameraDistance();
                flipCard();
            }
        });

        main3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vmain.setVisibility(View.INVISIBLE);
                Vmain2.setVisibility(View.INVISIBLE);
                Vmain3.setVisibility(View.INVISIBLE);
                main.startAnimation(goAway);
                main2.startAnimation(goAway);
                main.setVisibility(View.INVISIBLE);
                main2.setVisibility(View.INVISIBLE);
                main.setEnabled(false);
                main2.setEnabled(false);
                main3.setEnabled(false);
                findViews(R.id.card_front_main3, R.id.card_back_main3);
                animate_to_center(main3);
                loadAnimations();
                changeCameraDistance();
                flipCard();
            }
        });
    }

    private void animate_to_center(FrameLayout splashImage) {

        AnimationSet set = new AnimationSet(true);

        Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        set.addAnimation(zoom);

        RelativeLayout root = findViewById(R.id.fullscreen_content);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        splashImage.getLocationOnScreen(originalPos);

        int xDest = dm.widthPixels / 2;
        xDest -= (splashImage.getMeasuredWidth() / 2);
        int yDest = dm.heightPixels / 2 - (splashImage.getMeasuredHeight() / 2)
                - statusBarOffset;

        TranslateAnimation anim = new TranslateAnimation(0, xDest
                - originalPos[0], 0, yDest - originalPos[1]);
        anim.setDuration(500);
        set.addAnimation(anim);
        set.setFillAfter(true);
        set.setFillEnabled(true);

        splashImage.startAnimation(set);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void changeCameraDistance() {
        int distance = 5000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    private void findViews(int card_front, int card_back) {
        mCardBackLayout = findViewById(card_back);
        mCardFrontLayout = findViewById(card_front);
    }

    public void flipCard() {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }
}
