package com.haidozo.sudoku;

import java.util.ArrayList;
import java.util.Collections;

public class SudokuSolver {
	
	private Game game;
	private int boardSize;
	private ArrayList<Integer> numberOfSolutions;
	private ArrayList<Integer> sortedIndices;
	
	public SudokuSolver(Game game) {
		this.game = game;
		this.boardSize = game.getBoardSize();
		
		numberOfSolutions = new ArrayList<Integer>();
		sortedIndices = new ArrayList<Integer>();
	}


	/*
	 * Searches for the tiles with a single valid value
	 */
	private void solveSingleValueValidTiles() {
    	int size = boardSize*boardSize;
		for(int i=0; i<size; i++) {
			int y = i / boardSize;
	    	int x = i % boardSize;
	    	
	    	int[] unusedTiles = shrink(game.getUnusedTiles(x, y)); // remove zeros
	    	if(unusedTiles.length == 1 && ! game.getPredefined()[i]) {
	    		game.setTile(x, y, unusedTiles[0]);
	    		game.calculateUsedTiles();
	    		game.calculatePredefined();
	    		i = 0;
	    	}			
		}
	}

	private int[] shrink(int[] unusedTiles) {
		ArrayList<Integer> unused = new ArrayList<Integer>();
		
		for(int i : unusedTiles) {
			if(i != 0) {
				unused.add(i);
			}
		}
		
		// convert ArrayList to array
		int[] u = new int[unused.size()];
		for(int i=0; i<unused.size(); i++) {
			u[i] = unused.get(i);
		}
		return u;
	}

	public boolean solve() {
		game.calculatePredefined();
    	solveSingleValueValidTiles();
    	
    	game.calculatePredefined();
    	fillNSolutions();
    	solveNSolutionsOrder();
    	
    	return true; //solve(0);	
	}
	
	/*
	 * Calculates the number of valid solutions of each tile and sorts 
	 * the resulting array ascending
	 */
	private void fillNSolutions() {
    	int size = boardSize*boardSize;		
		for(int i=0; i<size; i++) {
			int y = i / boardSize;
	    	int x = i % boardSize;
	  
	    	int[] unusedTiles = shrink(game.getUnusedTiles(x, y)); // remove zeros
	    	if( !game.getPredefined()[i] ) {
	    			numberOfSolutions.add(unusedTiles.length);
	    	} else {
    			numberOfSolutions.add(0);	    			
	    	}
		}
		
		// Sort the tiles in ascending order according to their number of unused digits
		ArrayList<Integer> tmp = numberOfSolutions;
		while(sortedIndices.size() < numberOfSolutions.size()) {
			int minIndex = 0;
			for(int i=0; i<numberOfSolutions.size(); i++) {
				if(numberOfSolutions.get(i) == 0)
					continue;								
				int min = numberOfSolutions.get(i);
				for(int j=0; j<numberOfSolutions.size(); j++) {
					int comp;
					if( (comp=numberOfSolutions.get(j)) > 0 &&
						comp < min) {
						min = comp;
						minIndex = j;
					}
				}
			}
			numberOfSolutions.set(minIndex, 0);
			sortedIndices.add(minIndex);			
		}
	}
	
	private void solveNSolutionsOrder() {
		
	}


	private boolean solve(int pos) {
    	
    	if(pos == boardSize*boardSize) {	// reached end of board
    		return true;
    	}
    		
    	int y = pos / boardSize;
    	int x = pos % boardSize;
    	int v;
    	
    	if(game.getPredefined()[boardSize * y + x]) {
    		return solve(pos+1);
    	} else {
    		v = game.getTile(x, y);
    		if(v < 1 || v > 9) {
    			v = 1;
    		}
    		while(v <= 9) {
    			game.setTile(x, y, v);
    			if(game.isTileValid(x, y, v)) {
    				game.calculateUsedTiles();
    				boolean b = solve(pos+1);
    				if(b) {
    					return true;
    				}
    			}
        		v++;
    		}
    	}
    	
    	if(v == 10) {
    		game.setTile(x, y, 0);
    		game.calculateUsedTiles();
    		return false;
    	}   	
    	
    	return true;
    }
}
