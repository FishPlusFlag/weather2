package com.example.jiaqiyu.db;
//   android C  C++

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jiaqiyu.bean.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaQi Yu on 2016/10/25.
 */

public class CityDB {
    public  static  final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db;

    public CityDB(Context context, String path){
        db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE,null);
    }

    public List<City> getAllCity(){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.rawQuery("SELECT * from "+CITY_TABLE_NAME,null);
        while (cursor.moveToNext()){
            String province = cursor.getString(cursor.getColumnIndex("province"));
            String city =cursor.getString(cursor.getColumnIndex("city"));
            String number = cursor.getString(cursor.getColumnIndex("number"));
            String allPY = cursor.getString(cursor.getColumnIndex("allpy"));
            String allFirstPY = cursor.getString(cursor.getColumnIndex("allfirstpy"));
            String firstPY = cursor.getString(cursor.getColumnIndex("firstpy"));
            City item = new City(province,city,number,firstPY,allPY,allFirstPY);
            list.add(item);
        }
        return list;
    }

    public List<City> getSelectCity(String selectedCity){
        List<City> list = new ArrayList<City>();
        //System.out.println("进入筛选选择城市函数");
        //System.out.println("select * from "+CITY_TABLE_NAME+" where city like '%"+selectedCity+"%'");
        Cursor c = db.rawQuery("select * from "+CITY_TABLE_NAME+" where city like '%"+selectedCity+"%'",null);
        while (c.moveToNext())
        {
            //System.out.println("模糊查询查到了数据");
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            City cityTemp = new City(province,city,number,allPY,allFirstPY,firstPY);
            list.add(cityTemp);
        }
        c.close();
        return list;
    }



}
