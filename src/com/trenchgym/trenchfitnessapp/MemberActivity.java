package com.trenchgym.trenchfitnessapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MemberActivity extends FragmentActivity implements OnClickListener {

    private TextView tvCustomerHeader, tvMembershipHeader;
    private ImageView ivMemberPicture;
    private EditText etfName, etlName, etPhoneNumber, etEmergencyNumber,
	    etAddress, etPostCode, etMedicalConditions;
    private EditText[] etArray;
    private Button bSaveMember, bCancelMember, bSetDob, bAddMembership,
	    bSetMembershipStartDate;
    private Spinner setMembershipDuration;
    private TableLayout tblMembership;
    private TableRow trHeading;
    private LinearLayout llMembershipSetup;
    private ScrollView svMembership;
    private boolean validCustomer, pictureTaken;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private Calendar calendarDob, calendarStartDate;
    private List<Membership> membershipList;
    private int customerID;
    private List<MembershipType> membershipTypes;
    private DataManager dataManager;

    public class NullVaidator implements TextWatcher {
	private EditText et;

	private NullVaidator(EditText et) {
	    this.et = et;
	}

	@Override
	public void afterTextChanged(Editable s) {
	    // TODO Auto-generated method stub
	    String value = s.toString();
	    if (value.trim().length() > 0) {
		et.setError(null);
		validCustomer = true;
	    } else {
		et.setError("Blank Field");
		validCustomer = false;
		return;
	    }
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
		int after) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
		int count) {
	    // TODO Auto-generated method stub

	}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_member);
	// For Validation
	validCustomer = true;
	// validMembership = true;
	// Checks if picture is taken for saving
	pictureTaken = false;
	dataManager = new DataManager(this); 
	referenceAllViews();
	// Setup Spinner
	membershipTypes = dataManager.getDatabaseHelper().getAllMembershipTypes();
	ArrayAdapter<MembershipType> adapter = new ArrayAdapter<MembershipType>(
		this, android.R.layout.simple_spinner_item, membershipTypes);
	setMembershipDuration.setAdapter(adapter);

	if (!TrenchFitnessApp.editMode) {
	    // Hide keyboard
	    this.getWindow().setSoftInputMode(
		    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    customerID = getIntent().getExtras().getInt(
		    DatabaseHelper.CUSTOMER_KEY_ID);
	    Customer customer = dataManager.getDatabaseHelper().getCustomer(customerID);
	    // Load Picture from storage using customer id
	    Bitmap bmp = customer.getLocalImageFile();
	    if (bmp != null) {
		ivMemberPicture.setImageBitmap(bmp);
	    } else {
		ivMemberPicture.setImageDrawable(getResources().getDrawable(
			R.drawable.ic_launcher));
	    }
	    tvCustomerHeader.setText("Customer ID: #"
		    + customer.getCustomerID());
	    changeUI(TrenchFitnessApp.editMode);
	    etfName.setText(TrenchFitnessApp.capitaliseFirstLetter(customer
		    .getfName()));
	    etlName.setText(TrenchFitnessApp.capitaliseFirstLetter(customer
		    .getlName()));
	    calendarDob = customer.getDob();
	    bSetDob.setText(TrenchFitnessApp.calendarToString(calendarDob));
	    etPhoneNumber.setText(customer.getPhoneNumber());
	    etEmergencyNumber.setText(customer.getEmergencyContactNumber());
	    etAddress.setText(TrenchFitnessApp
		    .capitaliseFirstLetterOfEachWord(customer.getAddress()));
	    etPostCode.setText(customer.getPostCode().toUpperCase(
		    Locale.ENGLISH));
	    etMedicalConditions.setText(TrenchFitnessApp
		    .capitaliseFirstLetter(customer.getMedicalConditions()));
	    setupMembershipTable(customer.getCustomerID());
	}
    }

    private void setupMembershipTable(int customerID) {
	// TODO Auto-generated method stub
	membershipList = dataManager.getDatabaseHelper().getAllMembershipsForACustomer(customerID);
	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	for (Membership membership : membershipList) {
	    // TODO: Highlight in Red if expired, in green if currently active
	    View newMembershipRow = inflater.inflate(
		    R.layout.membership_tablerow, null);
	    TextView mNo = (TextView) newMembershipRow
		    .findViewById(R.id.tvMembershipID);
	    mNo.setText(String.valueOf(membership.getMembershipID()));
	    TextView mStartDate = (TextView) newMembershipRow
		    .findViewById(R.id.tvMembershipStartDate);
	    mStartDate.setText(TrenchFitnessApp.calendarToString(membership
		    .getMembershipStartDate()));
	    MembershipType mt = dataManager.getDatabaseHelper().getMembershipType(membership
		    .getMembershipTypeID());
	    TextView mEndDate = (TextView) newMembershipRow
		    .findViewById(R.id.tvMembershipEndDate);
	    Calendar endDate = membership.getMembershipStartDate();
	    endDate.add(Calendar.MONTH, mt.getDuration());
	    mEndDate.setText(TrenchFitnessApp.calendarToString(endDate));
	    TextView mDuration = (TextView) newMembershipRow
		    .findViewById(R.id.tvMembershipDuration);
	    mDuration.setText(String.valueOf(mt.getDuration()) + " Months");
	    if (endDate.before(Calendar.getInstance())) {
		newMembershipRow.setBackgroundColor(Color.RED);
	    } else {
		newMembershipRow.setBackgroundColor(Color.GREEN);
		// If there is less than a week before the membership expires
		if (endDate.getTimeInMillis()
			- Calendar.getInstance().getTimeInMillis() < DateUtils.WEEK_IN_MILLIS) {
		    newMembershipRow.setBackgroundColor(Color.rgb(255, 128, 0));
		    // Make a Dialog to Alert the user
		    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			    this);

		    // set title
		    alertDialogBuilder.setTitle("Membership Expiring!");

		    // set dialog message
		    alertDialogBuilder
			    .setMessage(
				    etfName.getText().toString()
					    + " "
					    + etlName.getText().toString()
					    + "'s membership is expiring. Please renew his membership. ")
			    .setCancelable(false)
			    .setPositiveButton("OK",
				    new DialogInterface.OnClickListener() {
					public void onClick(
						DialogInterface dialog, int id) {
					    dialog.dismiss();
					}
				    });
		    // create alert dialog
		    AlertDialog alertDialog = alertDialogBuilder.create();
		    // show it
		    alertDialog.show();
		}
	    }

	    tblMembership.addView(newMembershipRow);
	}

    }

    private void referenceAllViews() {
	// TODO Auto-generated method stub
	tblMembership = (TableLayout) findViewById(R.id.tlMembership);
	trHeading = (TableRow) findViewById(R.id.trHeading);
	llMembershipSetup = (LinearLayout) findViewById(R.id.llMembershipSetup);
	svMembership = (ScrollView) findViewById(R.id.svMembership);
	if (TrenchFitnessApp.createCustomer) {
	    svMembership.setVisibility(View.INVISIBLE);
	}
	tvCustomerHeader = (TextView) findViewById(R.id.tvCustomerHeader);
	tvMembershipHeader = (TextView) findViewById(R.id.tvMembershipHeader);
	ivMemberPicture = (ImageView) findViewById(R.id.ivMemberPicture);
	ivMemberPicture.setOnClickListener(this);

	etfName = (EditText) findViewById(R.id.etfName);
	etfName.addTextChangedListener(new NullVaidator(etfName));
	etlName = (EditText) findViewById(R.id.etlName);
	etlName.addTextChangedListener(new NullVaidator(etlName));
	etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
	etPhoneNumber.addTextChangedListener(new NullVaidator(etPhoneNumber));
	etEmergencyNumber = (EditText) findViewById(R.id.etEmergencyNumber);
	etEmergencyNumber.addTextChangedListener(new NullVaidator(
		etEmergencyNumber));
	etAddress = (EditText) findViewById(R.id.etAddress);
	etAddress.addTextChangedListener(new NullVaidator(etAddress));
	etPostCode = (EditText) findViewById(R.id.etPostCode);
	etPostCode.addTextChangedListener(new NullVaidator(etPostCode));
	etMedicalConditions = (EditText) findViewById(R.id.etMedicalConditions);
	etMedicalConditions.addTextChangedListener(new NullVaidator(
		etMedicalConditions));
	etArray = new EditText[] { etfName, etlName, etPhoneNumber,
		etEmergencyNumber, etAddress, etPostCode, etMedicalConditions };

	bSaveMember = (Button) findViewById(R.id.bSaveMember);
	bSaveMember.setOnClickListener(this);
	bCancelMember = (Button) findViewById(R.id.bCancelMember);
	bCancelMember.setOnClickListener(this);
	bSetDob = (Button) findViewById(R.id.bSetDob);
	bSetDob.setOnClickListener(this);
	bAddMembership = (Button) findViewById(R.id.bAddMembership);
	bAddMembership.setOnClickListener(this);
	bSetMembershipStartDate = (Button) findViewById(R.id.bSetStartDate);
	bSetMembershipStartDate.setOnClickListener(this);
	setMembershipDuration = (Spinner) findViewById(R.id.sSetDuration);

    }

    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.ivMemberPicture:
	    Intent cameraIntent = new Intent(
		    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	    break;
	case R.id.bSaveMember:
	    if (TrenchFitnessApp.editMode) {
		ValidateEditTextFields();
		if (validCustomer) {
		    //Get data from edit texts
		    Customer customer = new Customer();
		    customer.setfName(etfName.getText().toString().trim()
			    .toLowerCase());
		    customer.setlName(etlName.getText().toString().trim()
			    .toLowerCase());
		    customer.setDob(calendarDob);
		    customer.setJoinDate(Calendar.getInstance());
		    customer.setPhoneNumber(etPhoneNumber.getText().toString()
			    .trim().toLowerCase());
		    customer.setEmergencyContactNumber(etEmergencyNumber
			    .getText().toString().trim().toLowerCase());
		    customer.setAddress(etAddress.getText().toString().trim()
			    .toLowerCase());
		    customer.setPostCode(etPostCode.getText().toString().trim()
			    .toLowerCase());
		    customer.setMedicalConditions(etMedicalConditions.getText()
			    .toString().trim().toLowerCase());
		    // If creating a new customer
		    if (TrenchFitnessApp.createCustomer) {
			TrenchFitnessApp.createCustomer = false;
			dataManager.saveCustomer(customer);
			customerID = customer.getCustomerID();
			tvCustomerHeader.setText("Customer ID: # "
				+ customer.getCustomerID());
			Toast.makeText(this,
				"New member has been successfully created",
				Toast.LENGTH_SHORT).show();
		    } else {
			// If updating a customer
			customer.setCustomerID(customerID);
			dataManager.updateCustomer(customer);
			Toast.makeText(this, "Member successfully updated",
				Toast.LENGTH_SHORT).show();
		    }

		    if (pictureTaken) {
			pictureTaken = false;
			Bitmap bmp = ((BitmapDrawable)ivMemberPicture.getDrawable()).getBitmap();
			customer.saveImageLocally(bmp);
			customer.saveImageOnServer(bmp);
			}

		    svMembership.setVisibility(View.VISIBLE);
		    TrenchFitnessApp.editMode = false;
		    changeUI(TrenchFitnessApp.editMode);

		} else {
		    // If not valid customer
		    Toast.makeText(
			    this,
			    "Please ensure all fields have been entered correctly",
			    Toast.LENGTH_SHORT).show();
		}
	    } else {
		// If the edit button is pressed
		TrenchFitnessApp.editMode = true;
		changeUI(TrenchFitnessApp.editMode);
		// Show Keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	    }
	    break;

	case R.id.bCancelMember:
	    if (TrenchFitnessApp.createCustomer) {
		finish();
	    } else {
		TrenchFitnessApp.editMode = false;
		changeUI(TrenchFitnessApp.editMode);
	    }
	    break;

	case R.id.bSetDob:
	    if (TrenchFitnessApp.createCustomer) {
		// Show the date picker with the spinners but without the
		// calendar
		final Calendar c = Calendar.getInstance();
		showDatePicker(c, onDobSet, true, false);
	    } else {
		showDatePicker(calendarDob, onDobSet, true, false);
	    }
	    break;
	case R.id.bAddMembership:
	    // Should only be allowed to add new membership if there is no
	    // active membership for the customer
	    if (calendarStartDate == null) {
		calendarStartDate = Calendar.getInstance();
	    }

	    // Find the end date of the active membership
	    // And if calendarStartDate is after it and is in the future
	    // then save membership, if not don't allow to save
	    /*
	     * if (calendarStartDate.before(Calendar.getInstance())) {
	     * validMembership = false; }else{ validMembership = true; }
	     * 
	     * if (calendarStartDate.equals(Calendar.getInstance())) {
	     * validMembership = true; }
	     */
	    membershipList = dataManager.getDatabaseHelper().getAllMembershipsForACustomer(customerID);
	    if (membershipList.isEmpty()) {
		saveMembership();
	    } else {
		Membership m = membershipList.get(0);
		MembershipType mt = dataManager.getDatabaseHelper().getMembershipType(m
			.getMembershipTypeID());
		Calendar endDate = m.getMembershipStartDate();
		endDate.add(Calendar.MONTH, mt.getDuration());
		if (calendarStartDate.after(endDate)) {
		    saveMembership();
		} else {
		    // Invalid Start Date
		    Toast.makeText(
			    getApplicationContext(),
			    "The start date must be after the current membership expires",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	    break;
	case R.id.bSetStartDate:
	    // Show Date Picker with just the Calendar
	    final Calendar c = Calendar.getInstance();
	    showDatePicker(c, onStartDateSet, false, true);
	    break;
	}

    }

    private void saveMembership() {
	Membership m = new Membership();
	m.setCustomerID(customerID);
	m.setMembershipStartDate(calendarStartDate);
	m.setMembershipTypeID(membershipTypes.get(
		setMembershipDuration.getSelectedItemPosition())
		.getMembershipTypeID());
	dataManager.saveMembership(m);
	Toast.makeText(this, "Membership Saved Successfully",
		Toast.LENGTH_SHORT).show();
	tblMembership.removeAllViews();
	tblMembership.addView(trHeading);
	setupMembershipTable(customerID);
    }

    private void changeUI(boolean editMode) {
	if (!editMode) {
	    for (int i = 0; i < etArray.length; i++) {
		etArray[i].setEnabled(false);
	    }
	    bSaveMember.setText("Edit");
	    bSetDob.setEnabled(false);
	    ivMemberPicture.setClickable(false);
	    bCancelMember.setVisibility(View.INVISIBLE);
	} else {
	    // Set up the screen so the fields can be altered
	    for (int i = 0; i < etArray.length; i++) {
		etArray[i].setEnabled(true);
	    }
	    bSaveMember.setText("Save");
	    bSetDob.setEnabled(true);
	    ivMemberPicture.setClickable(true);
	    bCancelMember.setVisibility(View.VISIBLE);
	}
    }

    private void showDatePicker(Calendar c, OnDateSetListener onSet,
	    boolean showSpinners, boolean showCalendar) {
	// TODO Auto-generated method stub
	DatePickerFragment date = new DatePickerFragment();
	date.setCalendarViewShow(showCalendar);
	date.setSpinnersShow(showSpinners);
	Bundle args = new Bundle();
	args.putInt("year", c.get(Calendar.YEAR));
	args.putInt("month", c.get(Calendar.MONTH));
	args.putInt("day", c.get(Calendar.DAY_OF_MONTH));
	date.setArguments(args);
	date.setCallBack(onSet);
	date.show(getSupportFragmentManager(), "Date Picker");
    }

    private OnDateSetListener onDobSet = new OnDateSetListener() {

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
		int dayOfMonth) {
	    calendarDob = Calendar.getInstance();
	    calendarDob.set(Calendar.YEAR, year);
	    calendarDob.set(Calendar.MONTH, monthOfYear);
	    calendarDob.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	    bSetDob.setText(TrenchFitnessApp.calendarToString(calendarDob));
	}
    };

    private OnDateSetListener onStartDateSet = new OnDateSetListener() {

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
		int dayOfMonth) {
	    calendarStartDate = Calendar.getInstance();
	    calendarStartDate.set(Calendar.YEAR, year);
	    calendarStartDate.set(Calendar.MONTH, monthOfYear);
	    calendarStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	    bSetMembershipStartDate.setText(TrenchFitnessApp
		    .calendarToString(calendarStartDate));
	}
    };

    private void ValidateEditTextFields() {
	// TODO Auto-generated method stub
	for (int i = 0; i < etArray.length; i++) {
	    if (etArray[i].getText().toString().trim().length() <= 0) {
		validCustomer = false;
		etArray[i].setError("Blank Field");
	    }
	}

	if (etPhoneNumber.getText().length() != 11) {
	    validCustomer = false;
	    etPhoneNumber.setError("Invalid Phone Number");
	}

	if (etEmergencyNumber.getText().length() != 11) {
	    validCustomer = false;
	    etEmergencyNumber.setError("Invalid Phone Number");
	}

	if (calendarDob == null) {
	    validCustomer = false;
	}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == CAMERA_PIC_REQUEST
		&& resultCode == Activity.RESULT_OK) {
	    Bitmap picture = (Bitmap) data.getExtras().get("data");
	    ivMemberPicture.setImageBitmap(picture);
	    pictureTaken = true;
	}
    }

}
