# Naive_Sudoku
A (very) naive Sudoku solver to help complete simple grids


## Setup

Compile with the command  
>$ javac *.java && javac -d . *.java

Launch with the command
>$ java Naive_Sudoku.Sudoku

## Usage

When launched, the program will ask for a string representation of the Sudoku grid you want solved.  
You must enter the state (value or space) of each cell, from left to right, top to bottom  

Each time there is a one or more blank cell in your grid, you can input it in two ways :  
+ Simply type a space for each blank cell
+ Type the number of blank celle between parenthesis (for 5 spaces => (5) )

You can omit every blank cell after the last digit in the grid, the program will automatically count the remaining squares as blanks


## Output

If the solver can solve the grid, then the grid should be printed completed. That's your result !  
Else, the solver will try to solve all it can before printing the last step it obtained (which can be the same as the one entered)


## Examples

You can input one of the example below to get a taste of what the program is capable:
+ 12345678
+ 12(10)3(11)3
+ 12 6(6)453(11)36
+ 3 461(3)57 8(3)3 6(3)9 34  8 7(3)51  2 7 5 4 6(3)91  248 352  7(6)9  1 6  928

(still unsolved)
+ (3)7(4)44 3(9)6  9 2 8(5)1  6(4)87(4)1 9(3)3(3)5(4)1 6(10)4 2 5


## Some technical terms

+ Cell: small square of the grid. It can be empty or set with a value
+ Block: either a big square or a row or a column. A block can only contain one of each digit
+ Hypothesis: one or more value that a cell can take

## Solving 'rules'

For the time being, there are only two rules implemented:
+ The first is a rule stating that if in a block, a cell has only one hypothesis ov value k, then the cell value must be set to k (See example 1)
+ The second rule states that if a cell is the only cell in a block having the hypothesis k, then its value must be set to k (see examples 2&3)


## Future improvements

+ Make it less naive by adding more deduction rules
+ Add some parameters to interact with the solver and not getting directly the solution (maybe get hints)
+ Add parameter to specify the size of the board (3x3 is current default, add support for 4x4 hexa grids)
+ Prettify the outputs


## Thoughts

+ Is the 1st 'rule' (setting a cell's value if it's the only one left in a block)  a consequence of the new 'rule' (setting a cell value when it is the only one in a block having this hypothesis) ?
