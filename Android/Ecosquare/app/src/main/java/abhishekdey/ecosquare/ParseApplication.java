package abhishekdey.ecosquare;

import android.app.Application;

import com.parse.Parse;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by abhishekdey on 27/08/15.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Lg41go4Abua0UIYThLXKHcsgWbWpuVbVehru45xP", "hUsTpwaIAyzYtroir8y4XR5SXPz3NpJmHi0FbwUs");
        CalligraphyConfig.initDefault("fonts/Raleway.ttf");
    }
}
