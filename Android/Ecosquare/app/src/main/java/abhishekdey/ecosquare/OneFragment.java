package abhishekdey.ecosquare;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.text.ParseException;


public class OneFragment extends Fragment {

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater mInflater = (LayoutInflater) getActivity().getLayoutInflater();
        String _code="No Code used";
        try{
        _code = ParseUser.getCurrentUser().get("Code").toString();
        }catch(Exception ex){
            Toast.makeText(getActivity(), "Sorry! We can't fetch your data, maybe a connection issue.", Toast.LENGTH_LONG).show();
        }
        View convertView = mInflater.inflate(R.layout.fragment_one, null);

        TextView tv2=(TextView) convertView.findViewById(R.id.example1);
        TextView tv3=(TextView) convertView.findViewById(R.id.code);
        tv3.setText(_code);

        return convertView;
    }

}