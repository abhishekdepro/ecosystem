package abhishekdey.ecosquare;

/**
 * Created by abhishekdey on 20/09/15.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pkmmte.view.CircularImageView;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeFragment extends Fragment {
    public static CircularImageView cv;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    public HomeFragment(){}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tv=(TextView) rootView.findViewById(R.id.nameShow);

        TextView tv1=(TextView) rootView.findViewById(R.id.emailShow);
        TextView tv2=(TextView) rootView.findViewById(R.id.phoneShow);
        TextView tv3=(TextView) rootView.findViewById(R.id.coinsShow);
        TextView tv4=(TextView) rootView.findViewById(R.id.NAME);
        TextView tv5=(TextView) rootView.findViewById(R.id.hello);

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Raleway.ttf");

        tv.setTypeface(face);
        tv1.setTypeface(face);tv2.setTypeface(face);
        tv3.setTypeface(face);tv4.setTypeface(face);tv5.setTypeface(face);

        SharedPreferences prefs = getActivity().getSharedPreferences(SignUp.PREFS_NAME, Context.MODE_PRIVATE);
        String ImageUri = prefs.getString("photo", null);
        if(ImageUri!=null) {
            Uri img = Uri.parse(ImageUri);
            cv.setImageURI(img);
        }
        tv2.setText(ParseUser.getCurrentUser().getUsername());
        tv1.setText(ParseUser.getCurrentUser().getEmail());
        tv.setText(ParseUser.getCurrentUser().get("Name").toString());

        cv = (CircularImageView)rootView.findViewById(R.id.photo);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println(selectedImagePath);
                cv.setImageURI(selectedImageUri);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(SignUp.PREFS_NAME, Activity.MODE_PRIVATE).edit();
                editor.putString("photo", selectedImageUri.toString());
                editor.commit();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }






}
