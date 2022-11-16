package br.uff.ic;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class LvnFrame extends JFrame {

    public LvnFrame(String filePath) {
        setTitle(filePath);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon lvnLogo = new ImageIcon("./assets/lvn-logo.png");
        setIconImage(lvnLogo.getImage());

        getContentPane().add(new LvnPanel(filePath));
        pack();
        setVisible(true);
    }

}