/******
 Name: Zhexing (Jerry) Sun
 Assignment: Final
 Date: 04/17/2025
 Notes:  N-Queens Visualization
 ******/
import model.BoardModel;
import model.ChessBoard;
import view.ChessBoardView;
import controller.ChessController;

public class Main {
    public static void main(String[] args) {
        // Model
        BoardModel model = new ChessBoard();
        
        // View
        ChessBoardView view = new ChessBoardView(8);
        
        // Controller
        new ChessController(model, view);
        
        view.setVisible(true);

    }
} 