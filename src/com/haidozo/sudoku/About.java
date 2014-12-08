package com.haidozo.sudoku;

import com.haidozo.sudoku.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;



public class About extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		finish();
		return true;
	}

	
}
