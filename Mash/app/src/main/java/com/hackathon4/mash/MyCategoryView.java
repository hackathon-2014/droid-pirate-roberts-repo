package com.hackathon4.mash;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hackathon4.mash.activity.ActivityEditCategory;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;


/**
 * TODO: document your custom view class.
 */
public class MyCategoryView extends RelativeLayout {

    private ListView itemsView;
    private Button button;
    private LinearLayout itemsLayout;

    public MyCategoryView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyCategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyCategoryView, defStyle, 0);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.category, this);

        View view = inflate(getContext(), R.layout.category, null);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setCategory(String category){
        button = (Button) findViewById(R.id.addToCategory);
        button.setText(category);
    }

    public void addListItems(String[] items){


//        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),
//                android.R.layout.simple_list_item_1,
//                Arrays.asList(items));

        itemsLayout = (LinearLayout) findViewById(R.id.itemLayout);

        for (String s: items){
            LayoutInflater li = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView tv = //new TextView(getContext());
                    (TextView ) li.inflate(R.layout.item_template, null);
            tv.setText(s);

            itemsLayout.addView(tv);
        }
    }
}
