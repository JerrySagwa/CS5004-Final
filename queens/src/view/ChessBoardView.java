package view;

import controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardView extends JFrame implements ChessView {
    private int size;
    private static final int CELL_SIZE = 60;
    private JPanel[][] cells;
    private JPanel mainPanel;
    private JPanel boardPanel;
    private JPanel controlPanel;
    private final JButton hintButton;
    private final JButton resetButton;
    private final JButton configButton;
    private final JButton helpButton;

    private final List<CellClickListener> cellClickListeners = new ArrayList<>();
    private final List<HelpListener> helpListeners = new ArrayList<>();
    private final List<HintListener> hintListeners = new ArrayList<>();
    private final List<ResetListener> resetListeners = new ArrayList<>();
    private final List<ConfigListener> configListeners = new ArrayList<>();


    public ChessBoardView(int size) {
        this.size = size;
        this.cells = new JPanel[size][size];

        setTitle("N Queens Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Create control panel
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add buttons to control panel
        resetButton = new JButton("Reset");
        hintButton = new JButton("Hint");
        configButton = new JButton("Configure");
        helpButton = new JButton("Help");

        resetButton.addActionListener(e -> resetListeners.forEach(ResetListener::onReset));

        hintButton.addActionListener(e -> hintListeners.forEach(HintListener::onHint));

        configButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this,
                "Enter the size of the board (N for N-Queens):",
                "Board Configuration",
                JOptionPane.QUESTION_MESSAGE);

            if (input != null) {
                try {
                    int newSize = Integer.parseInt(input);
                    if (newSize >= 4 && newSize <= 12) {
                        configListeners.forEach(listener -> listener.onConfig(newSize));
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Size must be between 4 and 12.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter a valid number.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        helpButton.addActionListener(e -> {
            helpListeners.forEach(HelpListener::onHelp);
        });

        // Add some spacing between buttons
        controlPanel.add(resetButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(hintButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(helpButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(configButton);
        controlPanel.add(Box.createVerticalGlue());

        // Create board panel
        boardPanel = new JPanel(new GridLayout(size, size));
        initializeBoard();

        // Add panels to main panel
        mainPanel.add(controlPanel, BorderLayout.WEST);
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void initializeBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cell.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                final int finalRow = row;
                final int finalCol = col;

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cellClickListeners.forEach(listener -> listener.onCellClick(finalRow, finalCol));
                    }
                });

                cells[row][col] = cell;
                boardPanel.add(cell);
            }
        }
        String helpMessage = "N-Queens Puzzle Help:\n\n" +
                "1. Place N queens on an N×N chessboard so that no two queens threaten each other.\n" +
                "2. Queens can move any number of squares vertically, horizontally, or diagonally.\n" +
                "3. Click on a cell to place or remove a queen.\n" +
                "4. Use the Hint button to see a suggested move.\n" +
                "5. Use the Help button to toggle threat visualization.\n" +
                "6. Use the Reset button to clear the board.\n" +
                "7. Use the Configure button to change the board size (4-12).\n\n" +
                "The puzzle is solved when all queens are placed without threatening each other.";

        JOptionPane.showMessageDialog(this, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showInvalidMoveMessage() {
        JOptionPane.showMessageDialog(this, "Invalid move! Queens cannot attack each other.");
    }

    private void updateCell(int row, int col, boolean hasQueen, boolean threaten, boolean showHelp) {
        JPanel cell = cells[row][col];
        cell.removeAll();

        // Set background color based on whether the square is threatened (when hints are shown)
        if (showHelp && threaten) {
            cell.setBackground(new Color(255, 200, 200)); // Light red for threatened squares
        } else {
            cell.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
        }

        if (hasQueen) {
            JLabel queenLabel = new JLabel("♕", SwingConstants.CENTER);
            queenLabel.setFont(new Font("Arial", Font.BOLD, 30));
            cell.add(queenLabel);
        }

        cell.revalidate();
        cell.repaint();
    }

    @Override
    public void highlightValidMove(int row, int col) {
        JPanel cell = cells[row][col];
        cell.setBackground(new Color(200, 255, 200)); // Light green for valid move
        cell.revalidate();
        cell.repaint();
    }

    @Override
    public void showNoValidMovesMessage() {
        JOptionPane.showMessageDialog(this, "No valid moves available! The board will be reset.");
    }

    @Override
    public void showCongratulationsMessage() {
        JOptionPane.showMessageDialog(this, 
            "Congratulations! You've solved the N Queens Puzzle!",
            "Puzzle Solved", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
        this.cells = new JPanel[size][size];
        
        // Remove old board panel
        mainPanel.remove(boardPanel);
        
        // Create new board panel
        boardPanel = new JPanel(new GridLayout(size, size));
        initializeBoard();
        
        // Add new board panel
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        
        // Update window size
        pack();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    @Override
    public void updateBoard(boolean[][] boardState, boolean[][] threatenedSquares, boolean showHelp) {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                updateCell(i, j, boardState[i][j], threatenedSquares[i][j], showHelp);
            }
        }
    }

    // 实现事件监听器注册方法
    @Override
    public void addCellClickListener(CellClickListener listener) {
        cellClickListeners.add(listener);
    }

    @Override
    public void addResetListener(ResetListener listener) {
        resetListeners.add(listener);
    }

    @Override
    public void addHintListener(HintListener listener) {
        hintListeners.add(listener);
    }

    @Override
    public void addConfigListener(ConfigListener listener) {
        configListeners.add(listener);
    }

    @Override
    public void addHelpListener(HelpListener listener) {
        helpListeners.add(listener);
    }
}