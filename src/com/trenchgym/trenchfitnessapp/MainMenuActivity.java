package com.trenchgym.trenchfitnessapp;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends FragmentActivity implements
	OnClickListener {

    private Button addMember;
    private Button searchForMember;
    private Context context;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main_menu);
	context = this;
	dataManager = new DataManager(this);
	addMember = (Button) findViewById(R.id.bAddMember);
	addMember.setOnClickListener(this);
	searchForMember = (Button) findViewById(R.id.bSearchMember);
	searchForMember.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main_menu, menu);
	return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case R.id.action_download:
	    // Show pop-up confirming overwrite of data
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		    this);

	    // set title
	    alertDialogBuilder.setTitle("Download data from server");

	    // set dialog message
	    alertDialogBuilder
		    .setMessage(
			    "This action will overwrite all eixisting data with data from the server ")
		    .setCancelable(false)
		    .setPositiveButton("Yes",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
					int id) {

				    DownloadCustomersTask cTask = new DownloadCustomersTask();
				    cTask.execute();

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
	    break;
	}
	return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.bAddMember:
	    Intent addMember = new Intent(this, MemberActivity.class);
	    // Creating a new customer in edit mode
	    TrenchFitnessApp.editMode = true;
	    TrenchFitnessApp.createCustomer = true;
	    startActivity(addMember);
	    break;
	case R.id.bSearchMember:
	    Intent search = new Intent(this, SearchActivity.class);
	    startActivity(search);
	    break;
	}

    }

    private class DownloadCustomersTask extends AsyncTask<Void, Integer, Void> {

	ProgressDialogFragment progressDialog;

	@Override
	protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    progressDialog = new ProgressDialogFragment();
	    progressDialog.show(getSupportFragmentManager(), "ProgressDialog");
	    dataManager.getDatabaseHelper().deleteAllData();
	}

	@Override
	protected Void doInBackground(Void... params) {
	    // TODO Auto-generated method stub

	    final ParseQuery<Customer> customerQuery = ParseQuery
		    .getQuery(DatabaseHelper.CUSTOMER_TABLE_NAME);
	    customerQuery.findInBackground(new FindCallback<Customer>() {

		@Override
		public void done(List<Customer> customers, ParseException e) {
		    if (e == null) {
			int total = customers.size();
			int count = 1;
			for (final Customer customer : customers) {
			    if (customer.getfName() != null) {
				dataManager.getDatabaseHelper()
					.addCustomerWithID(customer);
				customer.getAndSaveServerImage();
				publishProgress((count / total) * 100);
				count++;
			    }
			}
		    }
		}
	    });

	    return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    // TODO Auto-generated method stub
	    progressDialog.setProgress(values[0]);

	}

	@Override
	protected void onPostExecute(Void result) {
	    // TODO Auto-generated method stub
	    progressDialog.dismiss();
	    DownloadMembershipsTask mTask = new DownloadMembershipsTask();
	    mTask.execute();
	}

    }

    private class DownloadMembershipsTask extends
	    AsyncTask<Void, Integer, Void> {

	ProgressDialogFragment progressDialog;

	@Override
	protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    progressDialog = new ProgressDialogFragment();
	    progressDialog.show(getSupportFragmentManager(), "x");
	}

	@Override
	protected Void doInBackground(Void... params) {
	    // TODO Auto-generated method stub

	    ParseQuery<Membership> query = ParseQuery
		    .getQuery(DatabaseHelper.MEMBERSHIP_TABLE_NAME);
	    query.findInBackground(new FindCallback<Membership>() {

		@Override
		public void done(List<Membership> memberships, ParseException e) {
		    if (e == null) {
			int total = memberships.size();
			int count = 1;
			for (Membership membership : memberships) {
			    dataManager.getDatabaseHelper()
				    .addMembershipWithID(membership);
			    publishProgress((count / total) * 100);
			    count++;
			}
		    }
		}
	    });

	    return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    // TODO Auto-generated method stub
	    progressDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
	    // TODO Auto-generated method stub
	    progressDialog.dismiss();
	    DownloadMembershipTypesTask mtTask = new DownloadMembershipTypesTask();
	    mtTask.execute();

	}

    }

    private class DownloadMembershipTypesTask extends
	    AsyncTask<Void, Integer, Void> {

	ProgressDialogFragment progressDialog;

	@Override
	protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    progressDialog = new ProgressDialogFragment();
	    progressDialog.show(getSupportFragmentManager(), "y");
	}

	@Override
	protected Void doInBackground(Void... params) {
	    // TODO Auto-generated method stub

	    ParseQuery<MembershipType> query = ParseQuery
		    .getQuery(DatabaseHelper.MEMBERSHIPTYPES_TABLE_NAME);
	    query.findInBackground(new FindCallback<MembershipType>() {

		@Override
		public void done(List<MembershipType> mTypes, ParseException e) {
		    if (e == null) {
			int total = mTypes.size();
			int count = 1;
			for (final MembershipType mType : mTypes) {
			    dataManager.getDatabaseHelper()
				    .addMembershipTypeWithID(mType);
			    publishProgress((count / total) * 100);
			    count++;
			}
		    }
		}
	    });

	    return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	    // TODO Auto-generated method stub
	    progressDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
	    // TODO Auto-generated method stub
	    progressDialog.dismiss();
	    Toast.makeText(context, "Data has been successfully downloaded",
		    Toast.LENGTH_SHORT).show();
	}

    }

}
