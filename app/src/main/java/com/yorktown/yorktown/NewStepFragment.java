package com.yorktown.yorktown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseUser;
import com.yorktown.yorktown.dialog.ShowDialog;

/**
 * Created by Daniel on 2/1/2015.
 */
public class NewStepFragment extends Fragment implements View.OnClickListener {

    protected static final int DATE_CODE = 0;
    protected static final int TIME_CODE = 1;

    private ShowDialog showDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_newstep, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {



        // populate the step type spinner with an array from the strings resource file
        Spinner typeSpinner = (Spinner) view.findViewById(R.id.step_type);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.step_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        showDialog = new ShowDialog(this);

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
                createStep(v);
                break;
            case R.id.step_date:
                showDialog.showDatePickerDialog(DATE_CODE);
                break;
            case R.id.step_time:
                showDialog.showTimePickerDialog(TIME_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DATE_CODE) {
            int year = data.getIntExtra("year", 0);
            int month = data.getIntExtra("month", 0);
            int day = data.getIntExtra("day", 0);

            ((Button) getView().findViewById(R.id.step_date)).setText(year + "/" + month + "/" + day);
        }

        else if (requestCode == TIME_CODE) {
            int hourOfDay = data.getIntExtra("hourOfDay", 0);
            int minute = data.getIntExtra("minute", 0);

            ((Button) getView().findViewById(R.id.step_time)).setText(hourOfDay + ":" + minute);
        }
    }

// *** HELPERS ***
    private void createStep(View view) {

        // grab information entered by user
        EditText tripTitle = (EditText) getView().findViewById(R.id.trip_title);
        ParseUser currentUser = ParseUser.getCurrentUser();

        //
    }
}
