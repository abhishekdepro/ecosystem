package abhishekdey.ecosquare;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;


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
        View convertView = mInflater.inflate(R.layout.fragment_one, null);

        TextView tv2=(TextView) convertView.findViewById(R.id.example1);
        TextView tv3=(TextView) convertView.findViewById(R.id.code);
        tv3.setText(ParseUser.getCurrentUser().get("Code").toString());

        return convertView;
    }

}