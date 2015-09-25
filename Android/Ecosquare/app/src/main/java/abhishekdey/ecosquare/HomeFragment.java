package abhishekdey.ecosquare;

/**
 * Created by abhishekdey on 20/09/15.
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeFragment extends Fragment {

    public HomeFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
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

        return rootView;
    }






}
