package com.yorktown.yorktown;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.ParseObject;
import com.yorktown.yorktown.eventbus.CreateStepEvent;
import com.yorktown.yorktown.eventbus.CreateTripEvent;
import com.yorktown.yorktown.eventbus.DeleteTripEvent;
import com.yorktown.yorktown.eventbus.EditStepEvent;
import com.yorktown.yorktown.eventbus.EditTripEvent;
import com.yorktown.yorktown.eventbus.ReadStepEvent;
import com.yorktown.yorktown.eventbus.ReadTripEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;


public class TripFragment extends ListFragment {

// *** GLOBAL PARAMETERS ***
    private JSONObject[] steps;
    private ParseObject parseObject;

// *** FACTORY ***
    public static TripFragment newInstance() {
        return new TripFragment();
    }


// *** LIFECYCLE ***
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allow fragment to contribute to the menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

// *** MENU ***
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // create a new step for this trip
        MenuItem addStepItem = menu.add(Menu.NONE, Menu.NONE, 1, "Add Step")
                .setIcon(R.drawable.ic_add_black_24dp);
        MenuItemCompat.setShowAsAction(addStepItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        addStepItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                EventBus.getDefault().post(new EditStepEvent(parseObject, -1));
                return true;
            }
        });

        // edit this trip
        MenuItem editTripItem = menu.add(Menu.NONE, Menu.NONE, 1, "Edit Trip")
                .setIcon(R.drawable.ic_create_grey600_24dp);
        MenuItemCompat.setShowAsAction(editTripItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        editTripItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                EventBus.getDefault().post(new EditTripEvent(parseObject));
                return true;
            }
        });

        // delete this trip
        MenuItem deleteTripItem = menu.add(Menu.NONE, Menu.NONE, 1, "Delete Trip")
                .setIcon(R.drawable.ic_delete_grey600_24dp);
        MenuItemCompat.setShowAsAction(deleteTripItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        deleteTripItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                EventBus.getDefault().post(new DeleteTripEvent());
                parseObject.deleteEventually();
                return true;
            }
        });
    }

// *** LISTENERS ***
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        EventBus.getDefault().post(new ReadStepEvent(parseObject, position));
    }

// *** EVENTBUS LISTENERS ***
    public void onEventMainThread(ReadTripEvent.FragmentEvent event){
        this.parseObject = event.parseObject;
        displayTrip();
    }

    public void onEventMainThread(CreateTripEvent.FragmentEvent event){
        this.parseObject = event.parseObject;
        displayTrip();
    }

    public void onEventMainThread(CreateStepEvent.FragmentEvent event){
        this.parseObject = event.parseObject;
        displayTrip();
    }

// *** HELPERS ***
    private void displayTrip() {
        JSONArray jsonArray = parseObject.getJSONArray("steps");
        steps = JSONHelpers.getJSONObjectArray(jsonArray);
        setListAdapter(new StepAdapter(getActivity(), R.layout.step_item, steps));
    }
}