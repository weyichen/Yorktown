package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TripFragment extends ListFragment {

    // *** VARIABLES ***
    OnStepSelectedListener mCallback;

    final static String ARG_POSITION = "position";
    final static String ARG_TRIPID = "trip_id";

    int mCurrentPosition = -1;
    String mCurrentTripId = null;

    private JSONObject[] steps;

// *** INTERFACE ***
    public interface OnStepSelectedListener {
        public void onStepSelected(int position, String step);
    }

// *** ACTIVITY LIFECYCLE ***
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnStepSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStepSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            // Set trip based on argument passed in
            updateTripView(args.getInt(ARG_POSITION), args.getString(ARG_TRIPID));

        } else if (mCurrentPosition != -1) {
            // Set trip based on saved instance state defined during onCreateView
            updateTripView(mCurrentPosition, mCurrentTripId);
        }
    }

// *** INSTANCE STATE ***
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
        outState.putString(ARG_TRIPID, mCurrentTripId);
    }

// *** LISTENERS ***
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Send the event to the host activity
        mCallback.onStepSelected(position, steps[position].toString());
    }

// *** HELPERS
    private void updateTripView(int position, String tripId) {
        mCurrentPosition = position;
        mCurrentTripId = tripId;

        getItinerary(tripId);
    }

    private void getItinerary(final String tripId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
        query.fromLocalDatastore(); // search the local datastore since all trips were already cached when they were fetched in CardsFragment

        query.getInBackground(tripId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.d("Trip", "Retrieved trip " + tripId);

                    JSONArray jsonArray = object.getJSONArray("steps");
                    parseJSON(jsonArray);
                } else {
                    Log.d("Trip", "Failed to retrieve trip " + tripId);
                }
            }
        });
    }

    private void parseJSON(JSONArray jsonArray) {

        try {
            steps = new JSONObject[jsonArray.length()];

            for(int i=0; i<jsonArray.length(); i++) {
                steps[i] = jsonArray.getJSONObject(i);
            }

            setListAdapter(new StepAdapter(getActivity(), R.layout.step_item, steps));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}