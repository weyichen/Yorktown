package com.yorktown.yorktown.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.yorktown.yorktown.DateHelpers;

import java.util.Calendar;

/**
 * Created by Daniel on 2/1/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // use the user-selected date format to format the time
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        String formattedTime = DateHelpers.formatTime(c.getTime());

        // return the correctly formatted date to the fragment that opened this fragment
        Intent intent = new Intent();
        intent.putExtra("string", formattedTime);
        intent.putExtra("hour", hourOfDay);
        intent.putExtra("minute", minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
    }
}