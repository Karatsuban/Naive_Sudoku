package Naive_Sudoku;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class Block
{
	// this class is used to hold the content of a block in a sudoku grid
	// a block can be either a big square, a column or a row

	Set<Integer> values = new HashSet<Integer>(); // holds all the values of non-empty squares in this block
	ArrayList<Set<Cell>> hyp_count; // for each possible value, holds a set containing the cell IDs of the cell having this value as an hypothesis
	int block_size; // number of squares in a block

	public Block(int size)
	{
		this.block_size = size;
		this.hyp_count = new ArrayList<Set<Cell>>(); //[this.block_size];
		for(int i=0; i<this.block_size; i++)
		{
			this.hyp_count.add( new HashSet<Cell>() );
		}
	}


	public boolean addValue(int digit)
	{
		if (!this.values.contains(digit))
		{
			this.values.add(digit);
			return true; // the digit is unique in the block
		}
		return false; // the digit is a duplicate
	}


	public void removeCountOfCell(Cell cell)
	{
		// remove the given cell from the hyp_count
		for (int a=0; a<this.block_size; a++)
		{
			this.hyp_count.get(a).remove(cell);
		}
	}

	public ArrayList<Cell> updateCount(Cell cell)
	{
		// remove the cell from all the hyp_count
		// adds the cell to all the values of its possible in hyp_count


		this.removeCountOfCell(cell);
		Integer[] values = cell.toArray();
		Set<Cell> temp;

		ArrayList<Cell> cells = new ArrayList<Cell>();
		//System.out.println("For id="+id+" possibles="+possible);

		for (int a : values)
		{
			temp = this.hyp_count.get(a);
			temp.add(cell);
			if (temp.size() == 1)
			{
				cell.setFinalValue(a);
				cells.add(cell);
			}
			this.hyp_count.set(a, temp);

		}

		return cells;
	}


	public boolean has(int digit)
	{
		// return true if the given digit in contained in the block
		return this.values.contains(digit);
	}


	// Getter
	public Set<Integer> getValues()
	{
		return this.values;
	}

	public ArrayList<Set<Cell>> getHypCount()
	{
		return this.hyp_count;
	}

}
