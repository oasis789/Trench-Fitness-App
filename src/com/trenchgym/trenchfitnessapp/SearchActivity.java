package com.trenchgym.trenchfitnessapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener {

    private RadioGroup radioSearchPrefGroup;
    private RadioButton rbID, rblName, rbPostCode;
    private EditText etSearchField;
    private Button bSearch, bClearSearchResults, bGetAllCustomers;
    private ListView lvSearchResults;
    private String searchString;
    private DataManager dataManager;
    private List<Customer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_search);
	dataManager = new DataManager(this);
	referenceViews();
    }

    private void referenceViews() {
	// TODO Auto-generated method stub
	radioSearchPrefGroup = (RadioGroup) findViewById(R.id.rgSearchParameter);
	bSearch = (Button) findViewById(R.id.bSearch);
	bSearch.setOnClickListener(this);
	bClearSearchResults = (Button) findViewById(R.id.bClearResults);
	bClearSearchResults.setOnClickListener(this);
	bGetAllCustomers = (Button) findViewById(R.id.bGetAllCustomers);
	bGetAllCustomers.setOnClickListener(this);
	etSearchField = (EditText) findViewById(R.id.etSearchField);
	lvSearchResults = (ListView) findViewById(R.id.listViewSearchResults);
	registerForContextMenu(lvSearchResults);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
		.getMenuInfo();
	switch (item.getItemId()) {
	case R.id.action_delete:
	    final int position = (int) info.id;
	    final Customer selectedCustomer = list.get(position);
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		    this);

	    // set title
	    alertDialogBuilder.setTitle("Delete Customer?");

	    // set dialog message
	    alertDialogBuilder
		    .setMessage(
			    "Are you sure you wish to delete "
				    + selectedCustomer.getFullName() + ".")
		    .setCancelable(false)
		    .setPositiveButton("Yes",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    dataManager.deleteCustomer(selectedCustomer.getCustomerID());
				    // Delete image file
				    selectedCustomer.deleteImage();
				    list.remove(position);
				    setupListView(list);
				}
			    })
		    .setNegativeButton("No",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {
				    // if this button is clicked, just close
				    // the dialog box and do nothing
				    dialog.cancel();
				}
			    });

	    // create alert dialog
	    AlertDialog alertDialog = alertDialogBuilder.create();

	    // show it
	    alertDialog.show();
	    // do something useful
	    return true;
	default:
	    return super.onContextItemSelected(item);
	}
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	// TODO Auto-generated method stub
	super.onCreateContextMenu(menu, v, menuInfo);
	menu.setHeaderTitle("Options");
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.search_menu, menu);
    }

    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.bSearch:
	    // Null Validation
	    lvSearchResults.setAdapter(null);
	    int selectedID = radioSearchPrefGroup.getCheckedRadioButtonId();
	    String radioSearchPref = DatabaseHelper.CUSTOMER_KEY_ID;
	    switch (selectedID) {
	    case R.id.rID:
		radioSearchPref = DatabaseHelper.CUSTOMER_KEY_ID;
		break;
	    case R.id.rlName:
		radioSearchPref = DatabaseHelper.CUSTOMER_KEY_LAST_NAME;
		break;
	    case R.id.rPostCode:
		radioSearchPref = DatabaseHelper.CUSTOMER_KEY_POST_CODE;
		break;
	    case R.id.rDob:
		radioSearchPref = DatabaseHelper.CUSTOMER_KEY_DOB;
	    }

	    if (etSearchField.getText().toString().trim().length() > 0) {
		etSearchField.setError(null);
		searchString = etSearchField.getText().toString().trim()
			.toLowerCase(Locale.ENGLISH);
		if (radioSearchPref.equals(DatabaseHelper.CUSTOMER_KEY_DOB)) {
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    Date date = null;
		    try {
			date = sdf.parse(searchString);
		    } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    searchString = String.valueOf(TrenchFitnessApp
			    .formatDateAsLong(cal));
		}
		list = dataManager.getDatabaseHelper()
			.getCustomersForSearchResult(radioSearchPref,
				searchString);
		if (list.isEmpty()) {
		    Toast.makeText(this, "No Customer's Found",
			    Toast.LENGTH_SHORT).show();
		} else {
		    setupListView(list);
		}

	    } else {
		etSearchField.setError("Blank Field!");
	    }
	    break;
	case R.id.bClearResults:
	    lvSearchResults.setAdapter(null);
	    break;
	case R.id.bGetAllCustomers:
	    // Hide Keyboard
	    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    inputManager.hideSoftInputFromWindow(getCurrentFocus()
		    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    list = dataManager.getDatabaseHelper()   .getAllCustomersForSearchResult();
	    if (list.isEmpty()) {
		Toast.makeText(this, "No Customer's Found", Toast.LENGTH_SHORT)
			.show();
	    } else {
		setupListView(list);
	    }
	    break;
	}
    }

    public void setupListView(final List<Customer> list) {
	SearchResultsCustomListAdapter customAdapter = new SearchResultsCustomListAdapter(
		this, R.layout.listview_individual_search_result, list);
	lvSearchResults.setAdapter(customAdapter);
	lvSearchResults.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		// TODO Auto-generated method stub
		int selectedID = list.get(position).getCustomerID();
		// Just viewing an existing customer
		TrenchFitnessApp.editMode = false;
		TrenchFitnessApp.createCustomer = false;
		Intent i = new Intent(getApplicationContext(),
			MemberActivity.class);
		i.putExtra(DatabaseHelper.CUSTOMER_KEY_ID, selectedID);
		startActivity(i);
	    }
	});
    }

}
