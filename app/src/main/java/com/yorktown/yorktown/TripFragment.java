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
import com.yorktown.yorktown.eventbus.NewStepEvent;
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
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        EventBus.getDefault().post(new NewStepEvent(parseObject));
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