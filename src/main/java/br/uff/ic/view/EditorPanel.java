package br.uff.ic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class EditorPanel extends JPanel {

    private JTextArea jTextAreaCode;
    private JTextArea jTextAreaTerminal;
    private JScrollPane jScrollPaneCode;
    private JScrollPane jScrollPaneTerminal;

    public EditorPanel() {
        setPreferredSize(new Dimension (927, 600));
        setLayout(new GridLayout(2, 1, 0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jTextAreaCode = new JTextArea(5, 5);
        jTextAreaTerminal = new JTextArea(5, 5);
        jTextAreaCode.setEditable(false);
        jTextAreaTerminal.setEditable(false);
        jTextAreaCode.setText("\t1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.brt1package ic.uff.br\n");
        jTextAreaTerminal.setText("LINE 1:");


        jScrollPaneCode = new JScrollPane(jTextAreaCode);
        jScrollPaneTerminal = new JScrollPane(jTextAreaTerminal);

        add (jScrollPaneCode);
        add (jScrollPaneTerminal);
    }
}
