package Chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.HashMap;

public class GUI {

    public static JFrame frame;
    private static JPanel panel;
    private static String[] pieces;
    private static JLabel pieceLabel;
    private static HashMap<String, ImageIcon> images;
    public static int screenSize = 768;
    public static int squareSize = 768 / 8;

    public GUI() {
        frame = new JFrame();
        frame.setTitle("Chess Game - CS CPT");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(screenSize, screenSize);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        panel = new chessPanel();
        panel.setLayout(new GridLayout(8, 8, 0, 0));
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
                    panel.add(pieceLabel);
                }
                else {
                    panel.add(new JLabel(""));
                }
            }
        }
    }
}