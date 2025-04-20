/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/

package model;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * model : all data of the chessboard
 * 1. 
 */
public class ChessBoard implements BoardModel {
    private int size;
    private boolean[][] board;
    private boolean[][] threatenedSquares;
    private boolean showHelp;
    private SolutionTree solutionTree;


    public ChessBoard() {
        this(8); // Default to 8-Queens
    }

    public ChessBoard(int size) {
        this.size = size;
        this.board = new boolean[size][size];
        this.threatenedSquares = new boolean[size][size];
        this.showHelp = false;
        this.solutionTree = new SolutionTree(size);
        this.solutionTree.buildTree(new boolean[size][size]);
    }

    @Override
    public boolean isShowHelp() {
        return showHelp;
    }

    @Override
    public void toggleHelp() {
        this.showHelp = !this.showHelp;
    }

    private final BiPredicate<Integer, Integer> isSafePredicate = (row, col) -> {
        // Check row
        for (int i = 0; i < size; i++) {
            if (i != col && board[row][i]) return false;
        }

        // Check column
        for (int i = 0; i < size; i++) {
            if (i != row && board[i][col]) return false;
        }

        // Check diagonals (4 directional)
        Predicate<int[]> checkDiagonal = (int[] coords) -> {
            int i = coords[0] + coords[2], j = coords[1] + coords[3];
            while (i >= 0 && j >= 0 && i < size && j < size) {
                if (board[i][j]) return false;
                i += coords[2];
                j += coords[3];
            }
            return true;
        };

        // Check all four diagonals
        return checkDiagonal.test(new int[]{row, col, -1, -1}) &&  // upper left
               checkDiagonal.test(new int[]{row, col, -1, 1}) &&   // upper right
               checkDiagonal.test(new int[]{row, col, 1, -1}) &&   // lower left
               checkDiagonal.test(new int[]{row, col, 1, 1});      // lower right
    };

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean[][] getBoardState() {
        return board;
    }

    @Override
    public boolean[][] getThreatenedSquares() {
        updateThreatenedSquares();
        return threatenedSquares;
    }

    @Override
    public void placeQueen(int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = true;
            updateThreatenedSquares();
        }
    }

    @Override
    public void removeQueen(int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = false;
            updateThreatenedSquares();
        }
    }

    @Override
    public void clearBoard() {
        board = new boolean[size][size];
        threatenedSquares = new boolean[size][size];
    }

    @Override
    public boolean isSolved() {
        int queenCount = 0;
        for (boolean[] row : board){
            for (boolean e : row) {
                if (e) {
                    queenCount++;
                }
            }
        }
        return queenCount == size;
    }

    @Override
    public int[] getNextValidMove() {
        return solutionTree.findNextMove(board);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
        this.board = new boolean[size][size];
        this.threatenedSquares = new boolean[size][size];
        this.solutionTree = new SolutionTree(size);
        this.solutionTree.buildTree(new boolean[size][size]);
    }

    @Override
    public void resizeBoard(int newSize) {
        setSize(newSize);
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private void updateThreatenedSquares() {
        threatenedSquares = new boolean[size][size];
        
        // 检查每一行和每一列
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j]) {
                    // 标记行和列
                    for (int k = 0; k < size; k++) {
                        threatenedSquares[i][k] = true;
                        threatenedSquares[k][j] = true;
                    }
                    
                    // 标记对角线
                    markDiagonal(i, j, -1, -1); // 左上
                    markDiagonal(i, j, -1, 1);  // 右上
                    markDiagonal(i, j, 1, -1);  // 左下
                    markDiagonal(i, j, 1, 1);   // 右下
                }
            }
        }
    }

    private void markDiagonal(int row, int col, int rowStep, int colStep) {
        int r = row + rowStep;
        int c = col + colStep;
        while (r >= 0 && r < size && c >= 0 && c < size) {
            threatenedSquares[r][c] = true;
            r += rowStep;
            c += colStep;
        }
    }
} 