package Chess;
//TODO:

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class cptMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}