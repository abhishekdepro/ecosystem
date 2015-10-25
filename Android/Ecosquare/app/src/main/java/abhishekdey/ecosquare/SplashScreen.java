package abhishekdey.ecosquare;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseUser;


public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 6000;
    LocationManager locationManager ;
    String provider;
    public static Location loc;

    public void buttonClickFunction(View v)
    {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);

    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isNetworkAvailable()==false){

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                            System.exit(0);
                            break;


                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setMessage("We cannot find any internet connection on your device. Please turn internet on.").setPositiveButton("Ok", dialogClickListener)
                    .show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);






        if(provider!=null && !provider.equals("")){

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 3000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

            if(location!=null){
                loc = location;
            }

        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {



           @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);
               finish();


            }
        }, SPLASH_TIME_OUT);
    }

}
