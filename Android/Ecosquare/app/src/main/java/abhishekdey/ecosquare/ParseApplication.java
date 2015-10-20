package abhishekdey.ecosquare;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by abhishekdey on 27/08/15.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xxx", "xxx");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        CalligraphyConfig.initDefault("fonts/Raleway.ttf");
    }
}
