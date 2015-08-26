package abhishekdey.ecosquare;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;


public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    public void buttonClickFunction(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Lg41go4Abua0UIYThLXKHcsgWbWpuVbVehru45xP", "hUsTpwaIAyzYtroir8y4XR5SXPz3NpJmHi0FbwUs");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MapsActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
