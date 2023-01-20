package Chess;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GUI {

    public static JFrame frame;
    public static JPanel panel;
    private static String[] pieces;
    private static JLabel pieceLabel;
    private static HashMap<String, ImageIcon> images;
    public static int screenSize = 768;
    public static int squareSize = screenSize / 8;

    public GUI() {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(screenSize, screenSize);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        panel = new chessPanel();
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();

        new chessGame();

        pieces = new String[]{"wR", "wN", "wB", "wK", "wQ", "wP", "bR", "bN", "bB", "bK", "bQ", "bP"};
        images = new HashMap();

        for (int i = 0; i < pieces.length; i++) {
            images.put(pieces[i], new ImageIcon("src/images/" + pieces[i] + ".png"));
        }

        drawPieces();
    }

    public static void drawPieces() {
        for (int r = 0; r < 8; r++){
            for (int c = 0; c < 8; c++){
                String piece = chessGame.board[r][c];

                if (!piece.equals("--")) {
                    Image image = images.get(piece).getImage();
                    pieceLabel = new JLabel(new ImageIcon(image.getScaledInstance(squareSize, squareSize,  java.awt.Image.SCALE_SMOOTH)));
                    pieceLabel.setBounds(c * squareSize, r * squareSize, squareSize, squareSize);
                    panel.add(pieceLabel);
                }
                else {
                    panel.add(new JLabel(""));
                }
            }
        }
    }

    public static void gameoverMessage(){
        String message = "";
        if (chessGame.checkmate){
            if (!chessGame.whiteToMove){
                message = "White Wins! White has won " + chessGame.wins + " time(s)";
            }
            else if (chessGame.whiteToMove){
                message = "Black Wins! Black has won " + chessGame.wins + " time(s)";
            }
        }
        else if (chessGame.stalemate){
            message = "Stalemate!\n That's a tie!";
        }

        JLabel endLabel = new JLabel(message);
        endLabel.setForeground(Color.RED);
        endLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        panel.add(endLabel);
        endLabel.setBounds(125, 320, endLabel.getPreferredSize().width, endLabel.getPreferredSize().height);
        panel.setComponentZOrder(endLabel, 0);

        JLabel playAgainLabel = new JLabel("Press 'P' to play again");
        playAgainLabel.setForeground(Color.RED);
        playAgainLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        panel.add(playAgainLabel);
        playAgainLabel.setBounds(240, 400, playAgainLabel.getPreferredSize().width, playAgainLabel.getPreferredSize().height);
        panel.setComponentZOrder(playAgainLabel, 1);
    }
}