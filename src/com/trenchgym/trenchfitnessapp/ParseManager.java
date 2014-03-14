package com.trenchgym.trenchfitnessapp;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ParseManager {


    public ParseManager() {
    }
    
    public void save(ParseObject po){
	po.saveInBackground();
    }
    
    public void saveInBackground(ParseObject po){
	po.saveInBackground();
    }

 
    public void updateCustomer(final Customer customer) {
	ParseQuery<Customer> query = ParseQuery
		.getQuery(DatabaseHelper.CUSTOMER_TABLE_NAME);
	query.whereEqualTo(DatabaseHelper.CUSTOMER_KEY_ID,
		customer.getCustomerID());
	query.getFirstInBackground(new GetCallback<Customer>() {

	    @Override
	    public void done(Customer oldCustomer, ParseException e) {
		// TODO Auto-generated method stub
		if (e == null) {
		    oldCustomer.deleteEventually();
		    customer.saveEventually();
		}
	    }

	});
    }

    public void deleteCustomer(int customerID) {
	ParseQuery<Customer> query = ParseQuery
		.getQuery(DatabaseHelper.CUSTOMER_TABLE_NAME);
	query.whereEqualTo(DatabaseHelper.CUSTOMER_KEY_ID, customerID);
	query.getFirstInBackground(new GetCallback<Customer>() {

	    @Override
	    public void done(Customer oldCustomer, ParseException e) {
		// TODO Auto-generated method stub
		if (e == null) {
		    oldCustomer.deleteEventually();
		}
	    }

	});
    }

    public void updateMembership(final Membership membership) {
	ParseQuery<Membership> query = ParseQuery
		.getQuery(DatabaseHelper.MEMBERSHIP_TABLE_NAME);
	query.whereEqualTo(DatabaseHelper.MEMBERSHIP_KEY_ID,
		membership.getMembershipID());
	query.getFirstInBackground(new GetCallback<Membership>() {

	    @Override
	    public void done(Membership oldMembership, ParseException e) {
		// TODO Auto-generated method stub
		if (e == null) {
		    oldMembership.deleteEventually();
		    membership.saveEventually();
		}
	    }

	});
    }

    public void deleteMembership(int membershipID) {
	ParseQuery<Membership> query = ParseQuery
		.getQuery(DatabaseHelper.MEMBERSHIP_TABLE_NAME);
	query.whereEqualTo(DatabaseHelper.MEMBERSHIP_KEY_ID, membershipID);
	query.getFirstInBackground(new GetCallback<Membership>() {

	    @Override
	    public void done(Membership oldMembership, ParseException e) {
		// TODO Auto-generated method stub
		if (e == null) {
		    oldMembership.deleteEventually();
		}
	    }

	});
    }

    public void updateMembershipType(final MembershipType mt) {
	// Update on parse server
	ParseQuery<MembershipType> query = ParseQuery
		.getQuery(DatabaseHelper.MEMBERSHIPTYPES_TABLE_NAME);
	query.whereEqualTo(DatabaseHelper.MEMBERSHIPTYPES_KEY_ID,
		mt.getMembershipTypeID());
	query.getFirstInBackground(new GetCallback<MembershipType>() {

	    @Override
	    public void done(MembershipType oldMt, ParseException e) {
		// TODO Auto-generated method stub
		if (e == null) {
		    oldMt.deleteEventually();
		    mt.saveEventually();
		}
	    }

	});
    }

    public void deleteMembershipType(int membershipTypeID) {
	ParseQuery<MembershipType> query = ParseQuery
		.getQuery(DatabaseHelper.MEMBERSHIPTYPES_TABLE_NAME);
	query.whereEqualTo(DatabaseHelper.MEMBERSHIPTYPES_KEY_ID,
		membershipTypeID);
	query.getFirstInBackground(new GetCallback<MembershipType>() {

	    @Override
	    public void done(MembershipType oldMt, ParseException e) {
		// TODO Auto-generated method stub
		if (e == null) {
		    oldMt.deleteEventually();
		}
	    }
	});
    }

}
