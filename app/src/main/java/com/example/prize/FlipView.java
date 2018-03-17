package com.example.prize;

/**
 * Created by Mohan on 17-03-2018.
 */

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FlipView extends FrameLayout {

    public static final int DEFAULT_FLIP_DURATION = 1000;
    private int animFlipHorizontalOutId = R.animator.flip_out;
    private int animFlipHorizontalInId = R.animator.flip_in;

    public enum FlipState {
        FRONT_SIDE, BACK_SIDE
    }

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    private boolean flipOnTouch;
    private int flipDuration;
    private boolean flipEnabled;

    private Context context;

    private FlipState mFlipState = FlipState.FRONT_SIDE;

    private OnFlipAnimationListener onFlipListener = null;

    public FlipView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public FlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Setting Default Values
        flipOnTouch = true;
        flipDuration = DEFAULT_FLIP_DURATION;
        flipEnabled = true;

        if (attrs != null) {
            final TypedArray attrArray =
                    context.obtainStyledAttributes(attrs, R.styleable.flip_view, 0, 0);
            try {
                flipOnTouch = attrArray.getBoolean(R.styleable.flip_view_flipOnTouch, true);
                flipDuration =
                        attrArray.getInt(R.styleable.flip_view_flipDuration, DEFAULT_FLIP_DURATION);
                flipEnabled = attrArray.getBoolean(R.styleable.flip_view_flipEnabled, true);

            } finally {
                attrArray.recycle();
            }
        }

        loadAnimations();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 2) {
            throw new IllegalStateException("FlipView can host only two direct children!");
        }

        findViews();
        changeCameraDistance();
    }

    @Override
    public void addView(View v, int pos, ViewGroup.LayoutParams params) {
        if (getChildCount() == 2) {
            throw new IllegalStateException("FlipView can host only two direct children!");
        }

        super.addView(v, pos, params);

        findViews();
        changeCameraDistance();
    }

    @Override
    public void removeView(View v) {
        super.removeView(v);

        findViews();
    }

    @Override
    public void removeAllViewsInLayout() {
        super.removeAllViewsInLayout();

        // Reset the state
        mFlipState = FlipState.BACK_SIDE;

        findViews();
    }

    private void findViews() {
        mCardBackLayout = null;
        mCardFrontLayout = null;

        int childs = getChildCount();
        if (childs < 1) {
            return;
        }

        if (childs < 2) {
            mFlipState = FlipState.FRONT_SIDE;

            mCardFrontLayout = getChildAt(0);
        } else if (childs == 2) {
            mCardFrontLayout = getChildAt(1);
            mCardBackLayout = getChildAt(0);
        }

        if (!isFlipOnTouch()) {
            mCardFrontLayout.setVisibility(VISIBLE);

            if (mCardBackLayout != null) {
                mCardBackLayout.setVisibility(GONE);
            }
        }
    }

    private void loadAnimations() {
        mSetRightOut =
                (AnimatorSet) AnimatorInflater.loadAnimator(this.context, animFlipHorizontalOutId);
        mSetLeftIn =
                (AnimatorSet) AnimatorInflater.loadAnimator(this.context, animFlipHorizontalInId);
        if (mSetRightOut == null || mSetLeftIn == null) {
            throw new RuntimeException(
                    "No Animations Found! Please set Flip in and Flip out animation Ids.");
        }

        mSetRightOut.removeAllListeners();
        mSetRightOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (mFlipState == FlipState.FRONT_SIDE) {
                    mCardBackLayout.setVisibility(GONE);
                    mCardFrontLayout.setVisibility(VISIBLE);

                    if (onFlipListener != null)
                        onFlipListener.onViewFlipCompleted(FlipView.this, FlipState.FRONT_SIDE);
                } else {
                    mCardBackLayout.setVisibility(VISIBLE);
                    mCardFrontLayout.setVisibility(GONE);

                    if (onFlipListener != null)
                        onFlipListener.onViewFlipCompleted(FlipView.this, FlipState.BACK_SIDE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        setFlipDuration(flipDuration);

    }

    private void changeCameraDistance() {
        int distance = 12000;
        float scale = getResources().getDisplayMetrics().density * distance;

        if (mCardFrontLayout != null) {
            mCardFrontLayout.setCameraDistance(scale);
        }
        if (mCardBackLayout != null) {
            mCardBackLayout.setCameraDistance(scale);
        }
    }

    public void flipTheView() {
        if (!flipEnabled || getChildCount() < 2) return;

        if (mSetRightOut.isRunning() || mSetLeftIn.isRunning()) return;

        mCardBackLayout.setVisibility(VISIBLE);
        mCardFrontLayout.setVisibility(VISIBLE);

        if (mFlipState == FlipState.FRONT_SIDE) {
            // From front to back
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
            mFlipState = FlipState.BACK_SIDE;
        } else {
            // from back to front
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
            mFlipState = FlipState.FRONT_SIDE;
        }

    }

    public void flipTheView(boolean withAnimation) {
        if (getChildCount() < 2) return;

        if (!withAnimation) {
            mSetLeftIn.setDuration(0);
            mSetRightOut.setDuration(0);
            boolean oldFlipEnabled = flipEnabled;
            flipEnabled = true;

            flipTheView();

            mSetLeftIn.setDuration(flipDuration);
            mSetRightOut.setDuration(flipDuration);
            flipEnabled = oldFlipEnabled;
        } else {
            flipTheView();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (flipEnabled) {
            flipTheView(true);
        }
        return super.onTouchEvent(event);
    }

    public boolean isFlipOnTouch() {
        return flipOnTouch;
    }

    public void setFlipOnTouch(boolean flipOnTouch) {
        this.flipOnTouch = flipOnTouch;
    }

    public int getFlipDuration() {
        return flipDuration;
    }

    public void setFlipDuration(int flipDuration) {
        this.flipDuration = flipDuration;

        mSetRightOut.getChildAnimations().get(0).setDuration(flipDuration);
        mSetRightOut.getChildAnimations().get(1).setStartDelay(flipDuration / 2);

        mSetLeftIn.getChildAnimations().get(1).setDuration(flipDuration);
        mSetLeftIn.getChildAnimations().get(2).setStartDelay(flipDuration / 2);

    }

    public boolean isFlipEnabled() {
        return flipEnabled;
    }

    public void setFlipEnabled(boolean flipEnabled) {
        this.flipEnabled = flipEnabled;
    }

    public FlipState getCurrentFlipState() {
        return mFlipState;
    }

    public boolean isFrontSide() {
        return (mFlipState == FlipState.FRONT_SIDE);
    }

    public boolean isBackSide() {
        return (mFlipState == FlipState.BACK_SIDE);
    }

    public OnFlipAnimationListener getOnFlipListener() {
        return onFlipListener;
    }

    public void setOnFlipListener(OnFlipAnimationListener onFlipListener) {
        this.onFlipListener = onFlipListener;
    }


    public interface OnFlipAnimationListener {
        void onViewFlipCompleted(FlipView FlipView, FlipState newCurrentSide);
    }
}