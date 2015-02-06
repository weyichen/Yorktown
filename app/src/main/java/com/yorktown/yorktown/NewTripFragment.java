package com.yorktown.yorktown;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yorktown.yorktown.eventbus.NewTripEvent;
import com.yorktown.yorktown.eventbus.ReadTripEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Daniel on 1/31/2015.
 */
public class NewTripFragment extends Fragment implements View.OnClickListener {

// *** GLOBAL PARAMETERS ***
    MainActivity mainActivity;

// *** UI ELEMENTS ***
    private EditText titleEditText;

// *** FACTORY ***
    public static NewTripFragment newInstance() {
        NewTripFragment fragment = new NewTripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

// *** INTERFACE ***
    public interface OnTripCreatedListener {
        public void onTripCreated(ParseObject parseObject, NewTripFragment fragment);
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
                createTrip();
                break;
        }
    }

// *** EVENTBUS LISTENERS ***
    public void onEventMainThread(NewTripEvent.FragmentEvent event) {

    }

// *** BUTTON HELPERS ***
    private void createTrip() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        // create the trip and use saveEventually to pin it, so that future queries to the local datastore will return it
        ParseObject newTrip = new ParseObject("Trip");

        String title = titleEditText.getText().toString();
        newTrip.put("title", title);

        // create an empty but non-null array on Parse, so that value will not be undefined
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONHelpers.put(jsonObject, "name", title);
        jsonArray.put(jsonObject);
        newTrip.put("steps", jsonArray);

        if (currentUser != null) newTrip.put("user", currentUser);
        newTrip.saveEventually();

        // close the NewTripFragment
        EventBus.getDefault().post(new ReadTripEvent(newTrip));
    }
}
