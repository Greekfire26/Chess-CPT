package Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;

public class chessGame {

    public static JPanel chessPanel;
    private JButton backButton;
    private JLabel scoreLabel;
    public static String[][] board;
    public static boolean whiteToMove;
    public static ArrayList<Move> movelist = new ArrayList<>();

    public chessGame() {
        chessPanel = new chessPanel();
        GUI.frame.setContentPane(chessPanel);
        chessPanel.setBackground(Color.DARK_GRAY);
        chessPanel.setLayout(null);

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
        scoreLabel = new JLabel("Computer: 0   Player: 0");
        scoreLabel.setForeground(new Color(255, 255, 255));
        scoreLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        chessPanel.add(scoreLabel);
        scoreLabel.setBounds(155, 515, 300, 50);

        backButton = GUI.createButton("Back");
        chessPanel.add(backButton);
        backButton.setBounds(5, 515, backButton.getPreferredSize().width, backButton.getPreferredSize().height);

        String[] pieces = {"wR", "wN", "wB", "wK", "wQ", "wP", "bR", "bN", "bB", "bK", "bQ", "bP"};
        JLabel[][] pieceLabels = new JLabel[board.length][board[0].length];

        HashMap<String, BufferedImage> images = new HashMap();

        try {
            for (int i = 0; i < pieces.length; i++){
                images.put(pieces[i], ImageIO.read(new File("src/images/" + pieces[i] + ".png")));
            }

            for (int r = 0; r < 8; r++){
                for (int c = 0; c < 8; c++){
                    String piece = board[r][c];

                    if (!piece.equals("--")){
                        pieceLabels[r][c] = new JLabel(new ImageIcon(images.get(piece)));
                        chessPanel.add(pieceLabels[r][c]);
                        pieceLabels[r][c].setBounds(c * 64, r * 64, 64, 64);
                    }
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }

        ArrayList<Integer> selected_square = new ArrayList();
        ArrayList<int[]> clicks = new ArrayList();

        GUI.frame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                System.out.println(me);
                if (me.getButton() == MouseEvent.BUTTON1){
                    int col = me.getXOnScreen() / 64;
                    int row = me.getYOnScreen() / 64;

                    if (selected_square.get(0) == row && selected_square.get(1) == col){
                        selected_square.clear();
                        clicks.clear();
                    } else {
                        selected_square.clear();
                        clicks.add(new int[]{row, col});
                    }

                    if (clicks.size() == 2){
                        Move move = new Move(clicks.get(0), clicks.get(1));
                        System.out.println(move.getChessNotation());
                        Move.makeMove(move);
                        selected_square.clear();
                        clicks.clear();
                    }

                }
            }
        });

    }
}