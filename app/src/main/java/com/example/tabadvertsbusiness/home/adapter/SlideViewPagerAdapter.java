package com.example.tabadvertsbusiness.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tabadvertsbusiness.R;

public class SlideViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};

    public SlideViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int data[] = {R.string.welcome,R.string.start_working_now,R.string.have_nice_time};
        final  int subInfoData[]  ={R.string.slider_sub_info1,R.string.slider_sub_info2,R.string.slider_sub_info3};
        View view = layoutInflater.inflate(R.layout.slide_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textInfo = view.findViewById(R.id.textInfo);
        imageView.setImageResource(images[position]);
        textInfo.setText(data[position]);


        TextView subInfo = view.findViewById(R.id.subInfo);
        subInfo.setText(subInfoData[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}