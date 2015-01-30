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

import org.json.JSONException;
import org.json.JSONObject;

public class StepFragment extends Fragment {

// *** VARIABLES ***
    final static String ARG_POSITION = "position";
    final static String ARG_STEP = "step";

    int mCurrentPosition = -1;
    String mCurrentStep = null;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

// *** ACTIVITY LIFECYCLE ***
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
            mCurrentStep = savedInstanceState.getString(ARG_STEP);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // add SupportMapFragment to container in StepFragment
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // initialize mMap
        if (mMap == null) {
            mMap = mapFragment.getMap();
            mMap.setMyLocationEnabled(true);
        }

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set view based on argument passed in
            updateStepView(args.getInt(ARG_POSITION), args.getString(ARG_STEP));
        } else if (mCurrentPosition != -1) {
            // Set view based on saved instance state defined during onCreateView
            updateStepView(mCurrentPosition, mCurrentStep);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

// *** INSTANCE STATE ***
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
        outState.putString(ARG_STEP, mCurrentStep);
    }

// *** HELPERS ***
    private void updateStepView(int position, String jsonString) {

        mCurrentPosition = position;
        mCurrentStep = jsonString;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            TextView stepTitle = (TextView) getActivity().findViewById(R.id.step_title);
            stepTitle.setText(jsonObject.getString("name"));

            TextView stepDetails = (TextView) getActivity().findViewById(R.id.step_details);
            stepDetails.setText(jsonObject.getString("details"));

            if (new Connectivity(getActivity()).isConnected()) {
                getLocationsOnline(jsonObject.getString("location_id"));
            } else {
                getLocationsCached(jsonObject.getString("location_id"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
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
                    plotOnMap(location);
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
                    plotOnMap(location);
                }
            }
        });
    }

    private void plotOnMap(ParseObject location) {
        ParseGeoPoint geoPoint = location.getParseGeoPoint("geoPoint");
        String pointName = ParseHelpers.getString("desc", location);

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
