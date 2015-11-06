package abhishekdey.ecosquare;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CompleteBooking extends AppCompatActivity {

    Uri uri = Uri.parse("android.resource://abhishekdey.ecosquare/drawable/leaf.png");
    CallbackManager callbackManager;
    ShareDialog shareDialog;

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
        setContentView(R.layout.activity_complete_booking);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    public void twitter_share(View v){
        String tweetUrl =
                String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                        Uri.encode("I recycled using @ecosquareapp. You should do the same! #cashfortrash"), Uri.encode("https://www.ecosquare.in"));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

// Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }

    public void fb_share(View v){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Ecosquare: Get Cash for Trash")
                    .setContentDescription(
                            "I recycled using @ecosquareapp. You should do the same! #cashfortrash")
                    .setContentUrl(Uri.parse("http://ecosquare.in"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

}
