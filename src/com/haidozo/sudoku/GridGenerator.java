package com.haidozo.sudoku;

import java.util.Vector;

public class GridGenerator {

	int[] grid = new int[81];
	
		
	public GridGenerator() {
		
	}
	
	public int[] generateGrid(int diff) {
						
		return grid;
	}

	private int getRandom(int low, int high) {
		return (int)(Math.random() * (high - low) + low);		
	}

}
