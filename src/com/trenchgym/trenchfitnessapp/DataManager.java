package com.trenchgym.trenchfitnessapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;

public class DataManager {

    DatabaseHelper dbHelp;
    ParseManager parseManager;

    public DataManager(Context context) {
	dbHelp = new DatabaseHelper(context);
	parseManager = new ParseManager();
    }

    public void getAllCustomersFromServerAndSave() {
	ParseQuery<Customer> query = ParseQuery
		.getQuery(DatabaseHelper.CUSTOMER_TABLE_NAME);

	query.findInBackground(new FindCallback<Customer>() {

	    @Override
	    public void done(List<Customer> customers, ParseException e) {
		// TODO Auto-generated method stub
		if (e == null) {
		    for (final Customer customer : customers) {
			dbHelp.addCustomerWithID(customer);
			// If there is a picutre save it locally
			customer.getAndSaveServerImage();
		    }
		}
	    }
	});
    }
    
    public void saveCustomersFromServer(){
	
    }
    
    public DatabaseHelper getDatabaseHelper(){
	return dbHelp;
    }
    
    public void saveInBackground(ParseObject po){
	parseManager.saveInBackground(po);
    }
    
    public void saveCustomer(Customer customer){
	customer.setCustomerID(dbHelp.addCustomer(customer));
	parseManager.save(customer);
    }
    
    public void updateCustomer(Customer customer){
	parseManager.updateCustomer(customer);
	dbHelp.updateCustomer(customer);
    }
    
    public void deleteCustomer(int CustomerID){
	parseManager.deleteCustomer(CustomerID);
	dbHelp.deleteCustomer(CustomerID);
    }
    
    
    public void saveMembership(Membership membership){
	membership.setMembershipID(dbHelp.addMembership(membership));
	parseManager.save(membership);
    }
    
    public void updateMembership(Membership membership){
	parseManager.updateMembership(membership);
	dbHelp.updateMembership(membership);
    }
    
    public void deleteMembership(int membershipID){
	parseManager.deleteMembership(membershipID);
	dbHelp.deleteMembership(membershipID);
    }
    
    public void saveMembershipType(MembershipType mt){
	mt.setMembershipTypeID(dbHelp.addMembershipType(mt));
	parseManager.save(mt);
    }
    
    public void updateMembershipType(MembershipType mt){
	parseManager.updateMembershipType(mt);
	dbHelp.updateMembershipType(mt);
    }
    
    public void deleteMembershipType(int membershipTypeID){
	parseManager.deleteMembershipType(membershipTypeID);
	dbHelp.deleteMembershipType(membershipTypeID);
    }

}
