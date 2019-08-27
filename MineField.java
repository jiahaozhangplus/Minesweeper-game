

// Name:Jiahao Zhang
// USC NetID:8392593422
// CS 455 PA3
// Spring 2019


/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
import java.util.Random;
public class MineField {
   
   // <put instance variables here>
   private boolean[][] mineData; //create a new 2d-array of boolean data
   private int numMines;// the number of Mines at the initial
   
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
    * @param mineData  the data for the mines; must have at least one row and one col.
    */
   public MineField(boolean[][] mineData) {
	   int num = 0;
	   this.mineData = new boolean[mineData.length][mineData[0].length];
	   for(int x = 0; x < numRows(); x++) {
		   for(int y = 0; y<numCols();y++) {
			   this.mineData[x][y] = mineData[x][y];
			   if(hasMine(x, y) == true){
					  num++;
			   }
		   }
	   }
	   numMines = num;
   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
	   mineData = new boolean [numRows][numCols];
	   this.numMines = numMines;
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col)
    */
   public void populateMineField(int row, int col) {
	   assert inRange(row, col); 
	   Random t = new Random();
	   resetEmpty(); //after reset, randomly display new mine location
	   for(int i = 0; i < numMines; i ++) {
		   int x = t.nextInt(numRows());
		   int y = t.nextInt(numCols());
		   if(mineData[x][y] == false && !(x==row && y==col)) {
			   mineData[x][y] = true;
		   }
		   else {
			   i--;
		   }	   
	   }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state the minefield is in at the beginning of a game.
    */
   public void resetEmpty() {
	   mineData = new boolean [numRows()][numCols()];
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
	  assert inRange(row, col); 
	  int count = 0;  //using loop to record the number of Mines
	  for(int x = Math.max(row -1, 0); x <= Math.min(numRows() - 1, row + 1);x++) {
		  for(int y = Math.max(col -1, 0);y <= Math.min(numCols() - 1, col+1);y++){ 
			  if(hasMine(x, y) == true && !(x==row && y==col)){
				  count++;
			  }
		  }
	  }
      return count;       
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
	   if(row >=0 && row < numRows() ) {
		   if(col >=0 && col < numCols()) {
			   return true;
		   }
	   }
       return false;
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return mineData.length;      
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return mineData[0].length;      
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
	   assert inRange(row, col);
	   return mineData[row][col];       
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return numMines;       
   }
         
}

