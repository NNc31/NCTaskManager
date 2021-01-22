package ua.edu.sumdu.j2se.nefodov.tasks.view;

import ua.edu.sumdu.j2se.nefodov.tasks.controller.Controller;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Operations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskOutput extends JFrame {
    private Controller controller;
    private  JPanel contentPane = new JPanel(new GridBagLayout());
    private JButton ok = new JButton("OK");
    private JButton exit = new JButton("Exit");

    public  TaskOutput(Controller controller) {
        this.controller = controller;
        setTitle("Task manager");
        setIconImage(new ImageIcon("icon.jpg").getImage());
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Current task list");
        JTextArea tasks = new JTextArea(15, 27);
        JScrollPane scroll = new JScrollPane(tasks);
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        contentPane.setBackground(new Color(0, 190, 255));
        flow.setBackground(new Color(0, 190, 255));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        ok.setFocusable(false);
        exit.setFocusable(false);
        tasks.setLineWrap(true);
        tasks.setEditable(false);
        tasks.setText(controller.getTaskList().toString());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        flow.add(ok);
        flow.add(exit);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 0, 0, 0);
        contentPane.add(title, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 50, 30, 50);
        contentPane.add(scroll, c);
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(0, 0, 5, 5);
        contentPane.add(flow, c);

        ButtonEventListener eventListener = new ButtonEventListener();
        ok.addActionListener(eventListener);
        exit.addActionListener(eventListener);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(ok)) {
                dispose();
                controller.launchOperation(Operations.MAIN_MENU);
            } else if (e.getSource().equals(exit)) {
                System.exit(0);
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
