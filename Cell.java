package Naive_Sudoku;

import java.util.Set;
import java.util.HashSet;

public class Cell
{
	boolean isSet; // whether the cell value is set or the cell is empty
	int x, y; // (row, col)
	int id;
	int value;
	Set<Integer> possible = new HashSet<Integer>(); // contains all the hypothesis for this cell
	int max_val; // maximum for the value

	int finalValue = -1; // value to set the Cell to

	boolean default_print = true; // whether to print the value or the value+1 (if this is normal sudoku of hexadecimal sudoku, for instance)

	public Cell(int x_in,int y_in, int id, int size)
	{
		// Set an empty cell
		this.x = x_in;
		this.y = y_in;
		this.id = id;
		this.max_val = size;
		this.isSet = false;
		this.value = -1;
		for (int i=0; i<this.max_val; i++)
		{
			possible.add(i); // add all the digits (at start, all hypothesis are valid)
		}
	}

	public Cell(int x_in, int y_in, int id, int size, int val)
	{
		this.x = x_in;
		this.y = y_in;
		this.id = id;
		this.max_val = size;
		this.value = val;
		this.isSet = true;
	}


	// Setters

	public void setPossible(Set<Integer> pos)
	{
		this.possible = pos;
	}

	public void setValue(int val)
	{
		this.value = val;
		this.isSet = true;
		this.possible.clear(); // empty the set
	}

	public void setFinalValue(int val)
	{
		this.finalValue = val;
	}

	// Getters

	public int getValue()
	{
		return this.value;
	}

	public boolean getIsSet()
	{
		return this.isSet;
	}

	public Set<Integer> getPossible()
	{
		return this.possible;
	}

	public Integer getX()
	{
		return this.x;
	}

	public Integer getY()
	{
		return this.y;
	}

	public Integer getId()
	{
		return this.id;
	}

	public Integer getFinalValue()
	{
		return this.finalValue;
	}

	public Integer[] toArray()
	{
		return this.possible.toArray(new Integer[0]);
	}

	@Override
	public String toString()
	{
		// TODO : move this detection code to Grid.java, always return the value.toString() (or " ")
		if (this.isSet)
		{
			if (this.default_print)
				return String.valueOf(value+1);
			else
				return String.valueOf(value);
		}
		else
		{
			return " ";
		}
	}
}
