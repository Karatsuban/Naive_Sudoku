package Naive_Sudoku;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;


public class Grid
{
	int base; // side of a big square
	int b_s; // nb of element in a big square
	Cell[][] grid; // array storing the Cells
	int empty_cells; // number of empty cells left
	LinkedList<Cell> fifo_update = new LinkedList<Cell>();
	Block[] squareBlock;
	Block[] columnBlock;
	Block[] rowBlock;
	boolean default_grid = false; // wether this is a default 9x9 sudoku grid

	public Grid(String desc, int size)
	{
		// Initialize a grid base on a string value
		// the string can be of length 81, in that case, every character is a number or a blank
		// the string can also contain values like "X6", which describe 6 blank spaces

		// if the string contains less than 81 values, the grid is created following the values and...
		// ...the missing values are padded with blanks

		this.base = size;
		this.b_s = this.base*this.base;
		this.grid = new Cell[this.b_s][this.b_s];
		this.squareBlock = new Block[this.b_s];
		this.columnBlock = new Block[this.b_s];
		this.rowBlock = new Block[this.b_s];
		this.empty_cells = 0;
		if (this.base == 3)
			this.default_grid = true;

		for (int a = 0; a<this.b_s; a++)
		{
			// initialize the Blocks
			this.squareBlock[a] = new Block(this.b_s);
			this.columnBlock[a] = new Block(this.b_s);
			this.rowBlock[a] = new Block(this.b_s);
		}

		this.populate(desc);
		System.out.println("There are "+this.empty_cells+" empty cells in this grid");

		this.updateAllCells(1); // first pass
		this.updateAllCells(2); // second pass
		this.updateAllCells(3); // second pass
	}


	public void populate(String desc)
	{
		// Fills the grid by creating set or empty cells, according to the input string
		int index = 0; // index of the lookout in the desc function
		int final_index = 0; // index of the write in the grid
		int i, j;
		int increment;
		String sub = "";

		while (index<desc.length())
		{
			i = final_index/this.b_s; // row number
			j = final_index%this.b_s; // col number

			sub = desc.substring(index, index+1);

			if (isNumeric(sub))
			{
				int value = Integer.parseInt(sub);
				value = this.trueValue(value);

				if (value < 0 || value >= this.b_s)
				{
					System.out.println("Forbidden value found : "+value);
					System.exit(2);
				}

				if (!this.addCell(i, j, value)) // character to integer
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

					if (!this.addCell(i, j, -1))
					{
						System.out.println("Duplicate found !");
						System.exit(2);
					}
					this.empty_cells += 1;
				}
				index += index_end - index+1;
				final_index += total_blanks;
			}
			else if (sub.equals(" "))
			{
				if (!this.addCell(i, j, -1))
				{
					System.out.println("Duplicate found !");
					System.exit(2);
				}
				index += 1;
				final_index += 1;
				this.empty_cells += 1;
			}
			else
			{
				System.out.println("Unknown value :"+sub);
				System.exit(2);
			}
		}

		while (final_index<81) // padding the rest of the cells with blank values
		{
			i = final_index/this.b_s;
			j = final_index%this.b_s;
			if (!this.addCell(i, j, -1))
			{
				System.out.println("Duplicate found !");
				System.exit(2);
			}
			this.empty_cells += 1;
			final_index++;
		}
	}


	private int trueValue(int val)
	{
		// return the true value of the given input, according to the grid type
		if (this.default_grid)
			return val-1;
		else
			return val;
	}


	public boolean addCell(int x_in, int y_in, int value_in)
	{
		// Creates a new Cell and put it at the given coordinates in the grid
		int id = x_in*this.b_s+y_in;

		if (value_in != -1)
		{
			this.grid[x_in][y_in] = new Cell(x_in, y_in, id, this.b_s, value_in);
			if (!this.updateBlocksValues(x_in, y_in, value_in))
				return false; // the cell value is a duplicate !
		}
		else
		{
			this.grid[x_in][y_in] = new Cell(x_in, y_in, id, this.b_s);
		}

		return true; // this cell value is not a duplicate
	}


	public boolean updateBlocksValues(int x_in, int y_in, int value_in)
	{
		// Adds a value in every Block the coordinates the cell belongs to (big square, row & column)
		// returns true if the value is not a duplicate in any Block
		// else returns false
		int i = (y_in/this.base)%this.base;
		int j = (x_in/this.base)%this.base;
		boolean isUnique = true;
		isUnique &= this.columnBlock[y_in].addValue(value_in);
		isUnique &= this.rowBlock[x_in].addValue(value_in);
		isUnique &= this.squareBlock[this.base*j+i].addValue(value_in);
		return isUnique;
	}


	public ArrayList<Cell> updateBlocksCounts(Cell cell)
	{
		// for each Block to which the Cell belongs,
		// decrement the counter from each of the 'before' digits
		// increment the counter from each of the 'after' digits
		// if after incrementing, the counter of the digit is 0 then call update_fifo on this element and set it's value to the digit

		int x = cell.getX();
		int y = cell.getY();

		int id = x*this.b_s+y;
		int i = (y/this.base)%this.base;
		int j = (x/this.base)%this.base;

		ArrayList<Cell> cells = new ArrayList<Cell>();

		cells.addAll(this.columnBlock[y].updateCount(cell));
		cells.addAll(this.rowBlock[x].updateCount(cell));
		cells.addAll(this.squareBlock[this.base*j+i].updateCount(cell));

		return cells;
	}



	// Functions to update the possibility each cell

	public void updateAllCells(int pass)
	{
		// Update all the cells
		int i,j;
		Cell cell;
		for (i=0; i<this.b_s; i++) // row number
		{
			for (j=0; j<this.b_s; j++) // col number
			{
				cell = this.grid[i][j];
				this.updateCell(cell, pass); // (row, col)
			}
		}
	}


	public void updateCell(Cell cell, int pass)
	{
		// (x,y) = (row, col)
		// update the cell with a different behavior according to pass

		if (cell.getIsSet())
			return;

		int x = cell.getX();
		int y = cell.getY();
		int temp;
		if (pass == 1 || pass > 3)
		{
			Set<Integer> impossible = new HashSet<>(); // contains all the other numbers in the biqSquare, row and column
			int i = (y/this.base)%this.base;
			int j = (x/this.base)%this.base;
			impossible.addAll(this.columnBlock[y].getValues());
			impossible.addAll(this.rowBlock[x].getValues());
			impossible.addAll(squareBlock[this.base*j+i].getValues());


			Set<Integer> possible = cell.getPossible(); // all the possible values for the cell

			possible.removeAll(impossible); // removes all the values of the union from the hypothesis
			cell.setPossible(possible); // set the new hypothesis

			if (possible.size() == 1) // the square (x,y) has only one possibility
			{
				//System.out.println("Cell "+cell.getId()+" possible="+possible);
				cell.setFinalValue(cell.toArray()[0]);
				this.addToFifo(cell);
			}

		}

		if (pass >= 2)
		{
			ArrayList<Cell> cells = this.updateBlocksCounts(cell);
			if (pass >= 3)
			{
				for(Cell c: cells)
					this.addToFifo(c); // add the cell to update
			}
		}
	}


	public void addToFifo(Cell cell)
	{
		// Add the Cell to the fifo_update if it is not in yet
		if (!this.fifo_update.contains(cell))
			this.fifo_update.addLast(cell); // add the index of the square at the END of the queue
	}


	public boolean iterate()
	{
		// Iterate over the Cells in fifo_update
		// returns true if there is at least one Cell
		// else returns false

		//System.out.println("There are currently: "+this.fifo_update.size()+" elements in the fifo");
		if (this.fifo_update.size() == 0)
		{
			System.out.println("Final Output !");
			System.out.println("There are now "+this.empty_cells+" empty cells in the grid");
			return false; // no more elements to update
		}

		this.empty_cells -= 1; // decrement the empty_cells counter

		Cell elt = this.fifo_update.removeFirst(); // get the first element of the fifo
		int x = elt.getX();
		int y = elt.getY();

		int value = elt.getFinalValue(); // get the final value
		elt.setValue(value); // set it as the value of the cell

		// Update every block with the new value
		this.updateBlocksValues(x, y, value);


		// Remove the cell from all of its Block's hyp_count
		int i = (y/this.base)%this.base;
		int j = (x/this.base)%this.base;

		this.columnBlock[y].removeCountOfCell(elt);
		this.rowBlock[x].removeCountOfCell(elt);
		this.squareBlock[j*this.base+i].removeCountOfCell(elt);


		// compute o_i and o_j
		int oj = (y/this.base)%this.base;
		int oi = (x/this.base)%this.base;

		// update the Blocks attached to it
		// loop on all the cells from the column, row and square around and update them

		for (int a = 0; a<this.b_s; a++)
		{
			j = a/this.base;
			i = a%this.base;

			this.updateCell(this.grid[x][a], 4); // update every square in the row x
			this.updateCell(this.grid[a][y], 4); // update every square in the column y
			this.updateCell(this.grid[oj+j][oi+i], 4); // updates every cell of the big square
		}

		return true; // one element has been updated
	}



	public String getStringRepr()
	{
		// returns a string containing the representation of the grid
		// TODO !
	}



	@Override
	public String toString()
	{
		// TODO FIXME : adapt this function to the grid size
		String out="";
		int i,j, value;
		for (i=0; i<this.b_s; i++) // row number
		{
			if (i%this.base == 0)
				out += "-------------\n"; // FIXME : does not work in case the base is NOT 3

			for (j=0; j<this.b_s; j++) // col number
			{
				if (j%this.base == 0)
					out += "|";

				if (this.grid[i][j].getIsSet())
				{
					value = this.grid[i][j].getValue();
					if (this.default_grid)
						out += String.valueOf(value+1);
					else
						out += String.valueOf(value);
				}
				else
				{
					out += " ";
				}
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


	// DEBUG function

	public void probe(String s)
	{
		int id, x, y;
		if (!this.isNumeric(s))
			return;

		id = Integer.parseInt(s);
		x = id/this.b_s;
		y = id%this.b_s;

		Cell c = this.grid[x][y];

		System.out.println("The cell "+id+" ("+y+","+x+") has :");
		System.out.println("Value = "+c.getValue());
		System.out.println("Possible = "+c.getPossible());


		int i = (y/this.base)%this.base;
		int j = (x/this.base)%this.base;

		ArrayList<Set<Cell>> alsc;

		System.out.println("column "+y+" hyp_count");
		alsc = this.columnBlock[y].getHypCount();
		for(int a=0; a<this.b_s; a++)
			System.out.println(a+" "+alsc.get(a));

		System.out.println("row "+x+" hyp_count");
		alsc = this.rowBlock[x].getHypCount();
		for(int a=0; a<this.b_s; a++)
			System.out.println(a+" "+alsc.get(a));

		System.out.println("square "+(j*this.base+i)+" hyp_count");
		alsc = this.squareBlock[j*this.base+i].getHypCount();
		for(int a=0; a<this.b_s; a++)
			System.out.println(a+" "+alsc.get(a));

	}

}

