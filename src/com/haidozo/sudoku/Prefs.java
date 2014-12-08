package com.haidozo.sudoku;

import com.haidozo.sudoku.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputFilter.LengthFilter;
import android.view.Gravity;
import android.widget.Toast;


public class Prefs extends PreferenceActivity {

	// Sudoku general Preferences
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEFAULT = false;
	private static final String OPT_DEBUG = "debug";
	private static final boolean OPT_DEBUG_DEFAULT = false;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// look for PreferenceFragment class doku for the new good way
		addPreferencesFromResource(R.xml.settings);
	}
	
	public static boolean getMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC, OPT_MUSIC_DEFAULT);
	}
	
	public static boolean getDebug(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_DEBUG, OPT_DEBUG_DEFAULT);
	}
}
