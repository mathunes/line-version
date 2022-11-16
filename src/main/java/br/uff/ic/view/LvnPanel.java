package br.uff.ic;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LvnPanel extends JPanel {

    private JTextArea jTextAreaCode;
    private JTextArea jTextAreaTerminal;
    private JScrollPane jScrollPaneCode;
    private JScrollPane jScrollPaneTerminal;
    private String text;
    private String filePath;
    Versioner versioner;

    public LvnPanel(String filePathArg) {

        filePath = filePathArg;
        String fileContent = "";
        versioner = new Versioner();

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

        jTextAreaCode = new JTextArea();
        jTextAreaTerminal = new JTextArea();

        jTextAreaTerminal.setEditable(false);
        jTextAreaCode.setEditable(false);
        jTextAreaCode.getCaret().setVisible(true);
        jTextAreaCode.setText(text);
        
        jScrollPaneCode = new JScrollPane(jTextAreaCode);
        jScrollPaneTerminal = new JScrollPane(jTextAreaTerminal);

        add(jScrollPaneCode);
        add(jScrollPaneTerminal);

        jTextAreaCode.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent caretEvent) {
                int positionSelected = caretEvent.getDot();

                if (positionSelected > 0) {
                    int lineSelected = text.substring(0, positionSelected - 1).split("\n", -1).length;


                    jTextAreaTerminal.setText(versioner.getLineInfoFromFileGraph(filePath, lineSelected - 1));
                }

            }
        });
    }
}
