package Naive_Sudoku;
import java.util.Scanner;

public class Sudoku
{
	public static void main(String[] args)
	{
		// Create a grid from imput and then solve from there
		Scanner user_input = new Scanner(System.in); // read from user
		System.out.println("Enter a sudoku-describing string");
		String s_grid = user_input.nextLine();
		Grid sudoku = new Grid(s_grid);
		System.out.println(sudoku);

		sudoku.updateAllSquares();

		while (sudoku.iterate())
		{
		}
		System.out.println(sudoku);
		/*
		while (sudoku.getUpdatableSize() != 0)
		{
			Square elt = sudoku.getFirstElement();
			int x = elt.getX();
			int y = elt.getY();

			sudoku.updateAroundSquare(x,y);
		}
		*/
	}
}
