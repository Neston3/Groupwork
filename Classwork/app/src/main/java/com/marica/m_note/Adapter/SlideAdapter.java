package com.marica.m_note.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marica.m_note.R;

/**
 * Created by Neston on 02/01/2018.
 */

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    public String[] list_title={
            "Welcome",
            "In order to add some notes press the add button on your bottom right of the screen,",
            "To delete a note swipe to the left."
    };

    //list of background
    public int[] list_backgroundcolor={
            //rgb(142,36,170)
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255)
    };

    public SlideAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getCount() {
        return list_title.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.slide,container,false);
        LinearLayout linearLayout=view.findViewById(R.id.slide_layout);
        TextView title=view.findViewById(R.id.textView1);

        linearLayout.setBackgroundColor(list_backgroundcolor[position]);
        title.setText(list_title[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
