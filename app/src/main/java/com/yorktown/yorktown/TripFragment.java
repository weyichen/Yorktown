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
import org.json.JSONException;
import org.json.JSONObject;


public class TripFragment extends ListFragment {

    // *** VARIABLES ***
    OnStepSelectedListener mCallback;

    final static String ARG_TRIPID = "trip_id";

    String mCurrentTripId;

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

        // allow fragment to contribute to the menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            // Set trip based on argument passed in
            updateTripView(args.getString(ARG_TRIPID));

        } else if (mCurrentTripId != null) {
            // Set trip based on saved instance state defined during onCreateView
            updateTripView(mCurrentTripId);
        }
    }

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

    // *** MENU ITEMS
    private void openAdd() {
        NewStepFragment newFragment = new NewStepFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

// *** INSTANCE STATE ***
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putString(ARG_TRIPID, mCurrentTripId);
    }

// *** LISTENERS ***
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Send the event to the host activity
        mCallback.onStepSelected(position, steps[position].toString());
    }

// *** HELPERS
    private void updateTripView(String tripId) {
        mCurrentTripId = tripId;

        getItinerary(tripId);
    }

    private void getItinerary(final String tripId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
        query.fromLocalDatastore();

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