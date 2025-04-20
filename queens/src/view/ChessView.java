/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/
package view;

import controller.*;

/**
 * responsibilities:
 * 1. show / update board
 * 2. add listeners
 * 3. show message
 */
public interface ChessView {
    // show messages
    void showInvalidMoveMessage();
    void showNoValidMovesMessage();
    void showCongratulationsMessage();
    void setVisible(boolean visible);
    
    // update view
    void updateBoard(boolean[][] boardState, boolean[][] threatenedSquares, boolean showHelp);

    void highlightValidMove(int row, int col);
    void setSize(int size);
    
    // event listener register
    void addCellClickListener(CellClickListener listener);
    void addResetListener(ResetListener listener);
    void addHintListener(HintListener listener);
    void addConfigListener(ConfigListener listener);
    void addHelpListener(HelpListener listener);

}