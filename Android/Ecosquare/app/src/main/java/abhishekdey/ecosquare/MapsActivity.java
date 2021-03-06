package abhishekdey.ecosquare;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pkmmte.view.CircularImageView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,LocationListener {

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //FrameLayout FragmentContainer = (FrameLayout) findViewById(R.id.frame_container);

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private Marker myMarker,x,y,_new;
    private Hashtable<String, String> Markers;
    private double lat,lon;
    LocationManager locationManager ;
    String provider;
    ProgressDialog progress;
    private boolean reset = false;
    ArrayList<Marker> markers=new ArrayList<>();
    private String activeMarker = "myMarker";
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] drawerListViewItems;
    private ListView drawerListView;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    static Button notifCount;
    static int mNotifCount = 0;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    final String[] data ={"one"};
    final String[] fragments ={
            "abhishekdey.ecosquare.fragment_home"};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

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


        //drawerListViewItems = getResources().getStringArray(R.array.items);

        // get ListView defined in activity_main.xml
        //drawerListView = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        //drawerListView.setAdapter(new ArrayAdapter<>(this,
       //         R.layout.drawer_listview_item, drawerListViewItems));

        SharedPreferences.Editor editor = getSharedPreferences(SignUp.PREFS_NAME,MODE_PRIVATE).edit();
        editor.putString("reload", "no");
        editor.commit();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


        // Recycle the typed array
        navMenuIcons.recycle();


        class SlideMenuClickListener implements
                ListView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // display view for selected nav drawer item
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.map)).commit();
                displayView(position);
                SharedPreferences.Editor editor = getSharedPreferences(SignUp.PREFS_NAME,MODE_PRIVATE).edit();
                editor.putString("reload", "yes");
                editor.commit();
            }


            /**
             * Diplaying fragment view for selected nav drawer list item
             * */
            private void displayView(int position) {
                // update the main content by replacing fragments
                Fragment fragment = null;
                FrameLayout container = (FrameLayout)findViewById(R.id.frame_container);

                switch (position) {
                    case 0:

                        container.removeAllViewsInLayout();
                        fragment = new HomeFragment();

                        break;
                    case 1:

                        container.removeAllViewsInLayout();
                        Intent _refIntent = new Intent(getApplicationContext(), Empty.class);
                        startActivity(_refIntent);
                        finish();
                        break;
                    case 2:

                        container.removeAllViewsInLayout();
                        Intent intent = new Intent(getApplicationContext(), FeedListActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 3:

                        container.removeAllViewsInLayout();
                        Intent refIntent = new Intent(getApplicationContext(), ReferralActivity.class);
                        startActivity(refIntent);
                        finish();
                        break;
                    case 4:

                        container.removeAllViewsInLayout();
                        Intent _blank = new Intent(getApplicationContext(), Tutorial.class);
                        startActivity(_blank);
                        finish();
                        break;
                    case 5:

                        container.removeAllViewsInLayout();
                        Intent about = new Intent(getApplicationContext(), About.class);
                        startActivity(about);
                        finish();
                        break;

                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();

                    // update selected item and title, then close the drawer
                    drawerListView.setItemChecked(position, true);
                    drawerListView.setSelection(position);
                    setTitle(navMenuTitles[position]);
                    drawer.closeDrawer(drawerListView);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        }

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(this,
                navDrawerItems);
        drawerListView.setAdapter(adapter);

        drawerListView.setOnItemClickListener(new SlideMenuClickListener());




        drawer = (DrawerLayout) findViewById(R.id.drawer);

        setupDrawerLayout();
        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView)findViewById(R.id.address_search);



        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(MapsActivity.this, R.layout.list_item));



        autocompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autocompleteView.setText("");
                autocompleteView.setCursorVisible(true);
            }
        });

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                final String description = (String) parent.getItemAtPosition(position);
                String place_id = PlaceAPI.placesidList.get(0);
                try {
                    Ion.with(getApplicationContext())
                            .load("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=AIzaSyDghrnfqL5qa-0AambYqvUV0qE-ysWAdJo")
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    JsonObject obj = result.getAsJsonObject("result");
                                    JsonObject geometry = obj.getAsJsonObject("geometry");
                                    JsonObject loc = geometry.getAsJsonObject("location");
                                    lat = Double.parseDouble(loc.get("lat").toString());
                                    lon = Double.parseDouble(loc.get("lng").toString());

                                    if (myMarker != null) {
                                        myMarker.remove();


                                        myMarker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(lat, lon))
                                                .title(description).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                                                .snippet("Kolkata"));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
                                        myMarker.setDraggable(true);
                                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker) {
                                                return false;
                                            }
                                        });

                                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                                        myMarker.showInfoWindow();
                                        reset = true;
                                    }
                                }
                            });
                } catch (Exception ex) {
                    Toast.makeText(getBaseContext(), "Aw Snap! Probably the internet is not talking with me", Toast.LENGTH_SHORT).show();
                }

                //Take marker to location

            }
        });
        final View inf = getLayoutInflater().inflate(R.layout.image_layout
                , null);
        final ImageView imageView = (ImageView)inf.findViewById(R.id.imgview);
        try{
        Ion.with(getApplicationContext())
                .load(getString(R.string.url)+"/promo")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e==null){
                        String exists = result.get("exists").toString();
                        if (exists.equals("1")) {
                            Ion.with(imageView)
                                    .placeholder(R.drawable.leaf)
                                    .error(R.drawable.leaf)
                                    .load(getString(R.string.url)+"/fetchpromo");

                            Dialog settingsDialog = new Dialog(getApplicationContext());
                            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            settingsDialog.setContentView(inf);
                            settingsDialog.show();
                            settingsDialog.getWindow().setLayout(400, 365);
                        }
                        }
                    }
                });
        }catch (Exception ex){
            Toast.makeText(getBaseContext(), "Could not connect at the moment.", Toast.LENGTH_SHORT).show();
        }





        MapsInitializer.initialize(getApplicationContext());
        setUpMapIfNeeded();
        //FloatingActionButton button= (FloatingActionButton) findViewById(R.id.add);
        //button.setOnClickListener(new View.OnClickListener() {

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        startTimer();

        progress = ProgressDialog.show(MapsActivity.this, "Location",
                "Finding your location", true);
        progress.setCanceledOnTouchOutside(true);



        if(provider!=null && !provider.equals("")){

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 3000, 1, this);
            if(SplashScreen.loc!=null){
                onLocationChanged(SplashScreen.loc);
            }
            else if(location!=null){
                Toast.makeText(getBaseContext(), "Location found", Toast.LENGTH_SHORT).show();
                onLocationChanged(location);
            }
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }



    }



    public void logout(View v){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(SignUp.PREFS_NAME, Activity.MODE_PRIVATE).edit();
                        editor.remove("photo");
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage("Logout?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    public void updateUser(View v){

        final EditText _name = (EditText)findViewById(R.id.nameShow);
        final EditText _email = (EditText)findViewById(R.id.emailShow);
        SharedPreferences prefs = this.getSharedPreferences(SignUp.PREFS_NAME, Context.MODE_PRIVATE);
        final String Settings = prefs.getString("updateHit", null);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                    {

                        new Thread(new Runnable() {
                            @Override
                            public void run()
                            {
                                //Trying Ion
                                JsonObject json = new JsonObject();
                                json.addProperty("id", ParseUser.getCurrentUser().getUsername());
                                json.addProperty("name", _name.getText().toString());
                                json.addProperty("email", _email.getText().toString());
                                Log.i("Json",json.toString());
                                Ion.with(getApplicationContext())
                                        .load(getString(R.string.url) + "/user/update")
                                        .setJsonObjectBody(json)
                                        .asJsonObject()
                                        .setCallback(new FutureCallback<JsonObject>() {
                                            @Override
                                            public void onCompleted(Exception e, JsonObject result) {
                                                if (e != null) {
                                                    Toast.makeText(getBaseContext(), "Data : " + e.getStackTrace(), Toast.LENGTH_LONG).show();
                                                } else {

                                                    Toast.makeText(getBaseContext(), "Updated successfully!", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });


                            }
                        }).start();

                        break;
                    }

                    case DialogInterface.BUTTON_NEGATIVE:

                        dialog.dismiss();
                        break;
                }
        }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage("Update your details?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed()
    {
        SharedPreferences prefs = this.getSharedPreferences(SignUp.PREFS_NAME, Context.MODE_PRIVATE);
        String Settings = prefs.getString("reload", null);


        if (doubleBackToExitPressedOnce || Settings.equals("yes")) {
            if(Settings.equals("yes")){
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }else {
                super.onBackPressed();
            }
        }

        this.doubleBackToExitPressedOnce = true;
        if(Settings.equals("no")) {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    private void stopTimer(){
        if(mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer(){
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        if(markers.isEmpty()==false){
                            for(int i=0;i<markers.size();i++){
                                Marker m = markers.get(i);
                                m.remove();
                            }
                        }

                        try{
                            getMarkers();
                        }catch (Exception e){
                            Log.i("Error","Fetch");
                        }
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 100000);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

        // Handle the drawer Actions
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.logout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerLayout(){

        // Instantiate the Drawer Toggle
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(" " + "Ecosquare");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(" " + "Ecosquare");
            }


        };

        // Set the Toggle on the Drawer, And tell the Action Bar Up Icon to show
        drawer.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        stopTimer();
        Log.i("Tag", "onPause, done");

    }
    @Override
    public void onResume() {
        super.onResume();
        if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
            setUpMapIfNeeded();
        }

        startTimer();
    }
    public void getMarkers() throws Exception{



        try{
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Ion.with(getApplicationContext())
                            .load(getString(R.string.url)+"/emp/location")
                            .as(new TypeToken<List<Employees>>(){})
                            .setCallback(new FutureCallback<List<Employees>>() {
                                @Override
                                public void onCompleted(Exception e, List<Employees> tweets) {
                                    if(tweets!=null){
                                        for (int i = 0; i < tweets.size(); i++) {
                                            // Create a marker for each city in the JSON data.
                                            Employees jsonObj = tweets.get(i);

                                            _new = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(Double.parseDouble(jsonObj.lat), Double.parseDouble(jsonObj.lon)))
                                                    .title(jsonObj._id).icon(BitmapDescriptorFactory.fromResource(R.drawable.ecocar))
                                                    .snippet("50% full"));

                                            markers.add(_new);
                                        }

                                    }
                                }
                            });
                }
            }).start();
        }catch (Exception ex){
            Toast.makeText(getBaseContext(), "Aw Snap! Probably the internet is not talking with me", Toast.LENGTH_SHORT).show();
        }
    }



    public void animate(LatLng location, final Marker m){
        final LatLng startPosition = m.getPosition();
        final LatLng finalPosition = location;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);

                m.setPosition(currentPosition);

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        m.setVisible(false);
                    } else {
                        m.setVisible(true);
                    }
                }
            }
        });
    }

    public void book(){
        progress = ProgressDialog.show(MapsActivity.this, "Working",
                "The minions are working..", true);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                //Trying Ion
                JsonObject json = new JsonObject();
                json.addProperty("u_id", "8981169454");
                json.addProperty("emp_id", "");
                json.addProperty("lat", Double.toString(lat));
                json.addProperty("lon", Double.toString(lon));
                json.addProperty("paper", "5");
                json.addProperty("plastic", "10");
                json.addProperty("mode", "cash");
                json.addProperty("status", "init");

                Ion.with(getApplicationContext())
                        .load(getString(R.string.url)+"/transaction")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (e != null) {
                                    Toast.makeText(getBaseContext(), "Data : " + e.getStackTrace(), Toast.LENGTH_LONG).show();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.dismiss();
                                        }
                                    });
                                    Toast.makeText(getBaseContext(), "Pickup added successfully! We will contact you soon.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        }).start();
    }


    @Override
    public void onLocationChanged(Location location) {
        // Getting reference to TextView tv_longitude

        try{
        progress.dismiss();
        // Setting Current Longitude
        lon = location.getLongitude();

        // Setting Current Latitude
        lat = location.getLatitude();

        if(reset==false)
            findAddress();
        }catch (Exception ex){


            Toast.makeText(getBaseContext(), "Aw Snap! Probably the internet is not talking with me", Toast.LENGTH_SHORT).show();
        }

    }

    public void findAddress(){
        Geocoder geocoder;
        String city;
        List<Address> addresses=new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
            }
              if(activeMarker=="myMarker"){
            if(myMarker!=null) {
                myMarker.remove();
            }
            if(reset==true) {
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                        .title(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                        .snippet(addresses.get(0).getLocality()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
                myMarker.setDraggable(true);
                mMap.setOnMarkerClickListener(this);

                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                myMarker.showInfoWindow();
                //adapter.getInfoWindow(myMarker);
            }else{
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat,lon))
                        .title(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                        .snippet(addresses.get(0).getLocality()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
                myMarker.setDraggable(true);
                mMap.setOnMarkerClickListener(this);
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
                myMarker.showInfoWindow();
            }
            //EditText search = (EditText)findViewById(R.id.address_search);
            //search.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1));
        }
        }catch (IOException ex){
            Toast.makeText(getBaseContext(), "Aw Snap! Probably the internet is not talking with me", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onProviderDisabled(String provider) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage("Turn Location On?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    /*GPSTracker gpsTracker = new GPSTracker(this);

    if (gpsTracker.getIsGPSTrackingEnabled())
    {
        String stringLatitude = String.valueOf(gpsTracker.latitude);

        String stringLongitude = String.valueOf(gpsTracker.longitude);

    }
    else
    {
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gpsTracker.showSettingsAlert();
    }*/

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

    }



    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
         x = mMap.addMarker(new MarkerOptions().position(new LatLng(22.56, 88.36)).title("C9ERTY").icon(BitmapDescriptorFactory.fromResource(R.drawable.ecocar)).snippet("50% full"));

        y = mMap.addMarker(new MarkerOptions().position(new LatLng(22.50, 88.36)).title("XYT6UI").icon(BitmapDescriptorFactory.fromResource(R.drawable.ecocar)).snippet("30% full"));

        x.showInfoWindow();
        y.showInfoWindow();
        //x.setDraggable(true);
        //y.setDraggable(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlng) {
                // TODO Auto-generated method stub
                lat = latlng.latitude;
                lon = latlng.longitude;
                activeMarker="myMarker";
                reset = true;
                findAddress();
                System.out.println(latlng);

            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                activeMarker = "myMarker";
                reset = true;
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (mMap.getMyLocation() != null) {
                    lat = marker.getPosition().latitude;
                    lon = marker.getPosition().longitude;
                    findAddress();
                }
                if (marker.equals(x)) {
                    activeMarker = "x";
                } else if (marker.equals(y)) {
                    activeMarker = "y";
                } else {
                    activeMarker = "myMarker";
                    reset = true;
                }

            }
        });



    }


    //POST HTTP

    public void locate_me(View v)
    {
        reset=false;
        onLocationChanged(mMap.getMyLocation());
        call();
    }

    public void predictaddress(View v){
        Intent intent = new Intent(getApplicationContext(), SearchAddress.class);
        startActivity(intent);
    }

    public void gotocalculator(View v){
        SharedPreferences.Editor editor = getSharedPreferences(SignUp.PREFS_NAME,MODE_PRIVATE).edit();
        editor.putString("lat", Double.toString(lat));
        editor.putString("lon", Double.toString(lon));
        editor.commit();
        Intent intent = new Intent(getApplicationContext(), Estimate.class);
        startActivity(intent);

    }

    public void gotoDeals(View v){

        Intent intent = new Intent(getApplicationContext(), Deals.class);
        startActivity(intent);

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(x))
        {
            x.showInfoWindow();


        }
        else if (marker.equals(y))
        {
            y.showInfoWindow();
        }
        else if (marker.equals(myMarker))
        {
            new CustomInfoWindowAdapter().getInfoWindow(myMarker);
            myMarker.showInfoWindow();


        } else{
            for(int i=0;i<markers.size();i++){
               if(marker.equals(markers.get(i))){
                   Marker m = markers.get(i);
                   new CustomInfoWindowAdapter().getInfoWindow(m);
                   m.showInfoWindow();
               }
            }
        }
        return true;
    }

    public void call(){
    }

    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                        .width(2)
                        .color(Color.BLUE).geodesic(true));
            }

        }
        catch (JSONException e) {
            Log.e("Json_error",e.getStackTrace().toString());
        }
    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;
        String url;
        connectAsyncTask(String urlPass){
            url = urlPass;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!=null){
                drawPath(result);
            }
        }
    }
    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.infowindowcustom,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (MapsActivity.this.myMarker != null
                    && MapsActivity.this.myMarker.isInfoWindowShown()) {
                MapsActivity.this.myMarker.hideInfoWindow();
                MapsActivity.this.myMarker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            MapsActivity.this.myMarker = marker;

            String url = null;




            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }

            final String snippet = marker.getSnippet();
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }

            return view;
        }
    }



}