package com.haidozo.sudoku;

import com.haidozo.sudoku.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

public class PuzzleView extends View {

	private static String TAG = "Sudoku";	
	private final int boardSize = 9; //Integer.parseInt(getResources().getString(R.string.board_size));
	
	private final Game game;
	private float tileWidth;
	private float tileHeight;
	private int selX;
	private int selY;
	private final Rect selRect = new Rect();
	
	private static final String SELX = "selX";
	private static final String SELY = "selY";
	private static final String VIEW_STATE = "viewState";
	private static final int id = 42;

	private String solved_puzzle = null;
	
	public PuzzleView(Context context) {
		super(context);
		selX = 0;
		selY = 0;
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		setId(id);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		tileWidth = w / (float) boardSize;
		tileHeight = h / (float) boardSize;
		
		getRect(selX, selY, selRect);
		Log.d(TAG, "onSizeChanged: width: " + tileWidth + " height: " + tileHeight);		
		super.onSizeChanged(w, h, oldw, oldh);		
	}

	private void getRect(int x, int y, Rect selRect) {
		selRect.set((int) (x * tileWidth), (int) (y * tileHeight), (int) (x * tileWidth + tileWidth), (int) (y * tileHeight + tileHeight));				
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i(TAG, "PuzzleView onDraw()");
		
		Paint background = new Paint();
		Paint dark = new Paint();
		Paint hilite = new Paint();
		Paint light = new Paint();
		
		// Draw the background
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		// Draw the board...
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		light.setColor(getResources().getColor(R.color.puzzle_light));
		
		// ...draw thin lines
		drawGrid(canvas, dark, hilite, light);
				
		// Draw the numbers
		if(true) {
			game.solvePuzzle();		
		}
		drawNumbers(canvas);
		
		// Draw the hints
		if(GridPrefs.getColorHints(getContext())) {
			drawColoredHints(canvas);	
		}
		
		if(GridPrefs.getNumberHints(getContext())) {
			drawNumberedHints(canvas);	
		}				
				
		// Draw the selection
		Log.d(TAG, "selRect = " + selRect);
		Paint selected = new Paint(); 
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selected);
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Log.d(TAG, "onRestoreInstanceState");
		
		Bundle b = (Bundle) state;
		
		select(b.getInt(SELX), b.getInt(SELY));
		super.onRestoreInstanceState(b.getParcelable(VIEW_STATE));		
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Log.d(TAG, "onSaveInstanceState");
		
		Parcelable p = super.onSaveInstanceState();
		Bundle bundle = new Bundle();
		
		bundle.putInt(SELX, selX);
		bundle.putInt(SELY, selY);
		bundle.putParcelable(VIEW_STATE, p);
		
		return bundle;
	}

	private void drawGrid(Canvas canvas, Paint dark, Paint hilite, Paint light) {
		for(int i=0; i<9; i++) {
			canvas.drawLine(0, i * tileHeight, getWidth(), i * tileHeight, light);
			canvas.drawLine(0, i * tileHeight + 1, getWidth(), i * tileHeight + 1, hilite);
			canvas.drawLine(i * tileWidth, 0, i * tileWidth, getHeight(), light);
			canvas.drawLine(i * tileWidth + 1, 0, i * tileWidth + 1, getHeight(), hilite);
		}
		
		// ...draw major grid lines
		for(int i=0; i<(float) boardSize; i++) {
			if( i % Math.sqrt(boardSize) == 0) {				
				canvas.drawLine(0, i * tileHeight, getWidth(), i * tileHeight, dark);
				canvas.drawLine(i * tileWidth, 0, i * tileWidth, getHeight(), dark);				
			}
		}
	}

	private void drawNumbers(Canvas canvas) {
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(tileHeight * .75f);
		foreground.setTextScaleX(tileWidth/tileHeight);
		foreground.setTextAlign(Align.CENTER);
		
		FontMetrics fm = foreground.getFontMetrics();
		float x = tileWidth / 2;
		float y = tileHeight / 2 - (fm.ascent + fm.descent) / 2;
		
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				canvas.drawText(game.getTileString(i, j), i * tileWidth + x, j * tileHeight + y, foreground);
			}			
		}
	}

	private void drawColoredHints(Canvas canvas) {
		Paint hint = new Paint();
		int c[] = { getResources().getColor(R.color.puzzle_hint_0),
				getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2) };
	
		Rect rect = new Rect();
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				int solutionsLeft = boardSize - game.getUsedTiles(i, j).length;
				if(solutionsLeft <= c.length && solutionsLeft > 0) {
					getRect(i, j, rect);
					hint.setColor(c[solutionsLeft-1]);
					canvas.drawRect(rect, hint);
					invalidate(rect);
				}
			}
		}
	}

	private void drawNumberedHints(Canvas canvas) {
		Rect rect = new Rect();
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				int[] unused = game.getUnusedTiles(i, j);
				if(unused.length != 0) {
					float innerTileHeight = tileHeight / 3.0f;
					float innerTileWidth = tileWidth / 3.0f;
					
					Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
					foreground.setColor(getResources().getColor(R.color.puzzle_inner_tile));
					foreground.setStyle(Style.FILL);
					foreground.setTextSize(innerTileHeight * .8f);
					foreground.setTextScaleX(innerTileWidth/innerTileHeight);
					foreground.setTextAlign(Align.CENTER);
					
					FontMetrics fm = foreground.getFontMetrics();
					float x = innerTileWidth / 2;
					float y = innerTileHeight / 2 - (fm.ascent + fm.descent) / 2;
															
					int k = 0;
					for(int l=0; l<(boardSize/Math.sqrt(boardSize)); l++) {
						for(int m=0; m<(boardSize/Math.sqrt(boardSize)); m++) {
							if(unused[k] != 0) {		
								canvas.drawText(String.valueOf(unused[k]), i * tileWidth + ((m%3) * innerTileWidth) + x, j * tileHeight + ((l%3) * innerTileHeight) + y, foreground);
							}
							k++;
						}			
					}
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown: keyCode = " + keyCode + " event = " + event);
		
		switch(keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX, selY - 1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX, selY + 1);
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX - 1, selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX + 1, selY);
			break;
		// to change if boardSize grows ;-)
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE:	setSelectedTile(0); break;
		case KeyEvent.KEYCODE_1:		setSelectedTile(1); break;
		case KeyEvent.KEYCODE_2:		setSelectedTile(2); break;
		case KeyEvent.KEYCODE_3:		setSelectedTile(3); break;
		case KeyEvent.KEYCODE_4:		setSelectedTile(4); break;
		case KeyEvent.KEYCODE_5:		setSelectedTile(5); break;
		case KeyEvent.KEYCODE_6:		setSelectedTile(6); break;
		case KeyEvent.KEYCODE_7:		setSelectedTile(7); break;
		case KeyEvent.KEYCODE_8:		setSelectedTile(8); break;
		case KeyEvent.KEYCODE_9:		setSelectedTile(9); break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeypadOrError(selX, selY);
			break;
		default:
			 return super.onKeyDown(keyCode, event);
		}
		
		return true;
	}
	
	public void setSelectedTile(int tile) {
		if(game.setTileIfValid(selX, selY, tile)) {
			invalidate();
		} else {
			Log.d(TAG, "setSelectedTile: invalid: " + tile);
			startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		}
		
		select( (int) (event.getX() / tileWidth), (int) (event.getY() / tileHeight));
		game.showKeypadOrError(selX, selY);
		Log.d(TAG, "onTouchEvent: x = " + selX + ", y = " + selY);
		return true;
	}

	private void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), boardSize-1);
		selY = Math.min(Math.max(y, 0), boardSize-1);
		getRect(selX, selY, selRect);
		invalidate(selRect);		
	}	

}
