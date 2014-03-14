package com.trenchgym.trenchfitnessapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

public class TrenchFitnessApp extends Application {

    public static final String DATE_FORMAT = "yyyyMMdd";
    public static boolean editMode = true;
    public static boolean createCustomer = true;
    public static final String appName = "Trench Fitness App";
    private DataManager dataManager;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
	    DATE_FORMAT);

    public static long formatDateAsLong(Calendar cal) {
	return Long.parseLong(dateFormat.format(cal.getTime()));
    }

    public static Calendar getCalendarFromFormattedLong(long l) {
	try {
	    Calendar c = Calendar.getInstance();
	    c.setTime(dateFormat.parse(String.valueOf(l)));
	    return c;

	} catch (ParseException e) {
	    return null;
	}
    }

    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	ParseObject.registerSubclass(Customer.class);
	ParseObject.registerSubclass(Membership.class);
	ParseObject.registerSubclass(MembershipType.class);
	Parse.initialize(this, "w1x30czaQ2mtUcZuTr68019rSdsuyrqebEHlrO4S",
		"vWNajCbwY2X7zZNAyAoZynjwTb4K0fM90XFesEf3");
	// Setting up the membership types
	SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(this);
	if (!prefs.getBoolean("firstTime", false)) {
	    // Price , Duration in number of months
	    dataManager = new DataManager(this);

	    MembershipType mt1 = new MembershipType();
	    mt1.setDuration(1);
	    mt1.setPrice(25);
	    
	    MembershipType mt2 = new MembershipType();
	    mt2.setDuration(3);
	    mt2.setPrice(65);

	    MembershipType mt3 = new MembershipType();
	    mt3.setDuration(6);
	    mt3.setPrice(120);

	    MembershipType mt4 = new MembershipType();
	    mt4.setDuration(12);
	    mt4.setPrice(200);
	    dataManager.saveMembershipType(mt1);
	    dataManager.saveMembershipType(mt2);
	    dataManager.saveMembershipType(mt3);
	    dataManager.saveMembershipType(mt4);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putBoolean("firstTime", true);
	    editor.commit();
	}
    }

    // Convert from calendar to String
    public static String calendarToString(Calendar c) {
	String date = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "/"
		+ String.valueOf(c.get(Calendar.MONTH) + 1) + "/"
		+ String.valueOf(c.get(Calendar.YEAR));
	return date;
    }

    // Capitalise first letter of a word
    public static String capitaliseFirstLetter(String s) {
	return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    // Capitalise the first letter of each word in a sentence
    public static String capitaliseFirstLetterOfEachWord(String s) {
	// Splits the sentence into its words
	String[] words = s.split(" ");
	String newSentence = null;
	for (String word : words) {
	    if (newSentence != null) {
		newSentence = newSentence + " " + capitaliseFirstLetter(word);
	    } else {
		newSentence = word;
	    }

	}
	return newSentence;
    }
}
