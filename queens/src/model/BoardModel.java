/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/

package model;

public interface BoardModel {
    /**
     * Places a queen at the specified position
     * @param row the row index (0 to size-1)
     * @param col the column index (0 to size-1)
     */
    void placeQueen(int row, int col);

    /**
     * Removes a queen from the specified position
     * @param row the row index (0 to size-1)
     * @param col the column index (0 to size-1)
     */
    void removeQueen(int row, int col);

    /**
     * Gets the current state of the board
     * @return a 2D array representing the board state
     */
    boolean[][] getBoardState();

    /**
     * Clears the board
     */
    void clearBoard();

    /**
     * Gets all threatened squares on the board
     * @return a 2D array where true indicates a threatened square
     */
    boolean[][] getThreatenedSquares();

    /**
     * Gets the next valid move for the current board state
     * @return an array containing [row, col] of the next valid move, or null if no valid moves exist
     */
    int[] getNextValidMove();

    /**
     * Checks if the puzzle is solved (N queens are placed correctly)
     * @return true if the puzzle is solved, false otherwise
     */
    boolean isSolved();

    /**
     * Gets the size of the board
     * @return the size of the board (N for N-Queens)
     */
    int getSize();

    /**
     * Sets the size of the board
     * @param size the new size of the board
     */
    void setSize(int size);

    void resizeBoard(int newSize);

    boolean isShowHelp();

    void toggleHelp();
}