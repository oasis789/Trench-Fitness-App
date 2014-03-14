package com.trenchgym.trenchfitnessapp;

import com.parse.*;

@ParseClassName(DatabaseHelper.MEMBERSHIPTYPES_TABLE_NAME)
public class MembershipType extends ParseObject {

    /*
     * public int membershipTypeID; public int price; public int duration; //In
     * number of months
     * 
     * public MembershipType(int membershipTypeID, int price, int duration) {
     * this.membershipTypeID = membershipTypeID; this.price = price;
     * this.duration = duration; }
     * 
     * public MembershipType(int price, int duration){ this.price = price;
     * this.duration = duration; }
     */

    public MembershipType() {
    }

    // Getters
    public int getMembershipTypeID() {
	// return membershipTypeID;
	return getInt(DatabaseHelper.MEMBERSHIPTYPES_KEY_ID);
    }

    public int getPrice() {
	// return price;
	return getInt(DatabaseHelper.MEMBERSHIPTYPES_KEY_PRICE);
    }

    public int getDuration() {
	// return duration;
	return getInt(DatabaseHelper.MEMBERSHIPTYPES_KEY_DURATION);
    }

    // Setters
    public void setMembershipTypeID(int membershipTypeID) {
	// this.membershipTypeID = membershipTypeID;
	put(DatabaseHelper.MEMBERSHIPTYPES_KEY_ID, membershipTypeID);
    }

    public void setPrice(int price) {
	// this.price = price;
	put(DatabaseHelper.MEMBERSHIPTYPES_KEY_PRICE, price);
    }

    public void setDuration(int duration) {
	// this.duration = duration;
	put(DatabaseHelper.MEMBERSHIPTYPES_KEY_DURATION, duration);
    }

    // Text to be displayed in the spinner
    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return String.valueOf(getDuration()) + " Months";
    }

    

}
