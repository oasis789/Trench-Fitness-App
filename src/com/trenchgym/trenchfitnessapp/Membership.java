package com.trenchgym.trenchfitnessapp;

import java.util.Calendar;

import com.parse.*;

@ParseClassName(DatabaseHelper.MEMBERSHIP_TABLE_NAME)
public class Membership extends ParseObject {

    public Membership() {
	// TODO Auto-generated constructor stub
    }

    // Getters
    public int getMembershipID() {
	// return membershipID;
	return getInt(DatabaseHelper.MEMBERSHIP_KEY_ID);
    }

    public int getCustomerID() {
	// return customerID;
	return getInt(DatabaseHelper.CUSTOMER_KEY_ID);
    }

    public int getMembershipTypeID() {
	// return membershipTypeID;
	return getInt(DatabaseHelper.MEMBERSHIPTYPES_KEY_ID);
    }

    public Calendar getMembershipStartDate() {
	// return membershipStartDate;
	return TrenchFitnessApp
		.getCalendarFromFormattedLong(getLong(DatabaseHelper.MEMBERSHIP_KEY_START_DATE));
    }

    // Setters
    public void setMembershipID(int membershipID) {
	// this.membershipID = membershipID;
	put(DatabaseHelper.MEMBERSHIP_KEY_ID, membershipID);
    }

    public void setCustomerID(int customerID) {
	// this.customerID = customerID;
	put(DatabaseHelper.CUSTOMER_KEY_ID, customerID);
    }

    public void setMembershipTypeID(int membershipTypeID) {
	// this.membershipTypeID = membershipTypeID;
	put(DatabaseHelper.MEMBERSHIPTYPES_KEY_ID, membershipTypeID);
    }

    public void setMembershipStartDate(Calendar membershipStartDate) {
	// this.membershipStartDate = membershipStartDate;
	put(DatabaseHelper.MEMBERSHIP_KEY_START_DATE,
		TrenchFitnessApp.formatDateAsLong(membershipStartDate));
    }
    
}
