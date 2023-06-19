package Naive_Sudoku;

import java.util.Set;
import java.util.HashSet;

public class Block
{
	// this class is used to hold the content of a block in a sudoku grid
	// a block can be either a big square, a column or a row

	Set<Integer> values = new HashSet<Integer>();

	public Block()
	{
	}


	public boolean add(int digit)
	{
		if (!this.values.contains(digit))
		{
			this.values.add(digit);
			return true; // the digit is unique in the block
		}
		return false; // the digit is a duplicate
	}


	// Getter
	public Set<Integer> getValues()
	{
		return this.values;
	}


}
