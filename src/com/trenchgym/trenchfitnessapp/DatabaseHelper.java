package com.trenchgym.trenchfitnessapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "trenchfitness";

    // Customer Table Details
    public static final String CUSTOMER_TABLE_NAME = "customers";
    public static final String CUSTOMER_KEY_ID = "customerid";
    public static final String CUSTOMER_KEY_FIRST_NAME = "firstname";
    public static final String CUSTOMER_KEY_LAST_NAME = "lastname";
    public static final String CUSTOMER_KEY_DOB = "dob";
    public static final String CUSTOMER_KEY_JOIN_DATE = "joindate";
    public static final String CUSTOMER_KEY_ADDRESS = "address";
    public static final String CUSTOMER_KEY_POST_CODE = "postcode";
    public static final String CUSTOMER_KEY_PHONE_NUMBER = "phonenumber";
    public static final String CUSTOMER_KEY_EMERGENCY_NUMBER = "emergencynumber";
    public static final String CUSTOMER_KEY_MEDICAL_CONDITIONS = "medicalconditions";

    // MembershipTypes Table Details
    public static final String MEMBERSHIPTYPES_TABLE_NAME = "membershiptypes";
    public static final String MEMBERSHIPTYPES_KEY_ID = "membershiptypesid";
    public static final String MEMBERSHIPTYPES_KEY_PRICE = "price";
    public static final String MEMBERSHIPTYPES_KEY_DURATION = "duration";

    // Membership Table Details
    public static final String MEMBERSHIP_TABLE_NAME = "membership";
    public static final String MEMBERSHIP_KEY_ID = "membershipid";
    public static final String MEMBERSHIP_KEY_START_DATE = "startdate";

    private TrenchFitnessApp app;

    public DatabaseHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	app = ((TrenchFitnessApp) context.getApplicationContext());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	// TODO Auto-generated method stub

	// Customer Table
	String CREATE_CUSTOMERS_TABLE = "CREATE TABLE " + CUSTOMER_TABLE_NAME
		+ "(" + CUSTOMER_KEY_ID
		+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ CUSTOMER_KEY_FIRST_NAME + " TEXT," + CUSTOMER_KEY_LAST_NAME
		+ " TEXT," + CUSTOMER_KEY_ADDRESS + " TEXT,"
		+ CUSTOMER_KEY_POST_CODE + " TEXT," + CUSTOMER_KEY_DOB
		+ " REAL," + CUSTOMER_KEY_JOIN_DATE + " REAL,"
		+ CUSTOMER_KEY_PHONE_NUMBER + " TEXT,"
		+ CUSTOMER_KEY_EMERGENCY_NUMBER + " TEXT,"
		+ CUSTOMER_KEY_MEDICAL_CONDITIONS + " TEXT" + ")";

	// MembershipTypes Table
	String CREATE_MEMBERSHIPTYPES_TABLE = "CREATE TABLE "
		+ MEMBERSHIPTYPES_TABLE_NAME + "(" + MEMBERSHIPTYPES_KEY_ID
		+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ MEMBERSHIPTYPES_KEY_PRICE + " INTEGER,"
		+ MEMBERSHIPTYPES_KEY_DURATION + " INTEGER" + ")";

	// Membership Table
	String CREATE_MEMBERSHIP_TABLE = "CREATE TABLE "
		+ MEMBERSHIP_TABLE_NAME + "(" + MEMBERSHIP_KEY_ID
		+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
		+ CUSTOMER_KEY_ID + " INTEGER," + MEMBERSHIPTYPES_KEY_ID
		+ " INTEGER," + MEMBERSHIP_KEY_START_DATE + " REAL,"
		+ "FOREIGN KEY (" + CUSTOMER_KEY_ID + ") REFERENCES "
		+ CUSTOMER_TABLE_NAME + " (" + CUSTOMER_KEY_ID + "),"
		+ "FOREIGN KEY (" + MEMBERSHIPTYPES_KEY_ID + ") REFERENCES "
		+ MEMBERSHIPTYPES_TABLE_NAME + " (" + MEMBERSHIPTYPES_KEY_ID
		+ ")" + ")";

	db.execSQL(CREATE_CUSTOMERS_TABLE);
	db.execSQL(CREATE_MEMBERSHIPTYPES_TABLE);
	db.execSQL(CREATE_MEMBERSHIP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// TODO Auto-generated method stub
    }
    
    public void deleteAllData(){
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(CUSTOMER_TABLE_NAME, null, null);
	db.delete(MEMBERSHIPTYPES_TABLE_NAME, null, null);
	db.delete(MEMBERSHIP_TABLE_NAME, null, null);
    }

    // Customers Table CRUD Operations
    public int addCustomer(Customer customer) {
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putCustomerIntoContentValues(customer);
	long rowID = db.insert(CUSTOMER_TABLE_NAME, null, cv);
	db.close();
	return (int) rowID;
    }
    
    public void addCustomerWithID(Customer customer){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putCustomerIntoContentValues(customer);
	cv.put(CUSTOMER_KEY_ID, customer.getCustomerID());
	long rowID = db.insert(CUSTOMER_TABLE_NAME, null, cv);
	db.close();
    }

    public Customer getCustomer(int id) {
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.query(CUSTOMER_TABLE_NAME, null, CUSTOMER_KEY_ID
		+ "=?", new String[] { String.valueOf(id) }, null, null, null,
		null);
	if (cursor != null) {
	    cursor.moveToFirst();
	}
	Customer customer = getCustomerFromCursor(cursor);
	return customer;
    }

    public List<Customer> getCustomersForSearchResult(
	    String searchParam, String searchString) {
	SQLiteDatabase db = this.getReadableDatabase();
	String[] columns = new String[] { CUSTOMER_KEY_ID,
		CUSTOMER_KEY_FIRST_NAME, CUSTOMER_KEY_LAST_NAME,
		CUSTOMER_KEY_ADDRESS, CUSTOMER_KEY_POST_CODE, CUSTOMER_KEY_DOB };
	Cursor cursor = db.query(CUSTOMER_TABLE_NAME, columns, searchParam
		+ "=?", new String[] { searchString }, null, null, null);
	List<Customer> list = new ArrayList<Customer>();
	if (cursor.moveToFirst()) {
	    do {
		Customer customer = getCustomerSearchResultFromCursor(cursor);
		list.add(customer);
	    } while (cursor.moveToNext());
	}
	return list;
    }

    public List<Customer> getAllCustomersForSearchResult() {
	SQLiteDatabase db = this.getReadableDatabase();
	String[] columns = new String[] { CUSTOMER_KEY_ID,
		CUSTOMER_KEY_FIRST_NAME, CUSTOMER_KEY_LAST_NAME,
		CUSTOMER_KEY_ADDRESS, CUSTOMER_KEY_POST_CODE, CUSTOMER_KEY_DOB };
	Cursor cursor = db.query(CUSTOMER_TABLE_NAME, columns, null, null,
		null, null, null);
	List<Customer> list = new ArrayList<Customer>();
	if (cursor.moveToFirst()) {
	    do {
		Customer customer = getCustomerSearchResultFromCursor(cursor);
		list.add(customer);
	    } while (cursor.moveToNext());
	}
	return list;
    }

    public void updateCustomer(final Customer customer) {
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putCustomerIntoContentValues(customer);
	db.update(CUSTOMER_TABLE_NAME, cv, CUSTOMER_KEY_ID + " =?",
		new String[] { String.valueOf(customer.getCustomerID()) });
    }

    public void deleteCustomer(int customerID) {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(CUSTOMER_TABLE_NAME, CUSTOMER_KEY_ID + " =?",
		new String[] { String.valueOf(customerID) });
	db.close();
    }

    public int getCustomerCount() {
	String countQuery = "SELECT  * FROM " + CUSTOMER_TABLE_NAME;
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.rawQuery(countQuery, null);
	cursor.close();
	return cursor.getCount();
    }

    private Customer getCustomerFromCursor(Cursor cursor) {
	Customer customer = new Customer();
	customer.setCustomerID(cursor.getInt(cursor
		.getColumnIndex(CUSTOMER_KEY_ID)));
	customer.setfName(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_FIRST_NAME)));
	customer.setlName(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_LAST_NAME)));
	customer.setDob(TrenchFitnessApp.getCalendarFromFormattedLong(cursor
		.getLong(cursor.getColumnIndex(CUSTOMER_KEY_DOB))));
	customer.setJoinDate(TrenchFitnessApp
		.getCalendarFromFormattedLong(cursor.getLong(cursor
			.getColumnIndex(CUSTOMER_KEY_JOIN_DATE))));
	customer.setPhoneNumber(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_PHONE_NUMBER)));
	customer.setAddress(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_ADDRESS)));
	customer.setEmergencyContactNumber(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_EMERGENCY_NUMBER)));
	customer.setPostCode(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_POST_CODE)));
	customer.setMedicalConditions(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_MEDICAL_CONDITIONS)));
	return customer;
    }
    
    private Customer getCustomerSearchResultFromCursor(Cursor cursor) {
	Customer customer = new Customer();
	customer.setCustomerID(cursor.getInt(cursor
		.getColumnIndex(CUSTOMER_KEY_ID)));
	customer.setfName(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_FIRST_NAME)));
	customer.setlName(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_LAST_NAME)));
	customer.setDob(TrenchFitnessApp.getCalendarFromFormattedLong(cursor
		.getLong(cursor.getColumnIndex(CUSTOMER_KEY_DOB))));
	customer.setAddress(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_ADDRESS)));	
	customer.setPostCode(cursor.getString(cursor
		.getColumnIndex(CUSTOMER_KEY_POST_CODE)));
	return customer;
    }

    private ContentValues putCustomerIntoContentValues(Customer customer) {
	ContentValues cv = new ContentValues();
	cv.put(CUSTOMER_KEY_FIRST_NAME, customer.getfName());
	Log.v("DB", Integer.toString(customer.getCustomerID()));
	cv.put(CUSTOMER_KEY_LAST_NAME, customer.getlName());
	cv.put(CUSTOMER_KEY_ADDRESS, customer.getAddress());
	cv.put(CUSTOMER_KEY_POST_CODE, customer.getPostCode());
	cv.put(CUSTOMER_KEY_DOB,
		TrenchFitnessApp.formatDateAsLong(customer.getDob()));
	cv.put(CUSTOMER_KEY_JOIN_DATE,
		TrenchFitnessApp.formatDateAsLong(customer.getJoinDate()));
	cv.put(CUSTOMER_KEY_PHONE_NUMBER, customer.getPhoneNumber());
	cv.put(CUSTOMER_KEY_EMERGENCY_NUMBER,
		customer.getEmergencyContactNumber());
	cv.put(CUSTOMER_KEY_MEDICAL_CONDITIONS, customer.getMedicalConditions());
	return cv;
    }

    // MembershipTypes Table CRUD Operations
    public int addMembershipType(MembershipType mt) {
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putMembershipTypeIntoContentValues(mt);
	long rowID = db.insert(MEMBERSHIPTYPES_TABLE_NAME, null, cv);
	db.close();
	return (int) rowID;
    }
    
    public void addMembershipTypeWithID(MembershipType mt){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putMembershipTypeIntoContentValues(mt);
	cv.put(MEMBERSHIPTYPES_KEY_ID, mt.getMembershipTypeID());
	long rowID = db.insert(MEMBERSHIPTYPES_TABLE_NAME, null, cv);
	db.close();
    }

    public MembershipType getMembershipType(int id) {
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.query(MEMBERSHIPTYPES_TABLE_NAME, null,
		MEMBERSHIPTYPES_KEY_ID + "=?",
		new String[] { String.valueOf(id) }, null, null, null, null);
	cursor.moveToFirst();
	MembershipType mt = getMembershipTypeFromCursor(cursor);
	return mt;
    }

    public List<MembershipType> getAllMembershipTypes() {
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.query(MEMBERSHIPTYPES_TABLE_NAME, null, null, null,
		null, null, null);
	List<MembershipType> mtList = new ArrayList<MembershipType>();
	if (cursor.moveToFirst()) {
	    do {
		MembershipType mt = getMembershipTypeFromCursor(cursor);
		mtList.add(mt);
	    } while (cursor.moveToNext());
	}
	return mtList;
    }

    public void updateMembershipType(final MembershipType mt) {
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putMembershipTypeIntoContentValues(mt);
	//Update locally
	db.update(MEMBERSHIPTYPES_TABLE_NAME, cv, MEMBERSHIPTYPES_KEY_ID
		+ " =?",
		new String[] { String.valueOf(mt.getMembershipTypeID()) });
    }

    public void deleteMembershipType(int membershipTypeID) {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(MEMBERSHIPTYPES_TABLE_NAME, MEMBERSHIPTYPES_KEY_ID + " =?",
		new String[] { String.valueOf(membershipTypeID) });
	db.close();
	
    }

    private MembershipType getMembershipTypeFromCursor(Cursor cursor) {
	// TODO Auto-generated method stub
	MembershipType mt = new MembershipType();
	mt.setMembershipTypeID(cursor.getInt(cursor
		.getColumnIndex(MEMBERSHIPTYPES_KEY_ID)));
	mt.setPrice(cursor.getInt(cursor
		.getColumnIndex(MEMBERSHIPTYPES_KEY_PRICE)));
	mt.setDuration(cursor.getInt(cursor
		.getColumnIndex(MEMBERSHIPTYPES_KEY_DURATION)));
	return mt;
    }

    private ContentValues putMembershipTypeIntoContentValues(MembershipType mt) {
	// TODO Auto-generated method stub
	ContentValues cv = new ContentValues();
	cv.put(MEMBERSHIPTYPES_KEY_PRICE, mt.getPrice());
	cv.put(MEMBERSHIPTYPES_KEY_DURATION, mt.getDuration());
	return cv;
    }

    // Membership Table CRUD Operations
    public int addMembership(Membership membership) {
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putMembershipIntoContentValues(membership);
	long rowID = db.insert(MEMBERSHIP_TABLE_NAME, null, cv);
	db.close();
	return (int) rowID;
    }
    
    public void addMembershipWithID(Membership membership){
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putMembershipIntoContentValues(membership);
	cv.put(MEMBERSHIP_KEY_ID, membership.getMembershipID());
	long rowID = db.insert(MEMBERSHIP_TABLE_NAME, null, cv);
	db.close();
    }

    public Membership getMembership(int id) {
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.query(MEMBERSHIP_TABLE_NAME, null, MEMBERSHIP_KEY_ID
		+ "=?", new String[] { String.valueOf(id) }, null, null, null,
		null);
	cursor.moveToFirst();
	Membership membership = getMembershipFromCursor(cursor);
	return membership;
    }

    public List<Membership> getAllMembershipsForACustomer(int customerID) {
	List<Membership> mList = new ArrayList<Membership>();
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.query(MEMBERSHIP_TABLE_NAME, null, CUSTOMER_KEY_ID
		+ "=?", new String[] { String.valueOf(customerID) }, null,
		null, MEMBERSHIP_KEY_START_DATE + " DESC");

	if (cursor.moveToFirst()) {
	    do {
		Membership m = getMembershipFromCursor(cursor);
		mList.add(m);
	    } while (cursor.moveToNext());
	}
	return mList;
    }

    public int updateMembership(final Membership membership) {
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues cv = putMembershipIntoContentValues(membership);
	return db.update(MEMBERSHIP_TABLE_NAME, cv, MEMBERSHIP_KEY_ID + " = ?",
		new String[] { String.valueOf(membership.getMembershipID()) });
    }

    public void deleteMembership(int membershipID) {
	SQLiteDatabase db = this.getWritableDatabase();
	db.delete(MEMBERSHIP_TABLE_NAME, MEMBERSHIP_KEY_ID + " = ?",
		new String[] { String.valueOf(membershipID) });
	db.close();
    }

    public int getMembershipCount() {
	String countQuery = "SELECT  * FROM " + MEMBERSHIP_TABLE_NAME;
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor cursor = db.rawQuery(countQuery, null);
	cursor.close();
	// return count
	return cursor.getCount();
    }

    private Membership getMembershipFromCursor(Cursor cursor) {
	Membership m = new Membership();
	m.setMembershipID(cursor.getInt(cursor
		.getColumnIndex(MEMBERSHIP_KEY_ID)));
	m.setCustomerID(cursor.getInt(cursor.getColumnIndex(CUSTOMER_KEY_ID)));
	m.setMembershipTypeID(cursor.getInt(cursor
		.getColumnIndex(MEMBERSHIPTYPES_KEY_ID)));
	m.setMembershipStartDate(TrenchFitnessApp
		.getCalendarFromFormattedLong(cursor.getLong(cursor
			.getColumnIndex(MEMBERSHIP_KEY_START_DATE))));
	return m;
    }

    private ContentValues putMembershipIntoContentValues(Membership membership) {
	ContentValues cv = new ContentValues();
	cv.put(CUSTOMER_KEY_ID, membership.getCustomerID());
	cv.put(MEMBERSHIPTYPES_KEY_ID, membership.getMembershipTypeID());
	cv.put(MEMBERSHIP_KEY_START_DATE, TrenchFitnessApp
		.formatDateAsLong(membership.getMembershipStartDate()));
	return cv;
    }

}
