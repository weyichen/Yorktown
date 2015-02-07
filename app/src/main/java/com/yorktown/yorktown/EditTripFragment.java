package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yorktown.yorktown.eventbus.EditTripEvent;
import com.yorktown.yorktown.eventbus.ReadTripEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Daniel on 1/31/2015.
 */
public class EditTripFragment extends Fragment implements View.OnClickListener {

// *** GLOBAL PARAMETERS ***
    MainActivity mainActivity;
    private ParseObject parseObject; // if this is null, we are creating a new trip

// *** UI ELEMENTS ***
    private EditText titleEditText;

// *** FACTORY ***
    public static EditTripFragment newInstance() {
        return new EditTripFragment();
    }

// *** LIFECYCLE ***
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newtrip, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // set up UI elements
        titleEditText = (EditText) view.findViewById(R.id.trip_title);

        // set listeners for buttons
        view.findViewById(R.id.create_trip).setOnClickListener(this);
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

// *** LISTENERS ***
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_trip:
                if (parseObject == null) createTrip(); else editTrip();
                break;
        }
    }

// *** EVENTBUS LISTENERS ***
    public void onEventMainThread(EditTripEvent.FragmentEvent event) {
        parseObject = event.parseObject;

        // display editable information
        if (parseObject != null) {
            titleEditText.setText(ParseHelpers.getString(parseObject, "title"), TextView.BufferType.EDITABLE);
        }
    }

// *** HELPERS ***
    private void createTrip() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        // create the trip and use saveEventually to pin it, so that future queries to the local datastore will return it
        ParseObject newTrip = new ParseObject("Trip");

        String title = titleEditText.getText().toString();
        newTrip.put("title", title);

        // TODO: testing create a new array of steps, with the first step being the name of the trip
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONHelpers.put(jsonObject, "name", title);
        jsonArray.put(jsonObject);
        newTrip.put("steps", jsonArray);

        if (currentUser != null) newTrip.put("user", currentUser);
        newTrip.saveEventually();

        // close the NewTripFragment and pass on the newly created ParseObject
        EventBus.getDefault().post(new ReadTripEvent(newTrip));
    }

    private void editTrip() {
        String title = titleEditText.getText().toString();
        parseObject.put("title", title);
        parseObject.saveEventually();
        EventBus.getDefault().post(new ReadTripEvent(parseObject));
    }
}
