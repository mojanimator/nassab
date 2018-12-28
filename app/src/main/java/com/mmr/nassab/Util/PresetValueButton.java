package com.mmr.nassab.Util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmr.nassab.R;

import java.util.ArrayList;

/**
 * Created by Mojtaba Rajabi on 18/12/2018.
 */

public class PresetValueButton extends LinearLayout implements RadioCheckable {
    private final Context ctx;
    // Views
    private TextView tvTitle, tvCount;

    // Constants
    public static final int DEFAULT_TEXT_COLOR = Color.TRANSPARENT;

    // Attribute Variables
    private String mTitle;
    private String mCount;
    private int mTitleTextColor;
    private int mCountTextColor;
    private int mPressedTextColor;

    // Variables
    private Drawable mInitialBackgroundDrawable;
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private boolean mChecked;
    private ArrayList<OnCheckedChangeListener> mOnCheckedChangeListeners = new ArrayList<>();
    private View view;


    //================================================================================
    // Constructors
    //================================================================================

    public PresetValueButton(Context context) {
        super(context);
        this.ctx = context;
        setupView();
    }

    public PresetValueButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PresetValueButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PresetValueButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.ctx = context;
        parseAttributes(attrs);
        setupView();
    }

    //================================================================================
    // Init & inflate methods
    //================================================================================

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.PresetValueButton, 0, 0);
        Resources resources = getContext().getResources();
        try {
            mTitle = a.getString(R.styleable.PresetValueButton_titleText);
            mCount = a.getString(R.styleable.PresetValueButton_countText);
            mTitleTextColor = a.getColor(R.styleable.PresetValueButton_titleTextColor, resources.getColor(R.color.colorTextDisable));
            mPressedTextColor = a.getColor(R.styleable.PresetValueButton_pressedTextColor, resources.getColor(R.color.colorPrimaryDark));
            mCountTextColor = a.getColor(R.styleable.PresetValueButton_countTextColor, resources.getColor(R.color.colorPrimaryDark));
        } finally {
            a.recycle();
        }
    }

    // Template method
    private void setupView() {
        inflateView();
        bindView();
        setCustomTouchListener();
    }

    protected void inflateView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.custom_tab, this, true);
        tvTitle = findViewById(R.id.tv_title);
        tvCount = findViewById(R.id.tv_count);
        mInitialBackgroundDrawable = getBackground();
//        mInitialBackgroundDrawable = ContextCompat.getDrawable(ctx, R.drawable.unselected_background);
//        view.setBackground(mInitialBackgroundDrawable);
    }

    protected void bindView() {
        if (mTitleTextColor != DEFAULT_TEXT_COLOR) {
            tvTitle.setTextColor(mTitleTextColor);
        }
        if (mCountTextColor != DEFAULT_TEXT_COLOR) {
            tvCount.setTextColor(mCountTextColor);
        }
        tvTitle.setText(mTitle);
        tvCount.setText(mCount);
    }

    //================================================================================
    // Overriding default behavior
    //================================================================================

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    private void setCustomTouchListener() {
        super.setOnTouchListener(new TouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    private void onTouchDown(MotionEvent motionEvent) {
        setChecked(true);
    }

    private void onTouchUp(MotionEvent motionEvent) {
        // Handle user defined click listeners
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }
    //================================================================================
    // Public methods
    //================================================================================

    public void setCheckedState() {
//        setBackgroundResource(R.drawable.primary_background);
        tvTitle.setTextColor(mPressedTextColor);
//        tvCount.setTextColor(mPressedTextColor);

    }


    public void setNormalState() {
        setBackgroundResource(R.drawable.primary_background);
//        setBackgroundDrawable(mInitialBackgroundDrawable);
        tvTitle.setTextColor(mTitleTextColor);
        tvCount.setTextColor(mCountTextColor);
        tvCount.setBackgroundResource(R.drawable.primary_background);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String value) {
        mTitle = value;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
        tvCount.setText(mCount);
    }

    //================================================================================
    // Checkable implementation
    //================================================================================

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (!mOnCheckedChangeListeners.isEmpty()) {
                for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                    mOnCheckedChangeListeners.get(i).onCheckedChanged(this, mChecked);
                }
            }
            if (mChecked) {
                setCheckedState();
            } else {
                setNormalState();
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.add(onCheckedChangeListener);
    }

    @Override
    public void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.remove(onCheckedChangeListener);
    }

    //================================================================================
    // Inner classes
    //================================================================================
    private final class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(event);
                    setCheckedBackground(v.getId());

                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
    }

    private void setCheckedBackground(int id) {

        if (id == R.id.tab_not_Installed) {
            setBackgroundResource(R.drawable.uninstalled_background);
            tvCount.setBackgroundResource(R.drawable.uninstalled_background);
        } else if (id == R.id.tab_installed) {
            setBackgroundResource(R.drawable.secondary_background);
            tvCount.setBackgroundResource(R.drawable.secondary_background);
        } else if (id == R.id.tab_installing) {
            setBackgroundResource(R.drawable.installing_background);
            tvCount.setBackgroundResource(R.drawable.installing_background);
        } else if (id == R.id.tab_all) {
            setBackgroundResource(R.drawable.all_background);
            tvCount.setBackgroundResource(R.drawable.all_background);
        }

    }
}
