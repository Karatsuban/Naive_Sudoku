package Naive_Sudoku;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;


public class Grid
{
	int base; // side of a big square
	int b_s; // nb of element in a big square
	Square[][] grid; // array storing the squares
	int empty_squares; // number of empty squares left
	LinkedList<Square> fifo_update = new LinkedList<Square>();
	Block[] squareBlock;
	Block[] columnBlock;
	Block[] rowBlock;

	public Grid(String desc)
	{
		// Initialize a grid base on a string value
		// the string can be of length 81, in that case, every character is a number or a blank
		// the string can also contain values like "X6", which describe 6 blank spaces

		// if the string contains less than 81 values, the grid is created following the values and...
		// ...the missing values are padded with blanks

		this.base = 3;
		this.b_s = this.base*this.base;
		this.grid = new Square[this.b_s][this.b_s];
		this.squareBlock = new Block[this.b_s];
		this.columnBlock = new Block[this.b_s];
		this.rowBlock = new Block[this.b_s];

		for (int a = 0; a<this.b_s; a++)
		{
			// initialize the Blocks
			this.squareBlock[a] = new Block();
			this.columnBlock[a] = new Block();
			this.rowBlock[a] = new Block();
		}

		int index = 0; // index of the lookout in the desc function
		int final_index = 0; // index of the write in the grid
		int i, j;
		int increment;
		String sub = "";
		String deployed = "";
		this.empty_squares = 0;

		while (index<desc.length())
		{
			i = final_index/this.b_s; // row number
			j = final_index%this.b_s; // col number

			sub = desc.substring(index, index+1);

			if (isNumeric(sub))
			{
				int value = Integer.parseInt(sub);
				if (value == 0 || value > this.b_s) // FIXME : in case we use [1-9], the limits are fine, but if we use [0-F], then there is a problem
				{
					System.out.println("Forbidden value found : "+value);
					System.exit(2);
				}
				if (!this.addSquare(i, j, Integer.parseInt(sub)))
				{
					System.out.println("Duplicate found !");
					System.exit(2);
				}

				index += 1;
				final_index += 1;
			}
			else if (sub.equals("("))
			{
				int index_end = desc.indexOf(")", index);
				if (index_end == -1) // no matching closing brace
				{
					System.out.println("Ill-formed string !");
					System.exit(2);
				}
				String values = desc.substring(index+1, index_end);
				int total_blanks = Integer.parseInt(values);

				for (int rep=0; rep<total_blanks; rep++)
				{
					i = (final_index+rep)/this.b_s;
					j = (final_index+rep)%this.b_s;

					if (!this.addSquare(i, j, -1))
					{
						System.out.println("Duplicate found !");
						System.exit(2);
					}
					this.empty_squares += 1;
				}
				index += index_end - index+1;
				final_index += total_blanks;
			}
			else if (sub.equals(" "))
			{
				if (!this.addSquare(i, j, -1))
				{
					System.out.println("Duplicate found !");
					System.exit(2);
				}
				index += 1;
				final_index += 1;
				this.empty_squares += 1;
			}
			else
			{
				System.out.println("Unknown value :"+sub);
				System.exit(2);
			}
		}

		while (final_index<81) // padding the rest of the squares with blank values
		{
			i = final_index/this.b_s;
			j = final_index%this.b_s;
			if (!this.addSquare(i, j, -1))
			{
				System.out.println("Duplicate found !");
				System.exit(2);
			}
			this.empty_squares += 1;
			final_index++;
		}

		System.out.println("There are "+this.empty_squares+" empty squares in this grid");

	}



	public boolean addSquare(int x_in, int y_in, int value_in)
	{
		// Creates a new Square and put it at the given coordinates in the grid
		if (value_in != -1)
		{
			this.grid[x_in][y_in] = new Square(x_in, y_in, value_in);
			if (!this.updateBlocks(x_in, y_in, value_in))
				return false; // the square is a duplicate !
		}
		else
		{
			this.grid[x_in][y_in] = new Square(x_in, y_in);
		}
		return true; // this square is not a duplicate
	}


	public boolean updateBlocks(int x_in, int y_in, int value_in)
	{
		// Adds a value in every Block the coordinates are contained in (big square, row & column)
		// returns true if the value is not a duplicate in any Block
		// else returns false
		int i = (y_in/this.base)%this.base;
		int j = (x_in/this.base)%this.base;
		boolean isUnique = true;
		isUnique &= this.columnBlock[y_in].add(value_in);
		isUnique &= this.rowBlock[x_in].add(value_in);
		isUnique &= this.squareBlock[this.base*j+i].add(value_in);
		return isUnique;
	}




	// Functions to update the possibility each square

	public void updateAllSquares()
	{
		int i,j;
		for (i=0; i<this.b_s; i++) // row number
		{
			for (j=0; j<this.b_s; j++) // col number
			{
				if (!this.grid[i][j].getIsSet())
					this.updateSquare(i,j); // (row, col)
			}
		}

		/*
		System.out.println("FIFO update :");
		for (int s=0; s<this.fifo_update.size(); s++)
		{
			Square sq = this.fifo_update.get(s);
			System.out.println(sq.getX()+" "+sq.getY());
		}
		*/
	}


	public void updateSquare(int x, int y)
	{
		// (x,y) = (row, col)
		//System.out.println("Updating square"+x+" "+y+" value ="+this.grid[x][y].getValue());
		Set<Integer> union; // contains all the other number in the biqSquare, row and column
		union = new HashSet<>();
		int i = (y/this.base)%this.base;
		int j = (x/this.base)%this.base;
		union.addAll(this.columnBlock[y].getValues());
		union.addAll(this.rowBlock[x].getValues());
		union.addAll(squareBlock[this.base*j+i].getValues());

		/*
		System.out.println("Values from col "+y+" "+this.columnBlock[y].getValues());
		System.out.println("Values from row "+x+" "+this.rowBlock[x].getValues());
		System.out.println("Values from square "+(this.base*j+i)+" "+squareBlock[this.base*j+i].getValues());
		*/

		Set<Integer> temp = this.grid[x][y].getPossible();

		//System.out.println("Possible before : "+temp);

		temp.removeAll(union);
		this.grid[x][y].setPossible(temp);

		//System.out.println("Possible after :"+temp);

		if (temp.size() == 1) // the square (x,y) has only one possibility
		{
			if (!this.fifo_update.contains(this.grid[x][y]))
			{
				System.out.println("Adding "+y+" "+x+" : "+this.grid[x][y].toArray()[0]);
				this.fifo_update.addLast(this.grid[x][y]); // add the index of the square at the END of the queue
			}
		}

	}



	public boolean iterate()
	{
		System.out.println("There are currently: "+this.fifo_update.size()+" elements in the fifo");
		if (this.fifo_update.size() == 0)
		{
			return false; // no more elements to update
		}

		Square elt = this.fifo_update.removeFirst(); // get the first element of the fifo
		int x = elt.getX();
		int y = elt.getY();

		System.out.println("Doing elt at coord: "+y+" "+x);
		System.out.println("Size of all possible is: "+elt.getPossible().size());

		int value = elt.toArray()[0]; // get the only possible value for this square
		elt.setValue(value); // set the value of its square


		this.updateBlocks(x, y, value);

		// compute o_i and o_j
		int oj = (y/this.base)%this.base;
		int oi = (x/this.base)%this.base;
		//System.out.println("Big Square origin: "+oi+" "+oj);
		int i,j;
		for (int a = 0; a<this.b_s; a++)
		{
			j = a/this.base;
			i = a%this.base;
			//System.out.print("idx: "+a+" (i, j)="+i+","+j+"  ");
			this.updateSquare(x, a); // update every square in the row x
			this.updateSquare(a, y); // update every square in the column y

			// compute two indices from a
			this.updateSquare(oj+j, oi+i); // FIXME : compute the coordinates of the square in the big square
		}
		System.out.println();
		// update the Blocks attached to it
		// loop on all the squares from the column, row and square around and update them
		return true; // one element has been updated
	}


	// GETTERS


	public int getUpdatableSize()
	{
		return this.fifo_update.size();
	}


	@Override
	public String toString()
	{
		String out="";
		int i,j = 0;
		for (i=0; i<this.b_s; i++) // row number
		{
			if (i%this.base == 0)
				out += "-------------\n"; // FIXME : does not work in case the base is NOT 3

			for (j=0; j<this.b_s; j++) // col number
			{
				if (j%this.base == 0)
					out += "|";
				out += this.grid[i][j].toString();
				if (j == this.b_s-1)
					out += "|";
			}
			out += "\n";
		}
		out += "-------------\n"; // FIXME : does not work in case the base is NOT 3
		System.out.println();
		return out;
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}


}

