package com.yorktown.yorktown.dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;


// helper class for displaying dialogs common across activities and fragments
public class ShowDialog {

    private Fragment parentFragment;

    public ShowDialog(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    public void showTimePickerDialog(int requestCode) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setTargetFragment(parentFragment, requestCode);
        newFragment.show(parentFragment.getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(int requestCode) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(parentFragment, requestCode);
        newFragment.show(parentFragment.getFragmentManager(), "datePicker");
    }
}
