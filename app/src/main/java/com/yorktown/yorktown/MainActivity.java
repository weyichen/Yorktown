package com.yorktown.yorktown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yorktown.yorktown.eventbus.CreateStepEvent;
import com.yorktown.yorktown.eventbus.CreateTripEvent;
import com.yorktown.yorktown.eventbus.DeleteStepEvent;
import com.yorktown.yorktown.eventbus.DeleteTripEvent;
import com.yorktown.yorktown.eventbus.EditStepEvent;
import com.yorktown.yorktown.eventbus.EditTripEvent;
import com.yorktown.yorktown.eventbus.GetLocationEvent;
import com.yorktown.yorktown.eventbus.ReadStepEvent;
import com.yorktown.yorktown.eventbus.ReadTripEvent;

import org.json.JSONArray;
import org.json.JSONException;

import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity {

// *** GLOBAL VARIABLES ***
    protected double latitude = Double.NaN, longitude = Double.NaN;
    protected LocalStore localStore;

// *** FRAGMENT TAGS ***
    protected static final String TAB_TAG = "tab";
    protected static final String CARDS_TAG = "cards";
    protected static final String TRIP_TAG = "trip";
    protected static final String LOCATION_TAG = "location";

// *** REQUEST CODE ***
    protected static final int LOGIN_CODE = 1;


// *** LIFECYCLE ***
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        localStore = new LocalStore(this);

        // set up fragments: LocationFragment (background) and TabFragment (foreground)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new TabFragment(), CARDS_TAG)
                    .add(new LocationFragment(), LOCATION_TAG)
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        invalidateOptionsMenu();
    }

// *** EVENTBUS LISTENER ***
    public void onEvent(EditTripEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        ParseObject parseObject = event.parseObject;

        EditTripFragment newFragment = EditTripFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(CreateTripEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        // close the NewTripFragment used to create this trip
        getSupportFragmentManager().popBackStack();

        TripFragment newFragment = TripFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(ReadTripEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        TripFragment newFragment = TripFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(DeleteTripEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        // close the TripFragment used to delete this trip
        getSupportFragmentManager().popBackStack();
    }

    public void onEvent(EditStepEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        EditStepFragment newFragment = EditStepFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(CreateStepEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        // close the NewStepFragment and TripFragment used to create this trip
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();

        TripFragment newFragment = TripFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(ReadStepEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        StepFragment newFragment = StepFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onEvent(DeleteStepEvent event) {
        EventBus.getDefault().postSticky(event.new FragmentEvent());

        // close the StepFragment used to delete this step
        getSupportFragmentManager().popBackStack();
    }

    public void onEvent(GetLocationEvent event) {
        this.latitude = event.latitude;
        this.longitude = event.longitude;
    }


// *** LISTENERS ***
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if log in is is successful, we refresh the activity
        if(requestCode == 1 && resultCode == RESULT_OK) {
            invalidateOptionsMenu();
        }
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