package com.yorktown.yorktown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Daniel on 1/31/2015.
 */
public class NewTripFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_newtrip, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Button button = (Button) view.findViewById(R.id.create_trip);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTrip(v);
            }
        });
    }

    private void createTrip(View view) {
        EditText tripTitle = (EditText) view.findViewById(R.id.trip_title);
        ParseUser currentUser = ParseUser.getCurrentUser();

        // create the trip and use saveEventually to pin it, so that future queries to Trip will return it
        ParseObject newTrip = new ParseObject("Trip");
        newTrip.put("title", tripTitle.getText().toString());
        try {
            newTrip.put("steps", new JSONArray("[]")); // create an empty but non-null array on Parse, so that value will not be undefined
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (currentUser != null) newTrip.put("user", currentUser);

        newTrip.saveEventually();

        String newTripId = newTrip.getObjectId();
        Log.d("newTripId", "new trip is " + newTripId);

        // create a new TripFragment for the selected trip, passing in the trip ID
        TripFragment newFragment = new TripFragment();
        Bundle args = new Bundle();
        args.putString(TripFragment.ARG_TRIPID, newTripId);
        newFragment.setArguments(args);

        // show the fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }
}
