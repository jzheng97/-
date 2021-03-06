import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainPanel {
    // UI panels
    JPanel mainPanel;
    JPanel optionPanel;
    JPanel textPanel;
    JTextArea textArea;
    JPanel operationPanel;

    // states & switches
    boolean isRunning = false;
    JButton startBtn;
    JTextField intervalField;
    double interval = 500;
    JCheckBox randomCB;
    boolean isRandom = false;
    JCheckBox loopCB;
    boolean isLooping = false;
    // Text emitting control
    PastingListener pastingListener = new PastingListener();

    public void initMainPanel(){
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        optionPanel = initOptionPanel();
        optionPanel.setMaximumSize(new Dimension(850, 200));
        optionPanel.setPreferredSize(new Dimension(850, 200));
        mainPanel.add(optionPanel);

        textPanel = initTextPanel();
        textPanel.setMaximumSize(new Dimension(850, 400));
        textPanel.setPreferredSize(new Dimension(850, 400));
        mainPanel.add(textPanel);

        operationPanel = initOperationPanel();
        operationPanel.setMaximumSize(new Dimension(850, 70));
        operationPanel.setPreferredSize(new Dimension(850, 70));
        mainPanel.add(operationPanel);

        this.mainPanel = mainPanel;
    }

    public JPanel initOptionPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new EmptyBorder(0, 25, 0, 0));
        // left & right option areas
        // left
        JPanel leftPanel = initLeftPanel();
        leftPanel.setMaximumSize(new Dimension(300, 160));
        leftPanel.setPreferredSize(new Dimension(300, 160));

        // right
        JPanel rightPanel = initRightPanel();
        rightPanel.setMaximumSize(new Dimension(500, 160));
        rightPanel.setPreferredSize(new Dimension(500, 160));

        panel.add(leftPanel);
        panel.add(rightPanel);
        return panel;
    }

    public JPanel initRightPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(1, 15, 0, 10));
        panel.add(new JLabel("??????: "));
        panel.add(new JSeparator());
        JTextArea intro = new JTextArea(10, 10);
        intro.setEditable(false);
        intro.setLineWrap(true);
        intro.setText(
            """
            1.???????????????????????????.txt?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            2.????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            3.???????????????????????????Ctrl + V???????????????????????????????????????????????????????????????????????????\nCtrl + /????????????????????????
            4.??????????????????????????????????????????Ctrl + V??????????????????????????????????????????????????????????????????????????????/??????
                                                                                                                                               - ??????
            """
        );
        panel.add(intro);
        return panel;
    }

    public JPanel initLeftPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        // upper left corner
        JPanel optionHeader = new JPanel();
        optionHeader.setLayout(new BoxLayout(optionHeader, BoxLayout.Y_AXIS));
        optionHeader.add(new JLabel("??????(????????????):"));
        optionHeader.add(new JSeparator());
        panel.add(optionHeader);

        // checkboxes
        // randomness
        JPanel randomWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        randomCB = new JCheckBox("????????????");
        randomWrapper.add(randomCB);
        randomCB.setBounds(100,100, 50,50);
//        randomCB.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == 1) {
//                    isRandom = true;
//                } else {
//                    isRandom = false;
//                }
//            }
//        });
        // looping
        JPanel loopWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loopCB = new JCheckBox("????????????");
//        loopCB.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == 1) {
//                    isLooping = true;
//                } else {
//                    isLooping = false;
//                }
//            }
//        });
        loopWrapper.add(loopCB);
        loopCB.setBounds(100,100, 50,50);
        panel.add(randomWrapper);
        panel.add(loopWrapper);

        // Set interval
        JPanel intervalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel intervalLabel = new JLabel("??????????????????(???): ");
        intervalField = new JTextField();
        intervalField.setText("0.5");
        intervalField.setColumns(10);

        intervalPanel.add(intervalLabel);
        intervalPanel.add(intervalField);
        panel.add(intervalPanel);

        // Set Start & stop button
        JPanel quickStart = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel quickStartLabel = new JLabel("????????????/?????????: Ctrl + ");
        JTextField quickStartKey = new JTextField("/");
        // TODO customize shortcut
        quickStartKey.setEditable(false);
        quickStart.add(quickStartLabel);
        quickStart.add(quickStartKey);
        panel.add(quickStart);
        return panel;
    }

    public JPanel initTextPanel(){
        JPanel panel = new JPanel();
        textArea = new JTextArea(23, 80);
        textArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);
        return panel;
    }

    public JPanel initOperationPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.setBorder(new EmptyBorder(10, 0, 10, 15));
        panel.add(new JPanel());
        JPanel buttons = new JPanel();
        // import button
        JButton importBtn = new JButton("?????????????????????");
        importBtn.setPreferredSize(new Dimension(180, 42));
        importBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
                dialog.setFile("*.txt");
                dialog.setMode(FileDialog.LOAD);
                dialog.setVisible(true);
                String path = dialog.getDirectory() + dialog.getFile();
                if (path.length() > 0) {
                    try(BufferedReader br = new BufferedReader(new FileReader(path))) {
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append(System.lineSeparator());
                            line = br.readLine();
                        }
                        textArea.setText(sb.toString());
                    } catch (IOException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
            }
        });
        buttons.add(importBtn);

        // start & pause button
        startBtn = new JButton("??????/?????????");
        startBtn.setPreferredSize(new Dimension(180, 42));
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    startBtn.setText("??????/?????????");
                    isLooping = loopCB.isSelected();
                    isRandom = randomCB.isSelected();
                    interval = validateAndGetInterval(intervalField.getText());
                    pastingListener.init(new TextStorage(textArea.getText(), isLooping, isRandom), interval);
                    isRunning = true;
                } else {
                    startBtn.setText("??????/?????????");
                    try {
                        pastingListener.stop();
                    } catch (NativeHookException ex) {
                        ex.printStackTrace();
                    }
                    isRunning = false;
                }
            }
        });
        buttons.add(startBtn);
        panel.add(buttons);
        return panel;
    }

    public double validateAndGetInterval(String input) {
        double res = 500d;
        try
        {
            res = Double.parseDouble(input) * 1000;
        }
        catch(NumberFormatException e)
        {
            intervalField.setText("0.5");
        }
        return res;
    }

    public static void main(String[] args) {
        MainPanel mp = new MainPanel();
        mp.initMainPanel();
        // Create and set up a frame window to select film
        JFrame frame = new JFrame("?????????????????????");
        frame.add(mp.mainPanel);
        frame.setPreferredSize(new Dimension(865, 700));
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
