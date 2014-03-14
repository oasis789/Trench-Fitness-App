package com.trenchgym.trenchfitnessapp;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

    OnDateSetListener ondateSet;
    boolean showCalendar, showSpinners;

    public DatePickerFragment() {
    }
    
    public void setCalendarViewShow(boolean shown){
	this.showCalendar = shown;
    }
    
    public void setSpinnersShow(boolean shown){
	this.showSpinners = shown;
    }

    public void setCallBack(OnDateSetListener ondate) {
	ondateSet = ondate;
    }

    private int year, month, day;

    @Override
    public void setArguments(Bundle args) {
	super.setArguments(args);
	year = args.getInt("year");
	month = args.getInt("month");
	day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	DatePickerDialog dp = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
	dp.getDatePicker().setCalendarViewShown(showCalendar);
	dp.getDatePicker().setSpinnersShown(showSpinners);
	return dp;
    }
}
