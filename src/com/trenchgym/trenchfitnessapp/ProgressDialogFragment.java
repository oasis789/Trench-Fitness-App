package com.trenchgym.trenchfitnessapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

public class ProgressDialogFragment extends DialogFragment{
   
    private ProgressDialog mProgressDialog;
    private int mMax = 100;

    public ProgressDialogFragment(){
	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(mMax);
        mProgressDialog.setCanceledOnTouchOutside(false);
        OnKeyListener keyListener = new OnKeyListener() {
            
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode,
				KeyEvent event) {
			
			if( keyCode == KeyEvent.KEYCODE_BACK){					
				return true;
			}
			return false;
		}

	
	};
	mProgressDialog.setOnKeyListener(keyListener);
        return mProgressDialog;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
    
    public void setTitle(String title){
	mProgressDialog.setTitle(title);
    }

    public void setMax(int arg1) {
        mProgressDialog.setMax(arg1);
        mMax = arg1;
    }

    public void setProgress(int arg1) {
        mProgressDialog.setProgress(arg1);
    }
}
