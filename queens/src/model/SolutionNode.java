/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/

package model;

import java.util.ArrayList;
import java.util.List;

// Node in the Solution Tree
// children : next node for a valid path
// leaf node : last position of the queen
public class SolutionNode {
    private final int row;
    private final int col;
    private final List<SolutionNode> children;
    private final SolutionNode parent;
    private boolean isSolution;

    public SolutionNode(int row, int col, SolutionNode parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.isSolution = false;
    }

    public SolutionNode() {
        this(-1, -1, null); // Root node
    }

    public void addChild(SolutionNode child) {
        children.add(child);
    }

    public List<SolutionNode> getChildren() {
        return children;
    }

    public SolutionNode getParent() {
        return parent;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void markAsSolution() {
        isSolution = true;
    }

    public boolean isSolution() {
        return isSolution;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public List<int[]> getPath() {
        List<int[]> path = new ArrayList<>();
        SolutionNode current = this;
        while (!current.isRoot()) {
            path.add(0, new int[]{current.getRow(), current.getCol()});
            current = current.getParent();
        }
        return path;
    }
} 