package abhishekdey.ecosquare;

import android.content.Context;
import android.location.Criteria;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;


public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker myMarker;
    private double lat,lon;
    LocationManager locationManager ;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if(provider!=null && !provider.equals("")){

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if(location!=null){
                Toast.makeText(getBaseContext(), "Location is: "+location.getLatitude(), Toast.LENGTH_SHORT).show();
                onLocationChanged(location);
            }
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    @Override
    public void onLocationChanged(Location location) {
        // Getting reference to TextView tv_longitude


        // Setting Current Longitude
        lon = location.getLongitude();

        // Setting Current Latitude
        lat = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
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
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(22.56,88.36) , 8.0f) );
        mMap.addMarker(new MarkerOptions().position(new LatLng(22.56, 88.36)).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon))).setDraggable(true);
        mMap.setOnMarkerClickListener(this);

        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("My Spot")
                .snippet("This is my spot!"));

    }
    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(myMarker))
        {

        }
        return true;
    }
}
