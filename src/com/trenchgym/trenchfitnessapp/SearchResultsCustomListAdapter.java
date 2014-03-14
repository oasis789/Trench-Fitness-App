package com.trenchgym.trenchfitnessapp;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchResultsCustomListAdapter extends ArrayAdapter<Customer> {

    private Context context;
    private List<Customer> list;

    public SearchResultsCustomListAdapter(Context context,int resource,
	    List<Customer> list) {
	super(context, resource, list);
	this.context = context;
	this.list = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
	// TODO Auto-generated method stub
	LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	view = inflater.inflate(R.layout.listview_individual_search_result,
		null);
	Customer searchResult = list.get(position);
	TextView tvFullName = (TextView) view.findViewById(R.id.tvSearchResultFullName);
	TextView tvAddress = (TextView) view.findViewById(R.id.tvSearchResultAddress);
	TextView tvDateOfBirth = (TextView) view.findViewById(R.id.tvSearchResultsDob);
	TextView tvID = (TextView) view.findViewById(R.id.tvSearchResultID);
	ImageView ivSearchResult = (ImageView) view.findViewById(R.id.ivSearchResultPicture);
	
	tvID.setText("ID: #" + String.valueOf(searchResult.getCustomerID()));
	tvFullName.setText(searchResult.getFullName());
	tvAddress.setText(searchResult.getAddress());
	tvDateOfBirth.setText("Date Of Birth: " + TrenchFitnessApp.calendarToString(searchResult.getDob()));
	Bitmap bmp = searchResult.getLocalImageFile();
	if(bmp != null){
	    ivSearchResult.setImageBitmap(bmp);
	}else{
	    ivSearchResult.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
	}
	return view;
    }

}
