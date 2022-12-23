package Chess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class GUI {

    public static JFrame frame;
    private static JPanel menuPanel;
    private static JLabel label;
    private static JPanel buttons;
    private static JButton playButton;
    private static JButton quitButton;

    public GUI(){
        frame = new JFrame();
        frame.setTitle("Chess Game - CS CPT");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(528, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void menu(){
        menuPanel = new JPanel();
        frame.add(menuPanel, BorderLayout.CENTER);
        frame.setContentPane(menuPanel);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        menuPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;

        label = new JLabel("<html><h1><strong><i>Chess!</i></strong></h1><hr></html>");
        menuPanel.add(label, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        buttons = new JPanel(new GridBagLayout());

        playButton = createButton("Play");
        buttons.add(playButton, gbc);

        quitButton = createButton("Quit");
        buttons.add(quitButton, gbc);

        gbc.weighty = 1;
        menuPanel.add(buttons, gbc);
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 24));

        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Play")) {
                    new chessGame();
                    refresh();
                }
                else if (e.getActionCommand().equals("Back")){
                    frame.setContentPane(menuPanel);
                    refresh();

                    for (KeyListener kl : frame.getKeyListeners()) {
                        frame.removeKeyListener(kl);
                    }
                }
                else if (e.getActionCommand().equals("Quit")){
                    frame.dispose();
                }
            }
        });
        return button;
    }

    public static void refresh(){
        frame.invalidate();
        frame.validate();
    }
}