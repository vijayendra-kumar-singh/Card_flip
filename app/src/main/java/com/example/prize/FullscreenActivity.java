package com.example.prize;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    FlipView card1, card2, card3;
    int selectedCard;
    RelativeLayout root;

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

        ImageView base = findViewById(R.id.base);

        base.getLayoutParams().height = (int) cardHeight;
        base.getLayoutParams().width = (int) cardWidth;
        RelativeLayout.LayoutParams layoutparamsBase = (RelativeLayout.LayoutParams) base.getLayoutParams();
        layoutparamsBase.setMargins((int) (dpWidth / 12), 0, (int) (dpWidth / 12), (int) (dpHeight / 6));
        base.setLayoutParams(layoutparamsBase);

        card1 = findViewById(R.id.card1);
        card1.getLayoutParams().height = (int) cardHeight;
        card1.getLayoutParams().width = (int) cardWidth;
        RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams) card1.getLayoutParams();
        layoutparams.setMargins(0, 0, (int) (dpWidth / 12), 0);
        card1.setLayoutParams(layoutparams);
        Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        card1.startAnimation(animSlide);

        card2 = findViewById(R.id.card2);
        card2.getLayoutParams().height = (int) cardHeight;
        card2.getLayoutParams().width = (int) cardWidth;
        RelativeLayout.LayoutParams layoutparams2 = (RelativeLayout.LayoutParams) card2.getLayoutParams();
        layoutparams2.setMargins((int) (dpWidth / 12), 0, (int) (dpWidth / 12), (int) (dpHeight / 6));
        card2.setLayoutParams(layoutparams2);
        Animation animSlide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_bottom);
        card2.startAnimation(animSlide2);

        card3 = findViewById(R.id.card3);
        card3.getLayoutParams().height = (int) cardHeight;
        card3.getLayoutParams().width = (int) cardWidth;
        RelativeLayout.LayoutParams layoutparams3 = (RelativeLayout.LayoutParams) card3.getLayoutParams();
        layoutparams3.setMargins((int) (dpWidth / 12), 0, 0, 0);
        card3.setLayoutParams(layoutparams3);
        Animation animSlide3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_right);
        card3.startAnimation(animSlide3);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card1.isFlipEnabled()) {
                    card1.setFlipEnabled(false);
                    animate_to_center(card1);
                    selectedCard = 1;
                    card1.flipTheView(true);
                } else {
                    //TODO : form function to open dialogue
                    Toast.makeText(FullscreenActivity.this, "open dialog", Toast.LENGTH_SHORT).show();
                }
            }
        });


        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card2.isFlipEnabled()) {
                    card2.setFlipEnabled(false);
                    animate_to_center(card2);
                    selectedCard = 2;
                    card2.flipTheView(true);
                } else {
                    //TODO : form function to open dialogue
                    Toast.makeText(FullscreenActivity.this, "open dialog", Toast.LENGTH_SHORT).show();
                }
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card3.isFlipEnabled()) {
                    card3.setFlipEnabled(false);
                    animate_to_center(card3);
                    selectedCard = 3;
                    card3.flipTheView(true);
                } else {
                    //TODO : form function to open dialogue
                    Toast.makeText(FullscreenActivity.this, "open dialog", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //TODO : create a fuction to set values on card as like this

        TextView TextCard1 = card1.findViewById(R.id.backText);
        TextCard1.setText("Back 1");
        TextView TextCard2 = card2.findViewById(R.id.backText);
        TextCard2.setText("Back 2");
        TextView TextCard3 = card3.findViewById(R.id.backText);
        TextCard3.setText("Back 3");
    }

    private void animate_to_center(final FlipView card) {

        AnimationSet set = new AnimationSet(true);
        final AnimationSet set2 = new AnimationSet(true);

        Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        set.addAnimation(zoom);

        root = findViewById(R.id.fullscreen_content);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        card.getLocationOnScreen(originalPos);

        final int xDest = dm.widthPixels / 2 - (card.getMeasuredWidth() / 2);
        final int yDest = dm.heightPixels / 2 - (card.getMeasuredHeight() / 2) - statusBarOffset;

        TranslateAnimation anim = new TranslateAnimation(0, xDest - originalPos[0], 0, yDest - originalPos[1]);
        anim.setDuration(1000);
        set.addAnimation(anim);
        set.setFillAfter(true);
        set.setFillEnabled(true);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAnimationEnd(Animation animation) {
                TextView asd = card.findViewById(R.id.backText);
                asd.setScaleX((float) (asd.getScaleX()*1.3));
                asd.setScaleY((float) (asd.getScaleY()*1.3));
                card.clearAnimation();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(card1.getWidth() * 2, card1.getHeight() * 2);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                card.setLayoutParams(lp);
                if (selectedCard == 1) {
                    card2.flipTheView(true);
                    card3.flipTheView(true);
                    card2.setFlipEnabled(false);
                    card3.setFlipEnabled(false);
                } else if (selectedCard == 2) {
                    card1.flipTheView(true);
                    card3.flipTheView(true);
                    card1.setFlipEnabled(false);
                    card3.setFlipEnabled(false);
                } else {
                    card1.flipTheView(true);
                    card2.flipTheView(true);
                    card1.setFlipEnabled(false);
                    card2.setFlipEnabled(false);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        card.startAnimation(set);

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
