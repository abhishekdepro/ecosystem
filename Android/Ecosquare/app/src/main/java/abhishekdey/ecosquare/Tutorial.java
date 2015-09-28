package abhishekdey.ecosquare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import me.relex.circleindicator.CircleIndicator;

public class Tutorial extends AppCompatActivity {

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
                if(position==4){

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}