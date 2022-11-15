package br.uff.ic;

import javax.swing.*;

public class WindowFrame extends JFrame {

    public WindowFrame(String fileContent) {
        setTitle("file name");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        final JLabel label = new JLabel("Hello World");
        getContentPane().add(label);
        getContentPane().add(new EditorPanel(fileContent));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

}