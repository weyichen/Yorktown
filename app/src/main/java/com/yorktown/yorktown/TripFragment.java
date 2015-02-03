package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;


public class TripFragment extends ListFragment {

// *** LISTENER VARIABLES ***
    OnStepSelectedListener mCallback;

// *** INITIALIZATION PARAMETERS ***
    private final static String ARG_TRIPID = "trip_id";

// *** GLOBAL PARAMETERS ***
    private String mTripId;
    private JSONObject[] steps;

// *** FACTORY ***
    public static TripFragment newInstance(String tripId) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRIPID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

// *** INTERFACE ***
    public interface OnStepSelectedListener {
        public void onStepSelected(String step);
    }

// *** LIFECYCLE ***
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

        // get initialization parameters
        if (getArguments() != null) {
            mTripId = getArguments().getString(ARG_TRIPID);
        }

        // allow fragment to contribute to the menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        getItinerary(mTripId);
    }

// *** MENU ***
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem addTripItem = menu.add(Menu.NONE, Menu.NONE, 1, "Add Trip")
                .setIcon(R.drawable.ic_note_add_grey600_24dp);
        MenuItemCompat.setShowAsAction(addTripItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        addTripItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                openAdd();
                return true;
            }
        });
    }

    private void openAdd() {
        NewStepFragment newFragment = NewStepFragment.newInstance(mTripId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

// *** LISTENERS ***
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Send the event to the host activity
        mCallback.onStepSelected(steps[position].toString());
    }

// *** HELPERS ***
    private void getItinerary(final String tripId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
        query.fromLocalDatastore();

        query.getInBackground(tripId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.d("Trip", "Retrieved trip " + tripId);

                    JSONArray jsonArray = object.getJSONArray("steps");

                    steps = ParseHelpers.getJSONObjectArray(jsonArray);
                    setListAdapter(new StepAdapter(getActivity(), R.layout.step_item, steps));

                } else {
                    Log.d("Trip", "Failed to retrieve trip " + tripId);
                }
            }
        });
    }

}