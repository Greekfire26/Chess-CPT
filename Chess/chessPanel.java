package Chess;

import javax.swing.*;
import java.awt.*;

public class chessPanel extends JPanel {
    chessPanel(){
        super.setPreferredSize(new Dimension(GUI.screenSize, GUI.screenSize));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        for (int row = 0; row < 8; row++) {
            for (int col = row % 2; col < 8; col += 2){
                g.fillRect(row * GUI.squareSize, col * GUI.squareSize, GUI.squareSize, GUI.squareSize);
            }
        }
    }
}