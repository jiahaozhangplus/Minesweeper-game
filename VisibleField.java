// Name:
// USC NetID:
// CS 455 PA3
// Spring 2019


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield), Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // Covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // Uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   
  
   // <put instance variables here>
   private MineField mineField; //create a new object of MineField
   private int[][] uncoverStatus;// use integer to represent the uncoverstatus

   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
	   this.mineField = mineField; // initialization
	   uncoverStatus = new int [mineField.numRows()][mineField.numCols()];
	   for(int x = 0; x < mineField.numRows(); x++) {
		   for(int y = 0; y<mineField.numCols();y++) {
			   uncoverStatus[x][y] = COVERED;
				  }
		   }
   }
   
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
	   uncoverStatus = new int[mineField.numRows()][mineField.numCols()];
	   for(int x = 0; x < mineField.numRows(); x++) {
		   for(int y = 0; y<mineField.numCols();y++) {
			   uncoverStatus[x][y] = COVERED;
				  }
		   }
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return mineField;       
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
	  assert  getMineField().inRange(row, col);
	  return uncoverStatus[row][col]; 
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  So the value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
	   int num = getMineField().numMines(); //using the date from MineField
	   for(int x = 0; x < getMineField().numRows(); x++) {
		   for(int y = 0; y<getMineField().numCols();y++) {
			   if(uncoverStatus[x][y] == MINE_GUESS){
					  num--;
				  }
		   }
	   }
	   return num;
   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
	   assert getMineField().inRange(row, col);
	   // will loop the three differnt states
       if(uncoverStatus[row][col] == COVERED){
    	  uncoverStatus[row][col] = MINE_GUESS;
       }
       else if(uncoverStatus[row][col] == MINE_GUESS){
     	  uncoverStatus[row][col] = QUESTION;
       }
       else if(uncoverStatus[row][col] == QUESTION){
     	  uncoverStatus[row][col] = COVERED;
        }
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
	   assert getMineField().inRange(row, col);
	   if(mineField.hasMine(row, col)) {
		   uncoverStatus[row][col] = EXPLODED_MINE;	 // game will be over
		   for(int x = 0; x < mineField.numRows(); x++) {
			   for(int y = 0; y<mineField.numCols();y++) {
				   if(getMineField().hasMine(x, y) && (uncoverStatus[x][y] == COVERED || uncoverStatus[x][y] == QUESTION)) {
						uncoverStatus[x][y] = MINE;
					  }
				   if(!getMineField().hasMine(x, y) && uncoverStatus[x][y] == MINE_GUESS) {
						uncoverStatus[x][y] = INCORRECT_GUESS;
					  }
				}
			}			   
		   return false;  
	   }else {
		   dfs(row, col); // running the recursive function
		   if(isGameOver()) { // change the status of square
			   for(int x = 0; x < mineField.numRows(); x++) {
				   for(int y = 0; y<mineField.numCols();y++) {
					   if(uncoverStatus[x][y] <0) {
							  uncoverStatus[x][y] = MINE_GUESS;
						  } 
					}
				}			   
		   }
		   return true;
	   }
   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {
	   boolean gameIsOver = false; // game over has 2 state, one win one lose
	   int count = 0;
	   outerloop: // using a loop to decide whether has exploded
	   for(int x = 0; x < mineField.numRows(); x++) {
		   for(int y = 0; y<mineField.numCols();y++) {
			   if(uncoverStatus[x][y] == EXPLODED_MINE) {
				   gameIsOver = true;
				   break outerloop;
			   }
			   if(uncoverStatus[x][y] < 0) {
				   count++;
			   }
			}
		}
	   if(count <= getMineField().numMines() && !gameIsOver) { // the situation that let player wins
		   gameIsOver = true;
	   }
      return gameIsOver;  
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
	   assert getMineField().inRange(row, col);
	   if(uncoverStatus[row][col] < 0) {
	        return false;  
	   }else {
		    return true;
	   }
   }
   
 
   // the private methods that execute dfs in recursive function
   private void dfs(int row, int col) {
		 if(!mineField.hasMine(row, col) && !isUncovered(row, col) && uncoverStatus[row][col]!= MINE_GUESS) {
		   uncoverStatus[row][col] = mineField.numAdjacentMines(row, col);
		   if(mineField.numAdjacentMines(row, col) == 0) {
			   if(row > 0 ) {
				   dfs(row -1, col);
			   }
			   if(row < mineField.numRows() - 1) {
				   dfs(row +1, col);
			   }
			   if(col > 0) {
				   dfs(row, col -1);
			   }
			   if(col < mineField.numCols() - 1) {
				   dfs(row, col +1);
			   }
			   if(row > 0 && col > 0) {
				   dfs(row -1, col -1);
			   }
			   if(row > 0 && col < mineField.numCols() - 1) {
				   dfs(row -1, col +1);
			   }
			   if(row < mineField.numRows() - 1 && col > 0) {
				   dfs(row +1, col -1);
			   }
			   if(row < mineField.numRows() - 1 && col < mineField.numCols() - 1) {
				   dfs(row +1, col +1);
			   }
		    }
		 }
	 }
}
