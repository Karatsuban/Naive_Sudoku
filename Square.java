package Naive_Sudoku;

import java.util.Set;
import java.util.HashSet;

public class Square
{
	boolean isSet;
	int x, y; // (row, col)
	int value;
	Set<Integer> possible = new HashSet<Integer>();

	public Square(int x_in,int y_in)
	{
		this.x = x_in;
		this.y = y_in;
		this.isSet = false;
		this.value = -1;
		for (int i=1; i<=9; i++)
		{
			possible.add(i);
		}
	}

	public Square(int x_in, int y_in, int val)
	{
		this.x = x_in;
		this.y = y_in;
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

	public Integer[] toArray()
	{
		return this.possible.toArray(new Integer[0]);
	}

	@Override
	public String toString()
	{
		if (this.isSet)
			return String.valueOf(value);
		else
			return " ";
	}
}
