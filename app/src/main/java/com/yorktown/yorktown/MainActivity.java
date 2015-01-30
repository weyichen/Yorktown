package com.yorktown.yorktown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends ActionBarActivity implements
        CardsFragment.OnTripSelectedListener,
        TripFragment.OnStepSelectedListener,
        LocationFragment.OnLocationListener {

// *** VARIABLES ***

    // tag for LocationFragment, which runs without a UI
    protected static final String LOCATION_TAG = "location";

    // coordinates obtained by LocationFragment
    protected double latitude = Double.NaN, longitude = Double.NaN;


    // request code for LoginActivity
    protected static final int LOGIN_CODE = 1;



// *** ACTIVITY LIFECYCLE ***
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // set up fragments: LocationFragment (background) and TabFragment (foreground)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new TabFragment())
                    .add(new LocationFragment(), LOCATION_TAG)
                    .commit();
        }
    }

// *** UI COMPONENTS ***
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // change "sign in" to "sign out" if user is signed in
        if (ParseUser.getCurrentUser() != null) {
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_login).setVisible(false);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
        }

        return true;
    }

// *** LISTENERS ***
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if log in is is successful, we refresh the activity
        if(requestCode == 1 && resultCode == RESULT_OK) {
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_login:
                openLogIn();
                return true;
            case R.id.action_logout:
                openLogOut();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTripSelected(int position, String tripId) {
        // Create fragment and give it an argument for the selected item
        TripFragment newFragment = new TripFragment();
        Bundle args = new Bundle();
        args.putInt(TripFragment.ARG_POSITION, position);
        args.putString(TripFragment.ARG_TRIPID, tripId); // pass trip ID
        Log.d("ARG_TRIPID", "tripID has been set " + tripId);
        newFragment.setArguments(args);

        // add the fragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStepSelected(int position, String jsonString) {
        // Create fragment and give it an argument for the selected item
        StepFragment newFragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(StepFragment.ARG_POSITION, position);
        args.putString(StepFragment.ARG_STEP, jsonString);
        newFragment.setArguments(args);

        // add the fragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLocation(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    // *** MENU ITEMS ***
    private void openLogIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_CODE);
    }

    private void openLogOut() {
        ParseUser.logOut();
        invalidateOptionsMenu();
    }

    private void openSearch() {
        final String testJSON = DebugData.testJSON;

        // update the object
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");

        try {
            final JSONArray jsonArray = new JSONArray(testJSON);

            query.getInBackground("K1vcTQl4ON", new GetCallback<ParseObject>() {
                public void done(ParseObject trip, ParseException e) {
                    if (e == null) {
                        trip.put("steps", jsonArray);
                        trip.saveInBackground();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

// *** HELPERS ***
}