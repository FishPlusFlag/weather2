package com.example.jiaqiyu.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jiaqiyu.app.MyApplication;
import com.example.jiaqiyu.bean.City;
import com.example.jiaqiyu.db.CityDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaQi Yu on 2016/10/18.
 */

public class SelectCity  extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;

   // private ListView mCityListView;
    //private ArrayAdapter<City> adapter;
    //private CityDB cityDB;
    //private List<City> cList = new ArrayList<City>();
    //private MyApplication mApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);
        mBackBtn=(ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);


       // mCityListView = (ListView) findViewById(R.id.citylist_view);
        //mApplication = (MyApplication) getApplication();
        //cList = mApplication.getCityList();





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back :
                Intent i = new Intent();
                i.putExtra("cityCode","101160101");
                setResult(RESULT_OK,i);
                finish();
                break;
            default:
                break;
        }
    }
}
