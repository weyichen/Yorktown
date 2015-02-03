package com.yorktown.yorktown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yorktown.yorktown.dialog.ShowDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 2/1/2015.
 */
public class NewStepFragment extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

// *** INITIALIZATION PARAMETERS ***
    private final static String ARG_TRIPID = "trip_id";

// *** GLOBAL PARAMETERS ***
    private ShowDialog showDialog;
    private int year, month, day, hour, minute;

// *** UI ELEMENTS ***
    private EditText nameEditText;
    private Spinner typeSpinner;
    private Button dateButton, timeButton;

// *** REQUEST CODES ***
    protected static final int DATE_CODE = 0;
    protected static final int TIME_CODE = 1;

// *** FACTORY ***
    public static NewStepFragment newInstance(String tripId) {
        NewStepFragment fragment = new NewStepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRIPID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

// *** LIFECYCLE ***
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_newstep, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // set up UI elements
        nameEditText = (EditText) view.findViewById(R.id.trip_title);
        typeSpinner = (Spinner) view.findViewById(R.id.step_type);
        dateButton = (Button) view.findViewById(R.id.step_date);
        timeButton = (Button) view.findViewById(R.id.step_time);

        // populate the step type spinner with an array from the strings resource file
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.step_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        //enable date and time dialogs
        showDialog = new ShowDialog(this);

        // get the current date and time
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // display the current date and time in the date and time selector buttons
        Date date = c.getTime();
        dateButton.setText(DateHelpers.formatDate(date));
        timeButton.setText(DateHelpers.formatTime(date));

        // set listeners for buttons
        view.findViewById(R.id.step_date).setOnClickListener(this);
        view.findViewById(R.id.step_time).setOnClickListener(this);
        view.findViewById(R.id.create_step).setOnClickListener(this);
    }

// *** LISTENERS ***
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_step:
                createStep();
                break;
            case R.id.step_date:
                showDialog.showDatePickerDialog(DATE_CODE);
                break;
            case R.id.step_time:
                showDialog.showTimePickerDialog(TIME_CODE);
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        CharSequence spinnerChoice = (CharSequence) parent.getItemAtPosition(pos);
        Log.d("spinnerChoice", spinnerChoice.toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // display the date and time selected by the user, according to the system DateFormat
        if(requestCode == DATE_CODE) {
            ((Button) getView().findViewById(R.id.step_date)).setText(data.getStringExtra("string"));
            year = data.getIntExtra("year", year);
            month = data.getIntExtra("month", month);
            day = data.getIntExtra("day", day);
        }
        else if (requestCode == TIME_CODE) {
            ((Button) getView().findViewById(R.id.step_time)).setText(data.getStringExtra("string"));
            hour = data.getIntExtra("hour", hour);
            minute = data.getIntExtra("minute", minute);
        }
    }

// *** HELPERS ***
    private void createStep() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        // grab information entered by user
        String stepName = nameEditText.getText().toString();
        int stepType = typeSpinner.getSelectedItemPosition();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        String date = DateHelpers.formatJSONTime(c.getTime());

        // construct JSONObject and place it in the JSONArray for the trip
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("name", stepName);
            jsonData.put("type", stepType);
            jsonData.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trip");
        query.fromLocalDatastore();

        /*query.getInBackground(tripId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                }
            }
        });*/

    }
}
