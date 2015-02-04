package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Daniel on 1/31/2015.
 */
public class NewTripFragment extends Fragment implements View.OnClickListener {

// *** LISTENER VARIABLES ***
    OnTripCreatedListener mCallback;

// *** GLOBAL PARAMETERS ***
    MainActivity mainActivity;

// *** UI ELEMENTS ***
    private EditText titleEditText;

// *** FACTORY ***
    public static NewTripFragment newInstance() {
        NewTripFragment fragment = new NewTripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

// *** INTERFACE ***
    public interface OnTripCreatedListener {
        public void onTripCreated(NewTripFragment fragment);
    }

// *** LIFECYCLE ***
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) getActivity();

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTripCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTripCreatedListener");
        }
    }

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

        // create the trip and use saveEventually to pin it, so that future queries to the local datastore will return it
        ParseObject newTrip = new ParseObject("Trip");
        newTrip.put("title", titleEditText.getText().toString());

        // create an empty but non-null array on Parse, so that value will not be undefined
        newTrip.put("steps", JSONHelpers.newJSONArray("[]"));

        if (currentUser != null) newTrip.put("user", currentUser);
        newTrip.saveEventually();

        mainActivity.localStore.addTrip();
        ParseHelpers.pin(newTrip, "trip" +mainActivity.localStore.getNumTrips());

        // close the NewTripFragment
        mCallback.onTripCreated(this);

//        String newTripId = newTrip.getObjectId();
//        Log.d("newTripId", "new trip is " + newTripId);
//
//        // create a new TripFragment for the selected trip, passing in the trip ID
//        TripFragment newFragment = TripFragment.newInstance(newTripId);
//
//        // show the fragment
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, newFragment)
//                .addToBackStack(null)
//                .commit();
    }
}
