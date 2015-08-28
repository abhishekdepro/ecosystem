package abhishekdey.ecosquare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.parse.ParseObject;

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


public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,LocationListener {

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker myMarker,x,y;
    private double lat,lon;
    LocationManager locationManager ;
    String provider;
    ProgressDialog progress;

    GoogleMap.InfoWindowAdapter adapter;
    private String activeMarker = "myMarker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        MapsInitializer.initialize(getApplicationContext());
        setUpMapIfNeeded();
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        Button button= (Button) findViewById(R.id.button_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = ProgressDialog.show(MapsActivity.this, "Working",
                        "The minions are working..", true);

                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        //Trying Ion
                        JsonObject json = new JsonObject();
                        json.addProperty("u_id", "8981169454");
                        json.addProperty("emp_id", "876567890");
                        json.addProperty("lat", Double.toString(lat));
                        json.addProperty("lon", Double.toString(lon));
                        json.addProperty("paper", "5");
                        json.addProperty("plastic", "10");
                        json.addProperty("mode", "cash");
                        json.addProperty("status", "init");

                        Ion.with(getApplicationContext())
                                .load("http://ecosquare.herokuapp.com/transaction")
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        if(e!=null){
                                            Toast.makeText(getBaseContext(), "Data : "+e.getStackTrace(), Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getBaseContext(), "Pickup added successfully! We will contact you soon.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                progress.dismiss();
                            }
                        });
                    }
                }).start();




                /*try {
                    HttpURLConnection httpcon = (HttpURLConnection) ((new URL("http://ecosquare.herokuapp.com/transaction?" +
                            "u_id=8767856743&emp_id=9804770561&lat=22.56&lon=88.34&paper=20&plastic=10&mode=cash&status=init").openConnection()));
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    byte[] outputBytes = "{'value': 7.5}".getBytes("UTF-8");
                    OutputStream os = httpcon.getOutputStream();
                    os.write(outputBytes);

                    os.close();
                }catch (Exception e){
                    Toast.makeText(getBaseContext(), "Error "+e.toString(), Toast.LENGTH_LONG).show();
                }
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("u_id", "8981169454");
                data.put("emp_id", "9804770561");
                data.put("lat",Double.toString(lat));
                data.put("lon",Double.toString(lon));
                data.put("paper","10");
                data.put("plastic","10");
                data.put("mode","cash");
                data.put("status", "init");
                HttpLibrary post = new HttpLibrary(data);
                post.execute("http://ecosquare.herokuapp.com/transaction?u_id=8981169454&emp_id=9804770561" +
                        "&lat="+data.get("lat")+"&lon="+data.get("lon")+"&paper=20&plastic=10&mode=cash&status=init");
                Toast.makeText(getBaseContext(), "Data : "+data, Toast.LENGTH_LONG).show();*/
            }
        });
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
}       if(activeMarker=="myMarker"){
        if(myMarker!=null)
            myMarker.remove();
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                .snippet(addresses.get(0).getLocality()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 11.0f));
        myMarker.setDraggable(true);
        mMap.setOnMarkerClickListener(this);
        myMarker.showInfoWindow();
        //mMap.setInfoWindowAdapter(adapter);
        //adapter.getInfoWindow(myMarker);
    }
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
        mMap.setMyLocationEnabled(true);
         x = mMap.addMarker(new MarkerOptions().position(new LatLng(22.56, 88.36)).title("C9ERTY").icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle)).snippet("50% full"));

         y = mMap.addMarker(new MarkerOptions().position(new LatLng(22.50, 88.36)).title("XYT6UI").icon(BitmapDescriptorFactory.fromResource(R.drawable.recycle)).snippet("30% full"));

        x.showInfoWindow();
        y.showInfoWindow();
        //x.setDraggable(true);
        //y.setDraggable(true);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (mMap.getMyLocation() != null) {
                    lat = marker.getPosition().latitude;
                    lon = marker.getPosition().longitude;
                    Location location = new Location("Test");
                    location.setLatitude(lat);
                    location.setLongitude(lon);
                    onLocationChanged(location);
                }
                if (marker.equals(x)) {
                    activeMarker = "x";
                } else if (marker.equals(y)) {
                    activeMarker = "y";
                } else {
                    activeMarker = "myMarker";
                }

            }
        });

    }
    //POST HTTP

    public void locate_me(View v)
    {
        onLocationChanged(mMap.getMyLocation());
        call();
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
