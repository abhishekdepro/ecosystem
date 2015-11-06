package abhishekdey.ecosquare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Estimate extends ActionBarActivity {
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
        setContentView(R.layout.activity_estimate);

    }
    public void clicked(View v){
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

            AlertDialog.Builder builder = new AlertDialog.Builder(Estimate.this);
            builder.setMessage("Book a Pickup?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
    }

    public void book(){
       final ProgressDialog progress = ProgressDialog.show(Estimate.this, "Working",
                "The minions are working..", true);
        SharedPreferences prefs = this.getSharedPreferences(SignUp.PREFS_NAME, Context.MODE_PRIVATE);
        final String _lat = prefs.getString("lat", null);
        final String _lon = prefs.getString("lon", null);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                //Trying Ion
                JsonObject json = new JsonObject();
                json.addProperty("u_id", ParseUser.getCurrentUser().getUsername().toString());
                json.addProperty("emp_id", "");
                json.addProperty("lat", _lat);
                json.addProperty("lon", _lon);
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

                                    //Toast.makeText(getBaseContext(), "Pickup added successfully! We will contact you soon.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), CompleteBooking.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });


            }
        }).start();
        //finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_estimate, menu);
        return false;
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
        }

        return super.onOptionsItemSelected(item);
    }
}
