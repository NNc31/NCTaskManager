package ua.edu.sumdu.j2se.nefodov.tasks.view;

import ua.edu.sumdu.j2se.nefodov.tasks.controller.Controller;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Operations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalendarOutput extends JFrame {
    private Controller controller;
    private  JPanel contentPane = new JPanel(new GridBagLayout());
    private JTextArea tasks = new JTextArea(15, 27);
    private JTextField startF = new JTextField(12);
    private JTextField endF = new JTextField(12);
    private JButton show = new JButton("Show");
    private JButton ok = new JButton("OK");
    private JButton back = new JButton("Back");
    private JLabel label = new JLabel("Calendar of tasks. Enter time period");

    public CalendarOutput(Controller controller) {
        this.controller = controller;
        setTitle("Task manager");
        setIconImage(new ImageIcon("icon.jpg").getImage());
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel period = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JScrollPane scroll = new JScrollPane(tasks);

        contentPane.setBackground(new Color(0, 190, 255));
        flow.setBackground(new Color(0, 190, 255));
        period.setBackground(new Color(0, 190, 255));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        show.setFocusable(false);
        ok.setFocusable(false);
        back.setFocusable(false);
        tasks.setEditable(false);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        flow.add(show);
        flow.add(ok);
        flow.add(back);
        period.add(new JLabel("Show from"));
        period.add(startF);
        period.add(new JLabel(" to"));
        period.add(endF);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 0, 20, 0);
        contentPane.add(label, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 50, 10, 50);
        contentPane.add(scroll, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 50, 20, 50);
        contentPane.add(period, c);
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 5, 5);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        contentPane.add(flow, c);

        ButtonEventListener eventListener = new ButtonEventListener();
        show.addActionListener(eventListener);
        ok.addActionListener(eventListener);
        back.addActionListener(eventListener);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(show)) {
                if (controller.checkTime(startF.getText(), endF.getText())) {
                    tasks.setText(controller.getCalendar(startF.getText(), endF.getText()));
                } else {
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Incorrect input!");
                }
            } else if (e.getSource().equals(ok)) {
                dispose();
                controller.launchOperation(Operations.MAIN_MENU);
            } else if (e.getSource().equals(back)) {
                dispose();
                controller.launchOperation(Operations.INFO_MENU);
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
