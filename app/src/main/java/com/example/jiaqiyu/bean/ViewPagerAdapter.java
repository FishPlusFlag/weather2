package com.example.jiaqiyu.bean;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by JiaQi Yu on 2016/12/26.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;
    public ViewPagerAdapter(List<View> views, Context context){
        this.views = views;
        this.context = context;
    }

    public int getCount() {
        return views.size();
    }


    public boolean isViewFromObject(View view, Object object) {
        //return view == views.get((int)Integer.parseInt(object.toString()));
        return (view == object);
    }

    public Object instantiateItem(ViewGroup container, int position){
        container.addView(views.get(position));
        return views.get(position);
    }

    public void destroyItem(ViewGroup container, int position,Object object){
        container.removeView(views.get(position));
    }
}
