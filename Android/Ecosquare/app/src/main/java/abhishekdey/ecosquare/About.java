package abhishekdey.ecosquare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class About extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar));
        }
        //window.setStatusBarColor(this.getResources().getColor(R.color.status_bar));
        ActionBar bar = getSupportActionBar();
        /*bar.setDisplayShowHomeEnabled(true);
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        bar.setLogo(R.drawable.logo);
        bar.setDisplayUseLogoEnabled(true);
        bar.setTitle(" " + "Ecosquare");*/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2A2A2A")));
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();


    }

}
