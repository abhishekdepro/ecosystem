package abhishekdey.ecosquare;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.parse.ParseUser;

import java.text.ParseException;


public class OneFragment extends Fragment {
    public static String _code;
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
        _code = "No Code used";
        View convertView = mInflater.inflate(R.layout.fragment_one, null);

        TextView tv2=(TextView) convertView.findViewById(R.id.example1);
        final TextView tv3=(TextView) convertView.findViewById(R.id.code);

        try{
            Ion.with(getActivity())

                    .load(getString(R.string.url)+"/user/"+ParseUser.getCurrentUser().getUsername().toString()+"/ref")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if(result!=null){
                                _code = result.get("ref").toString().replace("\"", "");
                                tv3.setText(_code);
                            }else{
                                Toast.makeText(getActivity(), "Sorry! We can't fetch your data, maybe a connection issue.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch(Exception ex){
            Toast.makeText(getActivity(), "Sorry! We can't fetch your data, maybe a connection issue.", Toast.LENGTH_LONG).show();
        }


        return convertView;
    }

}