package com.haidozo.sudoku;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import com.haidozo.sudoku.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;

public class Sudoku extends Activity implements OnClickListener {
	
	private static String TAG = "Sudoku";	
	
	public Sudoku() {
		super();		
	}

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:                
                    Log.i(TAG, "OpenCV loaded successfully.");
                    break;
                default:
                	Log.i(TAG, "OpenCV loaded failed.");
                    super.onManagerConnected(status);
            }
        }
    };	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        
        // Creating onClick listeners for the buttons
        findViewById(R.id.continue_button).setOnClickListener(this);
        findViewById(R.id.new_button).setOnClickListener(this);
        findViewById(R.id.about_button).setOnClickListener(this);
        findViewById(R.id.exit_button).setOnClickListener(this);  
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.continue_button:
			startGame(Game.DIFFICULTY_CONTINUE);
			Mat m = new Mat(3,3,3);
			break;
		case R.id.new_button:
			// make abstract class game or smth.
			openNewGameDialog();
			break;
		case R.id.about_button:
			startActivity(new Intent(this, About.class));
			break;
		case R.id.exit_button:	
			finish();
			break;
		}	
	}

	private void openNewGameDialog() {
		
		new AlertDialog.Builder(this)
		.setTitle(R.string.new_game_title)
		.setItems(R.array.difficulty, new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startGame(which);
			}
		})
		.show();
	}	

	private void startGame(int which) {
		Log.d(TAG, "clicked on " + which);		
		
		Intent intent = 
				new Intent(this, Game.class);
		intent.putExtra(Game.KEY_DIFFICULTY, which);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	} 
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
		}
		return false;
	}
}
