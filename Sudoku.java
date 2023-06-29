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
		Grid sudoku = new Grid(s_grid, 3); // TODO : add an argument to chose the grid size
		System.out.println(sudoku);


		while (sudoku.iterate())
		{
		}
		System.out.println(sudoku);


		// Probing code for debug
		System.out.println("DEBUG mode : type 'exit' to quit");
		s_grid = "";
		while (!s_grid.equals("exit"))
		{
			s_grid = user_input.nextLine();
			sudoku.probe(s_grid);
		}

	}
}
