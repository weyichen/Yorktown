package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

public class CardsFragment extends ListFragment {

// *** VARIABLES ***
    OnTripSelectedListener mCallback;

    private String[] tripIdList; // stores the objectId fields of all trips retrieved from Parse

// *** INTERFACE ***
    public interface OnTripSelectedListener {
        public void onTripSelected(int position, String tripId);
    }

// *** ACTIVITY LIFECYCLE ***
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTripSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTripSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve all trips from Parse
        getAllTrips();
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: implement multi-fragment layout
    }

// *** LISTENERS ***
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Send the event to the host activity
        mCallback.onTripSelected(position, tripIdList[position]);
    }

// *** HELPERS ***
    private void getAllTrips() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");

        // only return these columns
        query.selectKeys(Arrays.asList("title", "details", "color", "rank"));

        // rank determines order in which trips will show up
        query.orderByAscending("rank");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tripList, ParseException e) {
                if (e == null) {
                    Log.d("Trip", "Retrieved " + tripList.size() + " trips");

                    // Create an array adapter for the list view
                    setListAdapter(new TripAdapter(getActivity(), R.layout.card_item, tripList));

                    // store objectIds of trips for TripFragment access
                    tripIdList = new String[tripList.size()];
                    for (int i=0; i<tripList.size(); i++) {
                        tripIdList[i] = tripList.get(i).getObjectId();
                        //Log.d("tripIdList", "tripId " + p.getObjectId());
                    }

                } else {
                    Log.d("Trip", "Error: " + e.getMessage());
                }
            }
        });
    }

}