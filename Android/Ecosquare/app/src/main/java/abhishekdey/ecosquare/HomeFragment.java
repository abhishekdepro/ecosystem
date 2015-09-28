package abhishekdey.ecosquare;

/**
 * Created by abhishekdey on 20/09/15.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pkmmte.view.CircularImageView;

import java.io.FileDescriptor;
import java.io.IOException;
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
        final TextView tv=(TextView) rootView.findViewById(R.id.nameShow);

        final TextView tv1=(TextView) rootView.findViewById(R.id.emailShow);
        TextView tv2=(TextView) rootView.findViewById(R.id.phoneShow);
        TextView tv3=(TextView) rootView.findViewById(R.id.coinsShow);
        final TextView tv4=(TextView) rootView.findViewById(R.id.NAME);
        TextView tv5=(TextView) rootView.findViewById(R.id.hello);

        tv2.setEnabled(false);
        tv3.setEnabled(false);

        Typeface face= Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Raleway.ttf");
        cv = (CircularImageView)rootView.findViewById(R.id.photo);
        tv.setTypeface(face);
        tv1.setTypeface(face);tv2.setTypeface(face);
        tv3.setTypeface(face);tv4.setTypeface(face);tv5.setTypeface(face);

        SharedPreferences prefs = getActivity().getSharedPreferences(SignUp.PREFS_NAME, Context.MODE_PRIVATE);
        String ImageUri = prefs.getString("photo", "");
        if(ImageUri!=null) {
            Uri img = Uri.parse(ImageUri);
            try{
                Bitmap p = getBitmapFromUri(img);
                cv.setImageBitmap(p);
            }catch(IOException ex){

            }

        }

        try{
            Ion.with(getActivity())

                    .load(getString(R.string.url)+"/user/"+ParseUser.getCurrentUser().getUsername().toString())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if(result!=null){
                                tv1.setText(result.get("email").toString().replace("\"", ""));
                                tv4.setText(result.get("name").toString().replace("\"", ""));
                                tv.setText(result.get("name").toString().replace("\"", ""));
                            }else{
                                Toast.makeText(getActivity(), "Sorry! We can't fetch your data, maybe a connection issue.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch(Exception ex){
            Toast.makeText(getActivity(), "Sorry! We can't fetch your data, maybe a connection issue.", Toast.LENGTH_LONG).show();
        }

        tv2.setText(ParseUser.getCurrentUser().getUsername());




        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19){
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                }


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

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }








}
