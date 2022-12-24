package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class chessGame {

    public static String[][] board;
    public static boolean whiteToMove;
    public static ArrayList<Move> movelist = new ArrayList<>();

    public chessGame() {
        board = new String[][]{
                {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR",},
                {"bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"--", "--", "--", "--", "--", "--", "--", "--",},
                {"wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP",},
                {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR",}
        };

        play();
    }

    private void play() {
        ArrayList<Integer> selected_square = new ArrayList<>();
        ArrayList<ArrayList<Integer>> clicks = new ArrayList<>();

        GUI.frame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {

                if (me.getButton() == MouseEvent.BUTTON1) {
                    System.out.println(me); // debug

                    int col = Math.floorDiv(me.getX(), 96);
                    int row = Math.floorDiv(me.getY(), 96);
                    System.out.println("Col: " + col + "  Row: " + row); // debug

                    if ((!selected_square.isEmpty()) && selected_square.get(0) == row && selected_square.get(1) == col){
                        selected_square.clear();
                        clicks.clear();
                    }
                    else {
                        selected_square.clear();
                        selected_square.add(row); selected_square.add(col);

                        clicks.add(new ArrayList<>(selected_square));
                    }

                    if (clicks.size() == 2){
                        Move move = new Move(clicks.get(0), clicks.get(1));
                        System.out.println(move.getChessNotation());
                        Move.makeMove(move);
                        selected_square.clear();
                        clicks.clear();

                        for (int i = 0; i < 8; i++){
                            for (int j = 0; j < 8; j++){
                                System.out.print(board[i][j] + " ");
                            }
                            System.out.println();
                        }

                        for (Component component : GUI.panel.getComponents()) {
                            if (component instanceof JLabel){
                                GUI.panel.remove(component);
                            }
                        }

                        GUI.drawPieces();

                        GUI.panel.revalidate();
                        GUI.panel.repaint();
                    }
                }
            }
        });
    }
}