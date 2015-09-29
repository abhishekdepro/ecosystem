package abhishekdey.ecosquare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Tutorial extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tutorial);



        // UNSELECTED BACKGROUND
        ViewPager unselectedBackgroundViewPager =
                (ViewPager) findViewById(R.id.viewpager_unselected_background);
        CircleIndicator unselectedBackgroundIndicator =
                (CircleIndicator) findViewById(R.id.indicator_unselected_background);
        DemoPagerAdapter unselectedBackgroundPagerAdapter =
                new DemoPagerAdapter(getSupportFragmentManager());
        unselectedBackgroundViewPager.setAdapter(unselectedBackgroundPagerAdapter);
        unselectedBackgroundIndicator.setViewPager(unselectedBackgroundViewPager);

        unselectedBackgroundViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TextView tv = (TextView)findViewById(R.id.viewPagerText);
                ImageView img = (ImageView)findViewById(R.id.imgViewPager);
                ImageView imgInside = (ImageView)findViewById(R.id.imgInside);
                if(position==4){

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(position==0){
                    imgInside.setImageResource(R.drawable.waste);
                    img.setVisibility(View.INVISIBLE);
                    tv.setText("Just book your pickup using our app. Keep your waste ready.");
                }
                else if(position==2){
                    img.setImageResource(R.drawable.rupee);
                    imgInside.setVisibility(View.INVISIBLE);
                    tv.setText("Get Cash and Credits. Cash for Trash.");
                }else if(position==1){
                    img.setImageResource(R.drawable.car);
                    img.setVisibility(View.VISIBLE);
                    tv.setText("We will pick up the waste from your place");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}