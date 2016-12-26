package com.example.jiaqiyu.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiaqiyu.bean.ViewPagerAdapter;
import com.example.jiaqiyu.myweather.MainActivity;
import com.example.jiaqiyu.myweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaQi Yu on 2016/12/26.
 */

public class Guide extends Activity implements ViewPager.OnPageChangeListener {
    private View view1,view2,view3;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = new int[]{R.id.iv1,R.id.iv2,R.id.iv3};
    private TextView button_startUse;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SpeechUtility.createUtility(this, "appid=" + "55557e04" + "," + SpeechConstant.FORCE_LOGIN + "=true");
        setContentView(R.layout.activity_guide);
        initImageView();
        initDots();
        viewPager.setOnPageChangeListener(this);
        button_startUse = (TextView)views.get(2).findViewById(R.id.button_startUse);
        button_startUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.button_startUse){
                    Intent intent = new Intent(Guide.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    void initImageView(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.layout1,null);
        view2 = inflater.inflate(R.layout.layout2,null);
        view3 = inflater.inflate(R.layout.layout3,null);

        views= new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(views,this);
        viewPager.setAdapter(viewPagerAdapter);
    }
    void initDots(){
        dots = new ImageView[views.size()];
        for(int i = 0;i<views.size();i++){
            dots[i] = (ImageView)findViewById(ids[i]);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guid, menu);
        return true;
    }

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

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    public void onPageSelected(int position) {
        for(int j = 0 ;j<ids.length;j++){
            if(j == position) {
                dots[j].setImageResource(R.drawable.page_indicator_focused);
            }
            else{
                dots[j].setImageResource(R.drawable.page_indicator_unfocused);
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
