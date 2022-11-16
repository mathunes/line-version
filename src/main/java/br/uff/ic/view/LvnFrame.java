package br.uff.ic;

import javax.swing.JFrame;

public class LvnFrame extends JFrame {

    public LvnFrame(String filePath) {
        setTitle(filePath);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(new LvnPanel(filePath));
        pack();
        setVisible(true);
    }

}