package abhishekdey.ecosquare;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,LocationListener {

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //FrameLayout FragmentContainer = (FrameLayout) findViewById(R.id.frame_container);

    private Marker myMarker,x,y,_new;
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

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    final String[] data ={"one"};
    final String[] fragments ={
            "abhishekdey.ecosquare.fragment_home"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.status_bar));
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
        //drawerListView.setAdapter(new ArrayAdapter<String>(this,
         //       R.layout.drawer_listview_item, drawerListViewItems));


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
            }


            /**
             * Diplaying fragment view for selected nav drawer list item
             * */
            private void displayView(int position) {
                // update the main content by replacing fragments
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        FrameLayout container = (FrameLayout)findViewById(R.id.frame_container);
                        container.removeAllViews();
                        fragment = new HomeFragment();
                        break;


                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commit();

                    // update selected item and title, then close the drawer
                    drawerListView.setItemChecked(position, true);
                    drawerListView.setSelection(position);
                    setTitle(navMenuTitles[position]);
                    drawer.closeDrawer(drawerListView);
                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        }

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        drawerListView.setAdapter(adapter);

        drawerListView.setOnItemClickListener(new SlideMenuClickListener());




        drawer = (DrawerLayout) findViewById(R.id.drawer);

        setupDrawerLayout();

        MapsInitializer.initialize(getApplicationContext());
        setUpMapIfNeeded();
        FloatingActionButton button= (FloatingActionButton) findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                book();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage("Book a Pickup?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
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
                Toast.makeText(getBaseContext(), "Location is: "+location.getLatitude(), Toast.LENGTH_SHORT).show();
                onLocationChanged(location);
            }
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
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

        mTimer1.schedule(mTt1, 1, 30000);
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

        Ion.with(getApplicationContext())
                .load(getString(R.string.url)+"/emp/location")
                .as(new TypeToken<List<Employees>>(){})
                .setCallback(new FutureCallback<List<Employees>>() {
                    @Override
                    public void onCompleted(Exception e, List<Employees> tweets) {
                        for (int i = 0; i < tweets.size(); i++) {
                            // Create a marker for each city in the JSON data.
                            Employees jsonObj = tweets.get(i);

                            _new = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(jsonObj.lat), Double.parseDouble(jsonObj.lon)))
                                    .title(jsonObj._id).icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle))
                                    .snippet("50% full"));

                            markers.add(_new);
                        }
                        for (int i = 0; i < markers.size(); i++) {
                            Marker m = markers.get(i);
                        }
                    }
                });
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

            builder.setMessage("Aw Snap! Probably the internet is not talking with me").setPositiveButton("Ok", dialogClickListener)
                    .show();
        }

    }

    public void findAddress(){
        Geocoder geocoder;
        List<Address> addresses=new List<Address>() {
            @Override
            public void add(int location, Address object) {

            }

            @Override
            public boolean add(Address object) {
                return false;
            }

            @Override
            public boolean addAll(int location, Collection<? extends Address> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Address> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object object) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Address get(int location) {
                return null;
            }

            @Override
            public int indexOf(Object object) {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Address> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object object) {
                return 0;
            }

            @Override
            public ListIterator<Address> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Address> listIterator(int location) {
                return null;
            }

            @Override
            public Address remove(int location) {
                return null;
            }

            @Override
            public boolean remove(Object object) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Address set(int location, Address object) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public List<Address> subList(int start, int end) {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] array) {
                return null;
            }
        };
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
            }
        }catch (IOException ex){
            Toast.makeText(getBaseContext(), "Error in parsing", Toast.LENGTH_SHORT).show();
        }       if(activeMarker=="myMarker"){
            if(myMarker!=null) {
                myMarker.remove();
            }
            if(reset==true) {
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(myMarker.getPosition().latitude, myMarker.getPosition().longitude))
                        .title(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                        .snippet(addresses.get(0).getLocality()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14.0f));
                myMarker.setDraggable(true);
                mMap.setOnMarkerClickListener(this);
                myMarker.showInfoWindow();
                //mMap.setInfoWindowAdapter(adapter);
                //adapter.getInfoWindow(myMarker);
            }else{
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat,lon))
                        .title(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                        .snippet(addresses.get(0).getLocality()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14.0f));
                myMarker.setDraggable(true);
                mMap.setOnMarkerClickListener(this);
                myMarker.showInfoWindow();
            }
            EditText search = (EditText)findViewById(R.id.address_search);
            search.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1));
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
         x = mMap.addMarker(new MarkerOptions().position(new LatLng(22.56, 88.36)).title("C9ERTY").icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle)).snippet("50% full"));

        y = mMap.addMarker(new MarkerOptions().position(new LatLng(22.50, 88.36)).title("XYT6UI").icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle)).snippet("30% full"));

        x.showInfoWindow();
        y.showInfoWindow();
        //x.setDraggable(true);
        //y.setDraggable(true);

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

    public void gotocalculator(View v){
        Intent intent = new Intent(getApplicationContext(), Estimate.class);
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
            myMarker.showInfoWindow();



        } else{
            for(int i=0;i<markers.size();i++){
               if(marker.equals(markers.get(i))){
                   Marker m = markers.get(i);
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

}