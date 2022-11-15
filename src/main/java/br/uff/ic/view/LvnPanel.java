package br.uff.ic;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.*;

public class LvnPanel extends JPanel {

    private JTextArea jTextAreaCode;
    private JTextArea jTextAreaTerminal;
    private JScrollPane jScrollPaneCode;
    private JScrollPane jScrollPaneTerminal;
    private String text;
    private String filePath;

    public LvnPanel(String filePathArg) {

        filePath = filePathArg;
        String fileContent = "";

        try {
            Scanner scanner = new Scanner(new File(filePath));

            while (scanner.hasNext()){
                fileContent = fileContent + scanner.nextLine() + "\n";
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("lvn: a problem occurred when opening the GUI - " + e);
        }
        
        text = fileContent;

        setPreferredSize(new Dimension (927, 600));
        setLayout(new GridLayout(2, 1, 0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jTextAreaCode = new JTextArea(5, 5);
        jTextAreaTerminal = new JTextArea(5, 5);
        jTextAreaCode.setEditable(false);
        jTextAreaCode.getCaret().setVisible(true);
        jTextAreaTerminal.setEditable(false);
        jTextAreaCode.setText(text);

        jScrollPaneCode = new JScrollPane(jTextAreaCode);
        jScrollPaneTerminal = new JScrollPane(jTextAreaTerminal);

        add (jScrollPaneCode);
        add (jScrollPaneTerminal);

        jTextAreaCode.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent caretEvent) {
                int positionSelected = caretEvent.getDot();

                if (positionSelected > 0) {
                    int lineSelected = text.substring(0, positionSelected - 1).split("\n", -1).length;

                    Versioner versioner = new Versioner();

                    jTextAreaTerminal.setText(versioner.getLineInfoFromFileGraph(filePath, lineSelected - 1));
                }

            }
        });
    }
}
