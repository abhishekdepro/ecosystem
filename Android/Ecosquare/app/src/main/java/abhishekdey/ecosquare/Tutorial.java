package abhishekdey.ecosquare;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
        final ImageView img = (ImageView) findViewById(R.id.imgViewPager);
        final TextView tv = (TextView)findViewById(R.id.viewPagerText);


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



                if(position==1){
                    img.setImageResource(R.drawable.tut2);
                    tv.setText("Choose type of waste, weight and payment mode");
                }

                if(position==2){
                    img.setImageResource(R.drawable.tut4);
                    tv.setText("Share your achievements with friends on social.");
                }

                if(position==3){
                    img.setImageResource(R.drawable.tut3);
                    tv.setText("Share your referral code and earn eco points.");
                }

                if(position==4){
                    img.setImageResource(R.drawable.tut5);
                    tv.setText("Use your eco points to redeem exciting deals.");

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}