package com.yorktown.yorktown;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

// Use this class to track the number of locally-created trips and locations which have not been saved to the Parse server, and therefore do not have Parse objectIds yet
public class LocalStore {

    private int numTrips;
    private int numLocations;
    private String[] tripIds;

    private MainActivity mainActivity;

    public LocalStore(MainActivity activity) {
        this.mainActivity = activity;
    }

    public int getNumTrips() {
        return numTrips;
    }

    public int getNumLocations() {
        return numLocations;
    }

    public void addTrip() {
        numTrips++;
    }

    public void addLocation() {
        numLocations++;
    }

    public void syncTrips(final CardsFragment cardsFragment, final int itemLayoutId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");

        // only return these columns - doesn't work with local datastore
        //query.selectKeys(Arrays.asList("title", "details", "color", "rank"));

        // rank determines order in which trips will show up
        query.orderByAscending("rank");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> tripList, ParseException e) {
                if (e == null) {
                    Log.d("Trip", "Retrieved " + tripList.size() + " trips");

                    // toss old cache and cache new results by unpinning/pinning to Parse local datastore
                    ParseObject.unpinAllInBackground("allTrips", new DeleteCallback() {
                        public void done(ParseException e) {
                            ParseObject.pinAllInBackground("allTrips", tripList);
                        }
                    });

                    // Create an array adapter containing fetched trips for the ListView
                    cardsFragment.setListAdapter(new TripAdapter(mainActivity, itemLayoutId, tripList));

                    // store objectIds of trips for TripFragment access
                    tripIds = new String[tripList.size()];
                    for (int i=0; i<tripList.size(); i++) {
                        tripIds[i] = tripList.get(i).getObjectId();
                    }

                } else {
                    Log.d("Trip", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void getCachedTrips(final CardsFragment cardsFragment, final int itemLayoutId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
        query.fromPin("allTrips");
        query.orderByAscending("rank");
        query.fromLocalDatastore();

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tripList, ParseException e) {
                Log.d("Trip", "Retrieved " + tripList.size() + " trips offline");

                // Create an array adapter containing fetched trips for the ListView
                cardsFragment.setListAdapter(new TripAdapter(mainActivity, itemLayoutId, tripList));

                // store objectIds of trips for TripFragment access
                tripIds = new String[tripList.size()];
                for (int i=0; i<tripList.size(); i++) {
                    tripIds[i] = tripList.get(i).getObjectId();
                }
            }
        });
    }

    private void storeTripIds() {

    }
}
