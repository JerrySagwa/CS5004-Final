/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/

package model;

import java.util.function.BiPredicate;
import java.util.stream.IntStream;

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
    private final BiPredicate<Integer, Integer> isSafePredicate;



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
        this.isSafePredicate = (row, col) -> {
            // Check column
            for (int i = 0; i < row; i++) {
                if (board[i][col]) return false;
            }
            // Check upper diagonal (left)
            for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
                if (board[i][j]) return false;
            }
            // Check upper diagonal (right)
            for (int i = row, j = col; i >= 0 && j < size; i--, j++) {
                if (board[i][j]) return false;
            }
            return true;
        };
    }

    @Override
    public boolean isShowHelp() {
        return showHelp;
    }

    @Override
    public void toggleHelp() {
        this.showHelp = !this.showHelp;
    }

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
        // Use streams to check if the board is a valid solution
        record Position(int row, int col) {}
        int queenCount = IntStream.range(0, size)
                // Generate all board positions
                .boxed()
                .flatMap(row -> IntStream.range(0, size).mapToObj(col -> new Position(row, col)))
                // Filter: Keep positions with queens
                .filter(pos -> board[pos.row][pos.col])
                // Map: Count queens and check safety
                .mapToInt(pos -> isSafePredicate.test(pos.row, pos.col) ? 1 : 0)
                // Fold (reduce): Sum valid queens
                .sum();

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
        
        // mark each row and col
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j]) {
                    for (int k = 0; k < size; k++) {
                        threatenedSquares[i][k] = true;
                        threatenedSquares[k][j] = true;
                    }
                    
                    // mark diagonal
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