package Chess;

import javax.swing.*;
import java.awt.*;

public class chessPanel extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        for ( int row = 0; row < 8; row++ ) {
            for ( int col = row % 2; col < 8; col += 2 ) {
                g.fillRect( row * 64, col * 64, 64, 64 );
            }
        }
        g.drawLine(0,512,512,512);
    }
}