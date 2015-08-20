package abhishekdey.ecosquare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.mikepenz.materialdrawer.DrawerBuilder;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


public class MapsActivity extends ActionBarActivity implements GoogleMap.OnMarkerClickListener,LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker myMarker,x,y;
    private double lat,lon;
    LocationManager locationManager ;
    String provider;
    GoogleMap.InfoWindowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapsInitializer.initialize(getApplicationContext());
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

    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    String city = addresses.get(0).getLocality();
    String state = addresses.get(0).getAdminArea();
    String country = addresses.get(0).getCountryName();
    String postalCode = addresses.get(0).getPostalCode();
    String knownName = addresses.get(0).getFeatureName();

}catch (IOException ex){
    Toast.makeText(getBaseContext(), "Error in parsing", Toast.LENGTH_SHORT).show();
}
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
                .snippet(addresses.get(0).getLocality()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10.0f));
        myMarker.setDraggable(true);
        mMap.setOnMarkerClickListener(this);
        myMarker.showInfoWindow();
        //mMap.setInfoWindowAdapter(adapter);
        //adapter.getInfoWindow(myMarker);
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

         x = mMap.addMarker(new MarkerOptions().position(new LatLng(22.56, 88.36)).title("C9ERTY").icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle)).snippet("50% full"));

         y = mMap.addMarker(new MarkerOptions().position(new LatLng(22.50, 88.36)).title("XYT6UI").icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle)).snippet("30% full"));

        x.showInfoWindow();
        y.showInfoWindow();
        x.setDraggable(true);
        y.setDraggable(true);

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
        }
        return true;
    }
}
