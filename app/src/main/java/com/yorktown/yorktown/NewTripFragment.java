package com.yorktown.yorktown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Daniel on 1/31/2015.
 */
public class NewTripFragment extends Fragment implements View.OnClickListener {

// *** UI ELEMENTS ***
    private EditText titleEditText;

// *** FACTORY ***
    public static NewTripFragment newInstance() {
        NewTripFragment fragment = new NewTripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

// *** LIFECYCLE ***
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newtrip, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // set up UI elements
        titleEditText = (EditText) view.findViewById(R.id.trip_title);

        // set listeners for buttons
        view.findViewById(R.id.create_trip).setOnClickListener(this);
    }

// *** LISTENERS ***
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_trip:
                createTrip();
                break;
        }
    }

// *** BUTTON HELPERS ***
    private void createTrip() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        // create the trip and use saveEventually to pin it, so that future queries to Trip will return it
        ParseObject newTrip = new ParseObject("Trip");
        newTrip.put("title", titleEditText.getText().toString());

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
        TripFragment newFragment = TripFragment.newInstance(newTripId);

        // show the fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }
}
