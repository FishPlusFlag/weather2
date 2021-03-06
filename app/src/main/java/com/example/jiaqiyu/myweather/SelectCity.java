package com.example.jiaqiyu.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.CharacterPickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiaqiyu.app.MyApplication;
import com.example.jiaqiyu.bean.City;
import com.example.jiaqiyu.db.CityDB;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JiaQi Yu on 2016/10/18.
 */

public class SelectCity  extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;

    private ImageView mSearch;
    private ListView listView;
    private TextView title_city;
    private EditText search_edit;
    private static HashMap<String,City> city_number_map = null;
    private List<Map<String,Object>> listItems;
    private SimpleAdapter adapter;


//    String path = "/data"
//            + Environment.getDataDirectory().getAbsolutePath()
//            + File.separator+getPackageName()
//            +File.separator+"databases1"
//            +File.separator
//            +CityDB.CITY_DB_NAME;

    CityDB mCityDB =  new CityDB(MyApplication.getIntance(),MyApplication.path);
//    CityDB mCityDB =  new CityDB(MyApplication.getIntance());

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    String text = "";

    private Toast mToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);
        mBackBtn=(ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        title_city = (TextView)findViewById(R.id.title_name);
        search_edit = (EditText) findViewById(R.id.searchbox);
        listView = (ListView) findViewById(R.id.city_list);
        search_edit.addTextChangedListener(mTextWatcher);
        getData();
        adapter = new SimpleAdapter(this,listItems,R.layout.city_province,
                new String[]{"city","province"},new int[]{R.id.city_name,R.id.city_province});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SelectCity.this,"点击了："+listItems.get(position).get("city"),Toast.LENGTH_SHORT).show();
                title_city.setText("当前城市："+listItems.get(position).get("city"));
                changeCity();
            }
        });
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            temp = charSequence;
            //Log.d("myapp","beforeTextChanged:"+temp) ;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //System.out.println("输入为："+charSequence);
            temp = charSequence;
            // Log.d("myapp","onTextChanged:"+charSequence) ;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //  System.out.println("输入框发生变化");
            listItems.clear();
            List<City> list = null;
            list = mCityDB.getSelectCity(String.valueOf(temp));
            Iterator<City> iterator = list.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                City city = iterator.next();
                String cityName = city.getCity();
                String city_province = city.getProvince();
                listItem.put("city", cityName);
                listItem.put("province", city_province);
                listItems.add(listItem);
            }
            adapter.notifyDataSetChanged();

        }
    };

    public void getData()
    {
        listItems = new ArrayList<Map<String,Object>>();
        city_number_map = new HashMap<String,City>();
        List<City> list = null;
        list = mCityDB.getAllCity();
        Iterator<City> iterator = list.iterator();
        while(iterator.hasNext())
        {
            Map<String,Object> listItem = new HashMap<String,Object>();
            City city = iterator.next();
            String cityName = city.getCity();
            String city_province = city.getProvince();
            city_number_map.put(cityName,city);
            // System.out.println("加入HashMap后CityName:"+cityName+"  cityNumber"+city_number_map.get(cityName));
            listItem.put("city",cityName);
            listItem.put("province",city_province);
            listItems.add(listItem);
        }


    }

    public void changeCity()
    {
//        Intent intent = new Intent(this,MyActivity.class);
//        String cityName = title_city.getText().toString().substring(5);
//        String cityNumber = city_number_map.get(cityName).getNumber();
//        System.out.println("Onclick——CityName："+cityName);
//        System.out.println("Onclick——CityNumber:"+cityNumber);
//        intent.putExtra("cityName",cityName);
//        intent.putExtra("cityNumber",cityNumber);
//        finish();
//        startActivity(intent);

        Intent i = new Intent();
        String cityName = title_city.getText().toString().substring(5);
        City city = city_number_map.get(cityName);
        i.putExtra("city",cityName);
        setResult(RESULT_OK,i);
        finish();


    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode == 1 && resultCode == RESULT_OK){
            String cityName = (String)data.getExtras().getString("cityName");
            //Toast.makeText(this,"回调回来的数据是"+cityName,Toast.LENGTH_SHORT).show();
            search_edit.setText(cityName);
        }

    }
    int ret = 0; // 函数调用返回值




    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
