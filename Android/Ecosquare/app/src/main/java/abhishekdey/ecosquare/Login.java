package abhishekdey.ecosquare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.location.LocationListener;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {

    private ProgressDialog progress;
    private  Location loc=SplashScreen.loc;
    private double lat,lon;
    LocationManager locationManager ;
    String provider;
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
        ActionBar bar = getSupportActionBar();
        /*bar.setDisplayShowHomeEnabled(true);
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        bar.setLogo(R.drawable.logo);
        bar.setDisplayUseLogoEnabled(true);
        bar.setTitle("  " + "Ecosquare");*/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2A2A2A")));
        ParseUser currentUser = ParseUser.getCurrentUser();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(SignUp.PREFS_NAME, Context.MODE_PRIVATE);
        String isFirst = "yes";//prefs.getString("firstTime", "");
        if (currentUser != null) {
            if(isFirst.equals("no")){
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }else{
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(SignUp.PREFS_NAME, Activity.MODE_PRIVATE).edit();
                editor.putString("firstTime", "no");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), Tutorial.class);
                startActivity(intent);
            }
            finish();
        } else {
            // show the signup or login screen
        }

//Remove notification bar


//set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_login);
        locateme();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }else if (id == R.id.logout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClickFunction(View v){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
        finish();
    }

    public void signUp(View v)
    {
        progress = ProgressDialog.show(Login.this, "Register",
                "Creating your footprints on our server...", true);


        lat=SplashScreen.loc.getLatitude();
        lon=SplashScreen.loc.getLongitude();
        ParseUser user = new ParseUser();
        TextView _contact = (TextView) findViewById(R.id.contact);
        EditText _name = (EditText) findViewById(R.id.name);
        EditText _code = (EditText) findViewById(R.id.ref);
        TextView _email = (TextView) findViewById(R.id.email);
        EditText _password = (EditText)findViewById(R.id.password);
        user.setUsername(_contact.getText().toString());
        user.setPassword(_password.getText().toString());
        user.setEmail(_email.getText().toString());
        user.put("Name", _name.getText().toString());
        user.put("Latitude",Double.toString(lat));
        user.put("Longitude",Double.toString(lon));
        user.put("Code",_code.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), SignUp.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    break;


                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setMessage("Oh Snap! Something went wrong. Please try again").setPositiveButton("Ok", dialogClickListener)
                            .show();

                }

                }

        });
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            loc=location;
            lat = location.getLatitude();
            lon=location.getLongitude();
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
    };

    public void locateme(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // Getting the name of the provider that meets the criteria

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(provider, 1000, 1, mLocationListener);

    }
}
