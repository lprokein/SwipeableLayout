package ua.zabelnikov.swipelayout.layout.frame;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import ua.zabelnikov.swipelayout.R;
import ua.zabelnikov.swipelayout.layout.SwipeGestureManager;
import ua.zabelnikov.swipelayout.layout.listener.OnLayoutPercentageChangeListener;
import ua.zabelnikov.swipelayout.layout.listener.OnLayoutSwipedListener;

public class SwipeableLayout extends FrameLayout {

    private float swipeSpeed;
    private SwipeGestureManager swipeManager;
    private int swipeOrientationMode;

    public SwipeableLayout(Context context) {
        super(context);
    }

    public SwipeableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateAttributes(context, attrs);
    }

    public SwipeableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateAttributes(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflateAttributes(context, attrs);
    }

    private void inflateAttributes(Context context, AttributeSet attributeSet) {

        swipeSpeed = 1.0f;
        swipeOrientationMode = SwipeGestureManager.OrientationMode.UP_BOTTOM;

        if (attributeSet != null) {
            TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.SwipeableLayout);
            try {
                swipeSpeed = attributes.getFloat(R.styleable.SwipeableLayout_swipeSpeed, swipeSpeed);
                swipeOrientationMode = attributes.getInt(R.styleable.SwipeableLayout_swipeOrientation, swipeOrientationMode);
            } finally {
                attributes.recycle();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return swipeManager.onTouch(this, ev);
    }

    public void initSwipeManager() {
        SwipeGestureManager.Builder builder = new SwipeGestureManager.Builder(getContext());
        builder.setSwipeSpeed(swipeSpeed);
        builder.setOrientationMode(swipeOrientationMode);
        builder.setStartCoordinates(getX(), getY());
        swipeManager = builder.create();
        swipeManager.setOnLayoutPercentageChangeListener(onLayoutPercentageChangeListener);
        swipeManager.setOnSwipedListener(onLayoutSwipedListener);
        this.setOnTouchListener(swipeManager);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSwipeManager();
    }

    public void addBlock(int orientationMode) {
        swipeManager.addBlock(orientationMode);
    }

    public void removeBlock(int orientationMode) {
        swipeManager.removeBlock(orientationMode);
    }

    private OnLayoutSwipedListener onLayoutSwipedListener;
    private OnLayoutPercentageChangeListener onLayoutPercentageChangeListener;

    public void setOnSwipedListener(OnLayoutSwipedListener onLayoutSwipedListener) {
        this.onLayoutSwipedListener = onLayoutSwipedListener;
    }

    public void setOnLayoutPercentageChangeListener(OnLayoutPercentageChangeListener onLayoutPercentageChangeListener) {
        this.onLayoutPercentageChangeListener = onLayoutPercentageChangeListener;
    }
}
