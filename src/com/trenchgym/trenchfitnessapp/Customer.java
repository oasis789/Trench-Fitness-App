package com.trenchgym.trenchfitnessapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.parse.*;

@ParseClassName(DatabaseHelper.CUSTOMER_TABLE_NAME)
public class Customer extends ParseObject {
//New Comment
    //Another Comment
    public static final String IMAGE_FILE_EXTENSION = ".png";
    public static final String IMAGE_ROOT = Environment
	    .getExternalStorageDirectory().toString()
	    + "/"
	    + TrenchFitnessApp.appName;
    public static final String IMAGE_FOLDER = IMAGE_ROOT + "/"
	    + DatabaseHelper.CUSTOMER_TABLE_NAME;
    public String IMAGE_PATH;

    public Customer() {
    }

    // Getters
    
    /**
     * @return the customer id.
     */
    public int getCustomerID() {
	return getInt(DatabaseHelper.CUSTOMER_KEY_ID);
    }

    public String getfName() {
	return getString(DatabaseHelper.CUSTOMER_KEY_FIRST_NAME);
    }

    public String getlName() {
	return getString(DatabaseHelper.CUSTOMER_KEY_LAST_NAME);
    }

    public Calendar getDob() {
	return TrenchFitnessApp
		.getCalendarFromFormattedLong(getLong(DatabaseHelper.CUSTOMER_KEY_DOB));
    }

    public Calendar getJoinDate() {
	return TrenchFitnessApp
		.getCalendarFromFormattedLong(getLong(DatabaseHelper.CUSTOMER_KEY_JOIN_DATE));
    }

    public String getPhoneNumber() {
	return getString(DatabaseHelper.CUSTOMER_KEY_PHONE_NUMBER);
    }

    public String getEmergencyContactNumber() {
	return getString(DatabaseHelper.CUSTOMER_KEY_EMERGENCY_NUMBER);
    }

    public String getAddress() {
	return getString(DatabaseHelper.CUSTOMER_KEY_ADDRESS);
    }

    public String getPostCode() {
	return getString(DatabaseHelper.CUSTOMER_KEY_POST_CODE);
    }

    public String getMedicalConditions() {
	return getString(DatabaseHelper.CUSTOMER_KEY_MEDICAL_CONDITIONS);
    }

    // Setters
    public void setCustomerID(int customerID) {
	put(DatabaseHelper.CUSTOMER_KEY_ID, customerID);
	IMAGE_PATH = IMAGE_FOLDER + "/" + customerID + IMAGE_FILE_EXTENSION;
    }

    public void setfName(String fName) {
	put(DatabaseHelper.CUSTOMER_KEY_FIRST_NAME, fName);
    }

    public void setlName(String lName) {
	put(DatabaseHelper.CUSTOMER_KEY_LAST_NAME, lName);
    }

    public void setDob(Calendar dob) {
	put(DatabaseHelper.CUSTOMER_KEY_DOB,
		TrenchFitnessApp.formatDateAsLong(dob));
    }

    public void setJoinDate(Calendar joinDate) {
	put(DatabaseHelper.CUSTOMER_KEY_JOIN_DATE,
		TrenchFitnessApp.formatDateAsLong(joinDate));
    }

    public void setPhoneNumber(String phoneNumber) {
	put(DatabaseHelper.CUSTOMER_KEY_PHONE_NUMBER, phoneNumber);
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
	put(DatabaseHelper.CUSTOMER_KEY_EMERGENCY_NUMBER,
		emergencyContactNumber);
    }

    public void setAddress(String address) {
	put(DatabaseHelper.CUSTOMER_KEY_ADDRESS, address);
    }

    public void setPostCode(String postCode) {
	put(DatabaseHelper.CUSTOMER_KEY_POST_CODE, postCode);
    }

    public void setMedicalConditions(String medicalConditions) {
	put(DatabaseHelper.CUSTOMER_KEY_MEDICAL_CONDITIONS, medicalConditions);
    }

    public String getFullName() {
	return TrenchFitnessApp.capitaliseFirstLetter(getfName()) + " "
		+ TrenchFitnessApp.capitaliseFirstLetter(getlName());
    }

    public String getFullAddress() {
	return TrenchFitnessApp.capitaliseFirstLetterOfEachWord(getAddress())
		+ "\n" + getPostCode().toUpperCase(Locale.ENGLISH);
    }

    public void saveImageLocally(Bitmap bitmap) {
	File myDir = new File(IMAGE_FOLDER);
	myDir.mkdirs();
	String fname = getCustomerID() + IMAGE_FILE_EXTENSION;
	File file = new File(myDir, fname);
	if (file.exists())
	    file.delete();
	try {
	    // Save locally
	    FileOutputStream out = new FileOutputStream(file);
	    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	    out.flush();
	    out.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void saveImageOnServer(Bitmap bitmap) {
	// Save on server
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
	byte[] data = stream.toByteArray();
	ParseFile pFile = new ParseFile(getCustomerID() + IMAGE_FILE_EXTENSION,
		data);
	pFile.saveInBackground();
	put("image", pFile);
	saveInBackground();
    }

    public Bitmap getLocalImageFile() {
	String imageFile = IMAGE_PATH;
	Bitmap bmp = BitmapFactory.decodeFile(imageFile);
	if (bmp != null) {
	    return bmp;
	} else {
	    return null;
	}
    }

    public Bitmap getImageFromServer() {
	ParseFile image = (ParseFile) getParseFile("image");
	if (image != null) {
	    Bitmap bmp = null;
	    try {
		bmp = BitmapFactory.decodeByteArray(image.getData(), 0,
			image.getData().length);

	    } catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    return bmp;
	} else {
	    deleteImage();
	    return null;
	}
    }

    public void getAndSaveServerImage() {
	Bitmap bmp = getImageFromServer();
	if (bmp != null) {
	    saveImageLocally(bmp);
	}
    }

    public void deleteImage() {
	String IMAGE_PATH = IMAGE_FOLDER + "/" + getCustomerID() + IMAGE_FILE_EXTENSION;
	Log.v("Tag", IMAGE_PATH);
	File file = new File(IMAGE_PATH);
	file.delete();
    }

}
