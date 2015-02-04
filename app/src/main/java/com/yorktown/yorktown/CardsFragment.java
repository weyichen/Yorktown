package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.ParseObject;

import java.util.List;

public class CardsFragment extends ListFragment {

// *** LISTENER VARIABLES ***
    OnTripSelectedListener mCallback;

// *** GLOBAL PARAMETERS ***
    private String[] tripIdList; // stores the objectId fields of all trips retrieved from Parse
    MainActivity mainActivity;

// *** FACTORY ***
    public static CardsFragment newInstance() {
        CardsFragment fragment = new CardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

// *** INTERFACE ***
    public interface OnTripSelectedListener {
        public void onTripSelected(String tripId);
    }

// *** LIFECYCLE ***
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;

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

        // allow fragment to contribute to the menu
        setHasOptionsMenu(true);


    }

    @Override
    public void onStart() {
        super.onStart();



        // TODO: implement multi-fragment layout

    }

    @Override
    public void onResume() {
        super.onResume();

        // retrieve all trips from Parse
        if (new Connectivity(getActivity()).isConnected()) {
            mainActivity.localStore.syncTrips(this, R.layout.card_item);
        } else {
            mainActivity.localStore.getCachedTrips(this, R.layout.card_item);
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
        NewTripFragment newFragment = NewTripFragment.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

// *** LISTENERS ***
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Send the event to the host activity
        mCallback.onTripSelected(tripIdList[position]);
    }

// *** HELPERS ***




    private void displayTrips(List<ParseObject> tripList) {
    }

}
