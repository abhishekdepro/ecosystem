package abhishekdey.ecosquare;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by abhishekdey on 27/08/15.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xxx", "xxx");
    }
}