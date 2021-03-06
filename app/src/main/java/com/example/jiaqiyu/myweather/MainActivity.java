package com.example.jiaqiyu.myweather;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiaqiyu.bean.TodayWeather;
import com.example.jiaqiyu.bean.ViewPagerAdapter;
import com.example.jiaqiyu.util.NetUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiaqi Yu on 2016/9/27.
 */

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int UPDATE_TODAY_WEATHER = 1;
    private ImageView mUpdateBtn;
    private ImageView mCitySelect;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;
    private ViewPager forcastWeather;
    private List<View> viewList;
    private View view1,view2;
    private TextView day1_week,day1_wind,day1_weather,day1_temperature,
            day2_week,day2_wind,day2_weather,day2_temperature,
            day3_week,day3_wind,day3_weather,day3_temperature,
            day4_week,day4_wind,day4_weather,day4_temperature,
            day5_week,day5_wind,day5_weather,day5_temperature,
            day6_week,day6_wind,day6_weather,day6_temperature;
    private ImageView day1_picture,day2_picture,day3_picture,
            day4_picture,day5_picture,day6_picture;
    private  ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updatetodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;

            }
        }

    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK!");

            Toast.makeText(MainActivity.this, "网络状态良好！", Toast.LENGTH_LONG).show();

            /*
            queryWeatherCode(cityCode);
             */
        } else {
            Log.d("myWeather", "网络挂了！");
            Toast.makeText(MainActivity.this, "网络状态不佳！", Toast.LENGTH_LONG).show();

        }

        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        initForcastView();
        initview();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_city_manager) {
            Intent i = new Intent(this, SelectCity.class);
            //startActivity(i);
            startActivityForResult(i, 1);
        }

        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK!");
                /*
                Toast.makeText(MainActivity.this, "网络ok！", Toast.LENGTH_LONG).show();
                 */
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了！");

                Toast.makeText(MainActivity.this, "网络状态不佳！", Toast.LENGTH_LONG).show();

            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为：" + newCityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK!");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了！");
                Toast.makeText(MainActivity.this, "网络状态不佳！", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.e("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    // parseXML(responseStr);
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());

                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }


    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                // Log.d("myWeather","city:  "+xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                //Log.d("myWeather","updatetime:  "+xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并处罚相应事件
                eventType = xmlPullParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }


    void initview() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");

        //Log.d("myWeather", "初始界面测试！！！！");

//        LayoutInflater inflater = LayoutInflater.from(this);
//        view1 = inflater.inflate(R.layout.forcast_weather1,null);
//        view2 = inflater.inflate(R.layout.forcast_weather2,null);
//        viewList = new ArrayList<View>();
//        viewList.add(view1);
//        viewList.add(view2);
//        vpAdapter = new ViewPagerAdapter(viewList,this);
//        vp = (ViewPager) findViewById(R.id.viewpager);
//        vp.setAdapter(vpAdapter);




        View view1 = viewList.get(0);
        day1_week = (TextView)view1.findViewById(R.id.day1_week);
        day1_temperature = (TextView)view1.findViewById(R.id.day1_temperature);
        day1_weather = (TextView)view1.findViewById(R.id.day1_weather);
        day1_wind = (TextView)view1.findViewById(R.id.day1_wind);
        day1_picture = (ImageView)view1.findViewById(R.id.day1_picture);

        View view2 = viewList.get(0);
        day2_week = (TextView)view2.findViewById(R.id.day2_week);
        day2_temperature = (TextView)view2.findViewById(R.id.day2_temperature);
        day2_weather = (TextView)view2.findViewById(R.id.day2_weather);
        day2_wind = (TextView)view2.findViewById(R.id.day2_wind);
        day2_picture = (ImageView)view2.findViewById(R.id.day2_picture);

        View view3 = viewList.get(0);
        day3_week = (TextView)view3.findViewById(R.id.day3_week);
        day3_temperature = (TextView)view3.findViewById(R.id.day3_temperature);
        day3_weather = (TextView)view3.findViewById(R.id.day3_weather);
        day3_wind = (TextView)view3.findViewById(R.id.day3_wind);
        day3_picture = (ImageView)view3.findViewById(R.id.day3_picture);

        View view4 = viewList.get(1);
        day4_week = (TextView)view4.findViewById(R.id.day4_week);
        day4_temperature = (TextView)view4.findViewById(R.id.day4_temperature);
        day4_weather = (TextView)view4.findViewById(R.id.day4_weather);
        day4_wind = (TextView)view4.findViewById(R.id.day4_wind);
        day4_picture = (ImageView)view4.findViewById(R.id.day4_picture);

        View view5 = viewList.get(1);
        day5_week = (TextView)view5.findViewById(R.id.day5_week);
        day5_temperature = (TextView)view5.findViewById(R.id.day5_temperature);
        day5_weather = (TextView)view5.findViewById(R.id.day5_weather);
        day5_wind = (TextView)view5.findViewById(R.id.day5_wind);
        day5_picture = (ImageView)view5.findViewById(R.id.day5_picture);

        View view6 = viewList.get(1);
        day6_week = (TextView)view6.findViewById(R.id.day6_week);
        day6_temperature = (TextView)view6.findViewById(R.id.day6_temperature);
        day6_weather = (TextView)view6.findViewById(R.id.day6_weather);
        day6_wind = (TextView)view6.findViewById(R.id.day6_wind);
        day6_picture = (ImageView)view6.findViewById(R.id.day6_picture);


    }

    void initForcastView() {
        forcastWeather = (ViewPager) findViewById(R.id.forcastWeather);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.forcast_weather1, null);
        view2 = inflater.inflate(R.layout.forcast_weather2, null);
        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList, this);
        forcastWeather.setAdapter(viewPagerAdapter);

    }




    int getWeatherImageId(String type) {
        switch (type) {
            case ("雨夹雪"):
                return R.drawable.biz_plugin_weather_yujiaxue;
            case ("暴雪"):
                return R.drawable.biz_plugin_weather_baoxue;
            case "暴雨":
                return R.drawable.biz_plugin_weather_baoyu;
            case "大暴雨":
                return R.drawable.biz_plugin_weather_dabaoyu;
            case "大雪":
                return R.drawable.biz_plugin_weather_daxue;
            case "大雨":
                return R.drawable.biz_plugin_weather_dayu;
            case "多云":
                return R.drawable.biz_plugin_weather_duoyun;
            case "雷阵雨":
                return R.drawable.biz_plugin_weather_leizhenyu;
            case "晴":
                return R.drawable.biz_plugin_weather_qing;
            case "沙尘暴":
                return R.drawable.biz_plugin_weather_shachenbao;
            case "特大暴雨":
                return R.drawable.biz_plugin_weather_tedabaoyu;
            case "雾":
                return R.drawable.biz_plugin_weather_wu;
            case "小雪":
                return R.drawable.biz_plugin_weather_xiaoxue;
            case "小雨":
                return R.drawable.biz_plugin_weather_xiaoyu;
            case "阴":
                return R.drawable.biz_plugin_weather_yin;
            case "阵雪":
                return R.drawable.biz_plugin_weather_zhenxue;
            case "阵雨":
                return R.drawable.biz_plugin_weather_zhenyu;
            case "中雨":
                return R.drawable.biz_plugin_weather_zhongyu;

            default:
                return R.drawable.biz_plugin_weather_qing;


        }
    }

    int getPmImgId(String pm25) {
        int result = Integer.parseInt(pm25);
        if (result >= 101 && result <= 150) {
            return R.drawable.biz_plugin_weather_101_150;
        } else if (result >= 0 && result <= 50) {
            return R.drawable.biz_plugin_weather_0_50;
        } else if (result >= 51 && result <= 100) {
            return R.drawable.biz_plugin_weather_51_100;
        } else if (result >= 151 && result <= 200) {
            return R.drawable.biz_plugin_weather_151_200;
        } else if (result >= 201 && result <= 300) {
            return R.drawable.biz_plugin_weather_201_300;
        } else if (result > 300) {
            return R.drawable.biz_plugin_weather_greater_300;
        } else {
            return R.drawable.biz_plugin_weather_0_50;
        }

    }

    void updatetodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力" + todayWeather.getFengli());
        weatherImg.setImageResource(getWeatherImageId(todayWeather.getType()));
        pmImg.setImageResource(getPmImgId(todayWeather.getPm25()));
        Toast.makeText(MainActivity.this, "更新成功~", Toast.LENGTH_SHORT).show();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

