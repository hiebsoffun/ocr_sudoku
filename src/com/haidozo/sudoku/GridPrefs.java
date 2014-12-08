package com.haidozo.sudoku;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

public class GridPrefs extends PreferenceActivity {
	
	private static final String OPT_COLOR_HINTS = "color_hints";
	private static final boolean OPT_COLOR_HINTS_DEFAULT = false;
	private static final String OPT_NUMBER_HINTS = "number_hints";
	private static final boolean OPT_NUMBER_HINTS_DEFAULT = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		// look for PreferenceFragment class doku for the new good way
		addPreferencesFromResource(R.xml.grid_settings);
	}
	
	public static boolean getColorHints(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_COLOR_HINTS, OPT_COLOR_HINTS_DEFAULT);
	}
	
	public static boolean getNumberHints(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_NUMBER_HINTS, OPT_NUMBER_HINTS_DEFAULT);
	}
	
	
	
}
