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
    // fix : using high order function
    public int[] findNextMove(boolean[][] currentBoard) {
        // Find the target row that needs a queen
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

        // Use streams to find the next valid move
        int finalTargetRow = targetRow;
        return solutions.stream()
                // Map: Transform each solution to its path
                .map(SolutionNode::getPath)
                // Filter: Keep solutions that match the current board state up to targetRow
                .filter(path -> {
                    for (int row = 0; row < finalTargetRow; row++) {
                        boolean foundMatch = false;
                        int finalRow = row;
                        for (int col = 0; col < size; col++) {
                            if (currentBoard[row][col]) {
                                // Check if this position is in the path
                                int finalCol = col;
                                boolean inPath = path.stream()
                                        .anyMatch(pos -> pos[0] == finalRow && pos[1] == finalCol);
                                if (!inPath) {
                                    return false;
                                }
                                foundMatch = true;
                                break;
                            }
                        }
                        if (!foundMatch && path.stream().anyMatch(pos -> pos[0] == finalRow)) {
                            return false;
                        }
                    }
                    return true;
                })
                // Map: Extract the position for the target row
                .flatMap(path -> path.stream().filter(pos -> pos[0] == finalTargetRow))
                // Fold (reduce): Take the first valid move
                .findFirst()
                .orElse(null);
    }
} 