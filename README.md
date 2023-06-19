# Naive_Sudoku
A (very) naive Sudoku solver to help complete simple grids


## Setup

Compile with the command  
>$ javac *.java && javac . -d *.java

Launch with the command
>$ java Naive_Sudoku.Sudoku

## Usage

When launched, the program will ask for a string representation of the Sudoku grid you want solved.  
You must enter the state (value or space) of each square, from left to right, top to bottom  

Each time there is a space (or blank square) in your grid, you can input it in two ways :  
+ Simply type a space
+ Type the number of spaces between parenthesis (for 5 spaces => (5) )

You can omit every blank squares after the last digit in the grid, the program will automtacially count the remaining squares as blanks


## Output

If the solver can solve the grid, then the grid should be printed completed. That's your result !  
Else, the solver will try to solve all it can before printing the last step it obtained (which could be the same as the one entered)


## Future improvements

+ Make it less naive by adding more deduction rules
+ Add some parameters to interact with the solver and not getting directly the solution (maybe get hints)
+ Add parameter to specify the size of the board (3x3 is current default, add support for 4x4 hexa grids)
+ Prettify the outputs
