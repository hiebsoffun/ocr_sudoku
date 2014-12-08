package com.haidozo.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.haidozo.sudoku.R;

public class Keypad extends Dialog {
	
	private static final String TAG = "Sudoku";
	
	private final View keys[] = new View[9];
	private View keypad;
	
	private final int[] useds;
	private final PuzzleView puzzleView;

	public Keypad(Context context, int useds[], PuzzleView puzzleView) {
		super(context);
		this.useds = useds;
		this.puzzleView = puzzleView;
		
		// dismiss dialog when touching outside
		this.setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.keypad_title);
		setContentView(R.layout.keypad);
		findViews();
		boolean hintsON = GridPrefs.getColorHints(puzzleView.getContext()) || GridPrefs.getNumberHints(puzzleView.getContext());
		if(hintsON) {
			for(int element : useds) {
				if(element != 0) {
					keys[element-1].setVisibility(View.INVISIBLE);
				}
			}	
		}

		setListeners();		
	}

	private void setListeners() {
		for(int i=0; i<keys.length; i++) {
			final int valueToSet = i + 1;
			keys[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					returnValueToSet(valueToSet);
				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int valueToSet = 0;
		
		switch(keyCode) {
		
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE:	valueToSet = 0; break;
		case KeyEvent.KEYCODE_1:		valueToSet = 1; break;
		case KeyEvent.KEYCODE_2:		valueToSet = 2; break;
		case KeyEvent.KEYCODE_3:		valueToSet = 3; break;
		case KeyEvent.KEYCODE_4:		valueToSet = 4; break;
		case KeyEvent.KEYCODE_5:		valueToSet = 5; break;
		case KeyEvent.KEYCODE_6:		valueToSet = 6; break;
		case KeyEvent.KEYCODE_7:		valueToSet = 7; break;
		case KeyEvent.KEYCODE_8:		valueToSet = 8; break;
		case KeyEvent.KEYCODE_9:		valueToSet = 9; break;
		
		default:
			 return super.onKeyDown(keyCode, event);
		
		}
		
		if(isValid(valueToSet)) {
			returnValueToSet(valueToSet);
		}
		return true;
	}
		
	private void returnValueToSet(int valueToSet) {
		puzzleView.setSelectedTile(valueToSet);
		// everything's done - close the dialog
		dismiss();
	}

	private boolean isValid(int valueToSet) {
		for(int v : useds) {
			if(valueToSet == v) {
				return false;
			}
		}
		return true;
	}

	private void findViews() {
		keypad = findViewById(R.layout.keypad);
		
		keys[0] = findViewById(R.id.keypad_1);
		keys[1] = findViewById(R.id.keypad_2);
		keys[2] = findViewById(R.id.keypad_3);
		keys[3] = findViewById(R.id.keypad_4);
		keys[4] = findViewById(R.id.keypad_5);
		keys[5] = findViewById(R.id.keypad_6);
		keys[6] = findViewById(R.id.keypad_7);
		keys[7] = findViewById(R.id.keypad_8);
		keys[8] = findViewById(R.id.keypad_9);		
	}
	
}
