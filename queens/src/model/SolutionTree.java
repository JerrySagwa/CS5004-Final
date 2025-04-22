/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionTree {
    private final SolutionNode root;
    private final int size;
    private List<SolutionNode> solutions;

    public SolutionTree(int size) {
        this.size = size;
        this.root = new SolutionNode();
        this.solutions = new ArrayList<>();
    }

    // using backtracking algorithm to build solution tree
    public void buildTree(boolean[][] board) {
        buildTreeRecursive(root, board, 0);
    }

    private void buildTreeRecursive(SolutionNode node, boolean[][] board, int currentRow) {
        if (currentRow == size) {
            // Found a solution
            node.markAsSolution();
            solutions.add(node);
            return;
        }

        // Try placing a queen in each column of the current row
        for (int col = 0; col < size; col++) {
            if (isSafe(board, currentRow, col)) {
                // Create a new node for this position
                SolutionNode child = new SolutionNode(currentRow, col, node);
                node.addChild(child);

                // Place the queen and continue building the tree
                board[currentRow][col] = true;
                buildTreeRecursive(child, board, currentRow + 1);
                board[currentRow][col] = false;
            }
        }
    }

    private boolean isSafe(boolean[][] board, int row, int col) {
        // Check column
        for (int i = 0; i < row; i++) {
            if (board[i][col]) return false;
        }

        // Check upper diagonal
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j]) return false;
        }

        // Check upper diagonal (right)
        for (int i = row, j = col; i >= 0 && j < size; i--, j++) {
            if (board[i][j]) return false;
        }

        return true;
    }

    public int[] findNextMove(boolean[][] currentBoard) {
        // Find the first row without a queen
        int targetRow = -1;
        for (int row = 0; row < size; row++) {
            boolean hasQueen = false;
            for (int col = 0; col < size; col++) {
                if (currentBoard[row][col]) {
                    hasQueen = true;
                    break;
                }
            }
            if (!hasQueen) {
                targetRow = row;
                break;
            }
        }

        if (targetRow == -1) {
            return null; // All rows have queens
        }

        // Check each solution for a matching path
        for (SolutionNode solution : solutions) {
            List<int[]> path = solution.getPath();
            boolean isValidPath = true;

            // Check if the path matches the board state for rows 0 to targetRow-1
            for (int row = 0; row < targetRow; row++) {
                boolean queenInRow = false;
                int boardCol = -1;

                // Find if the board has a queen in this row
                for (int col = 0; col < size; col++) {
                    if (currentBoard[row][col]) {
                        queenInRow = true;
                        boardCol = col;
                        break;
                    }
                }

                // If the board has a queen, check if it’s in the path
                if (queenInRow) {
                    boolean foundInPath = false;
                    for (int[] pos : path) {
                        if (pos[0] == row && pos[1] == boardCol) {
                            foundInPath = true;
                            break;
                        }
                    }
                    if (!foundInPath) {
                        isValidPath = false;
                        break;
                    }
                } else {
                    // If no queen in the board row, ensure no queen in the path for this row
                    for (int[] pos : path) {
                        if (pos[0] == row) {
                            isValidPath = false;
                            break;
                        }
                    }
                    if (!isValidPath) {
                        break;
                    }
                }
            }

            // If the path matches, return the position for targetRow
            if (isValidPath) {
                for (int[] pos : path) {
                    if (pos[0] == targetRow) {
                        return pos;
                    }
                }
            }
        }

        return null; // No valid move found
    }

}