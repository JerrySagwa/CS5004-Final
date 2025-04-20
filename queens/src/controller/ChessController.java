/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/
package controller;

import model.BoardModel;
import view.ChessView;

/**
 * Controller
 * 1.Handle various event
 * 2.interface segregation : implement interface according to the required functionality
 */
public class ChessController implements CellClickListener, ResetListener, HintListener, ConfigListener, HelpListener {
    private final BoardModel model;
    private final ChessView view;

    public ChessController(BoardModel model, ChessView view) {
        this.model = model;
        this.view = view;
        view.addCellClickListener(this);
        view.addConfigListener(this);
        view.addHelpListener(this);
        view.addHintListener(this);
        view.addResetListener(this);
    }

    @Override
    public void onCellClick(int row, int col) {
        if (model.getBoardState()[row][col]) {
            model.removeQueen(row, col);
        } else {
            if (model.getThreatenedSquares()[row][col]) {
                view.showInvalidMoveMessage();
                return;
            }
            model.placeQueen(row, col);
        }
        
        view.updateBoard(model.getBoardState(), model.getThreatenedSquares(), model.isShowHelp());

        if (model.isSolved()) {
            view.showCongratulationsMessage();
        }
    }

    @Override
    public void onReset() {
        model.clearBoard();
        view.updateBoard(model.getBoardState(), model.getThreatenedSquares(), model.isShowHelp());
    }

    @Override
    public void onHint() {
        int[] nextMove = model.getNextValidMove();
        if (nextMove != null) {
            view.highlightValidMove(nextMove[0], nextMove[1]);
        } else {
            view.showNoValidMovesMessage();
            onReset();
        }
    }

    @Override
    public void onConfig(int size) {
        model.setSize(size);
        view.setSize(size);
        view.updateBoard(model.getBoardState(), model.getThreatenedSquares(), model.isShowHelp());
    }

    @Override
    public void onHelp() {
        model.toggleHelp();
        view.updateBoard(model.getBoardState(), model.getThreatenedSquares(), model.isShowHelp());
    }
}