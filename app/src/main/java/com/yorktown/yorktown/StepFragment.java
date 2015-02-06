package com.yorktown.yorktown;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yorktown.yorktown.eventbus.ReadStepEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class StepFragment extends Fragment {

// *** INITIALIZATION PARAMETERS ***
    private final static String ARG_POSITION = "position";

// *** GLOBAL PARAMETERS ***
    private ParseObject parseObject;

// *** UI ELEMENTS ***
    private SupportMapFragment mapFragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

// *** FACTORY ***
    public static StepFragment newInstance() {
        return new StepFragment();
    }

// *** LIFECYCLE ***
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allow fragment to contribute to the menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().registerSticky(this);



        // place the MapFragment
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }

        // initialize the map
        if (mMap == null) {
            mMap = mapFragment.getMap();
            //mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

// *** EVENTBUS LISTENERS ***
    public void onEventMainThread(ReadStepEvent.FragmentEvent event){
        this.parseObject = event.parseObject;

        updateStepView(event.position);
    }

// *** HELPERS ***
    private void updateStepView(int position) {

        JSONArray jsonArray = ParseHelpers.getJSONArray(parseObject, "steps");
        JSONObject jsonObject = JSONHelpers.getJSONObject(jsonArray, position);

        TextView stepTitle = (TextView) getActivity().findViewById(R.id.step_title);
        stepTitle.setText(JSONHelpers.getString(jsonObject, "name"));

        TextView stepDetails = (TextView) getActivity().findViewById(R.id.step_details);
        stepDetails.setText(JSONHelpers.getString(jsonObject, "details"));

        if (new Connectivity(getActivity()).isConnected()) {
            getLocationsOnline(JSONHelpers.getString(jsonObject, "location_id"));
        } else {
            getLocationsCached(JSONHelpers.getString(jsonObject, "location_id"));
        }
    }

    private void getLocationsOnline(final String locationId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");

        query.getInBackground(locationId, new GetCallback<ParseObject>() {
            public void done(final ParseObject location, ParseException e) {
                if (e == null) {

                    // toss old cache and cache new results by unpinning/pinning to Parse local datastore
                    ParseObject.unpinAllInBackground(location.getObjectId(), new DeleteCallback() {
                        public void done(ParseException e) {
                            location.pinInBackground();
                        }
                    });

                    // plot point on map
                    //plotOnMap(location);
                }
            }
        });
    }

    private void getLocationsCached(final String locationId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
        query.fromLocalDatastore();

        query.getInBackground(locationId, new GetCallback<ParseObject>() {
            public void done(ParseObject location, ParseException e) {
                if (e == null) {
                    //plotOnMap(location);
                }
            }
        });
    }

    private void plotOnMap(ParseObject location) {
        ParseGeoPoint geoPoint = location.getParseGeoPoint("geoPoint");
        String pointName = ParseHelpers.getString(location, "desc");

        final double latitude = geoPoint.getLatitude();
        final double longitude = geoPoint.getLongitude();

        LatLng coordinates = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));
        mMap.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(pointName));
        Log.d("map marker", latitude + ", " + longitude);

        enableDirections(latitude, longitude);
    }

    private void enableDirections(final double latitude, final double longitude) {
        // set listener for directions button
        Button button = (Button) getView().findViewById(R.id.directions);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDirections(latitude, longitude);
            }
        });

        // TODO: visual indication that button is pressable
    }

    // launch directions in Google Maps, from user's current location to location in step
    private void getDirections(final double latitude, final double longitude) {
        Uri gmmIntentUri;
        MainActivity mainActivity = (MainActivity) getActivity(); // get values from parent activity

        double userLat = mainActivity.latitude;
        double userLon = mainActivity.longitude;

        // if location is not available, perform search without start address
        if (Double.isNaN(userLat) || Double.isNaN(userLon)) {
            gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude);
        } else {
            gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr="+userLat+","+userLon+"&daddr="+latitude+","+longitude);
        }

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }



    // TODO: move location search
/*    private void map(View view) {
        Uri gmmIntentUri = null;

        MainActivity mainActivity = (MainActivity) getActivity(); // get values from parent activity
        String quer = ""; //TODO: tripActivity;
        String[] queries = quer.split("\"\\s+");
        StringBuilder qBuilder = new StringBuilder();
        for (String q: queries) {
            qBuilder.append(q + "+");
        }
        String query = qBuilder.toString();

        double lat = mainActivity.latitude;
        double lon = mainActivity.longitude;

        // if location is not available, perform search with 0,0
        if (Double.isNaN(lat) || Double.isNaN(lon)) {
            gmmIntentUri = Uri.parse("geo:0,0?q="+query); // TODO: with coordinates of user's city
        } else {
            gmmIntentUri = Uri.parse("geo:"+lat+","+lon+"?q="+query);
        }

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }*/

}
