package com.harsh.shah.saavnmp3.utils.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harsh.shah.saavnmp3.R;


public class BottomSheetItemView extends LinearLayout {

    public BottomSheetItemView(Context context) {
        super(context);
        init(null, 0);
    }

    public BottomSheetItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BottomSheetItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final String title;
        final Drawable mExampleDrawable;

        inflate(getContext(), R.layout.bottom_sheet_items_custom_view, this);


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BottomSheetItemView, defStyle, 0);

            title = a.getString(
                    R.styleable.BottomSheetItemView_title);

            mExampleDrawable = a.getDrawable(
                        R.styleable.BottomSheetItemView_android_src);


            ((TextView)findViewById(R.id.text)).setText(title);
            ((ImageView)findViewById(R.id.icon)).setImageDrawable(mExampleDrawable);


            a.recycle();
    }
}