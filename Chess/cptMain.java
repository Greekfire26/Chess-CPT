package Chess;
//TODO:

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class cptMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Nimbus Look and Feel Not Installed!");
                }
                GUI gui = new GUI();
                gui.menu();
            }
        });
    }
}