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
import com.yorktown.yorktown.eventbus.NewTripEvent;
import com.yorktown.yorktown.eventbus.ReadTripEvent;

import de.greenrobot.event.EventBus;

public class CardsFragment extends ListFragment {

// *** GLOBAL PARAMETERS ***
    MainActivity mainActivity;

// *** FACTORY ***
    public static CardsFragment newInstance() {
        return new CardsFragment();
    }

// *** LIFECYCLE ***
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;
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

        // retrieve all trips from Parse
        if (new Connectivity(getActivity()).isConnected()) {
            mainActivity.localStore.syncTrips();
        }

        // TODO: implement multi-fragment layout

    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.localStore.fetchCachedTrips(this, R.layout.card_item);

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
        EventBus.getDefault().post(new NewTripEvent());
    }

// *** LISTENERS ***
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject tripObject = mainActivity.localStore.getTrips().get(position);
        EventBus.getDefault().post(new ReadTripEvent(tripObject));
    }



// *** HELPERS ***

}
