package ua.edu.sumdu.j2se.nefodov.tasks.view;

import ua.edu.sumdu.j2se.nefodov.tasks.controller.UserController;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.UserOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMenu extends JFrame {
    UserController controller;
    int taskNum;
    private JPanel contentPane = new JPanel(new GridBagLayout());
    private JPanel cards = new JPanel(new CardLayout());
    private JButton submit = new JButton("Submit");
    private JButton back = new JButton("Back");
    private JRadioButton title = new JRadioButton("Title");
    private JRadioButton active = new JRadioButton("Active");
    private JRadioButton time = new JRadioButton("Time");
    private JRadioButton start = new JRadioButton("Start time");
    private JRadioButton end = new JRadioButton("End time");
    private JRadioButton interval = new JRadioButton("Interval");
    private JRadioButton active1 = new JRadioButton("Active");
    private JRadioButton active2 = new JRadioButton("Not active");
    private JLabel label = new JLabel("Select option and submit changes");
    private JTextField titleF = new JTextField(9);
    private JTextField timeF = new JTextField(12);
    private JTextField startF = new JTextField(12);
    private JTextField endF = new JTextField(12);
    private JTextField intervalF = new JTextField(6);

    public EditMenu(UserController controller, int taskNum) {
        this.controller = controller;
        setTitle("Task manager");
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.taskNum = taskNum;

        JPanel sbPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel optional = new JPanel(new GridBagLayout());
        JPanel titleCard = new JPanel();
        JPanel parameter = new JPanel();
        JPanel activeCard = new JPanel(new GridLayout(2, 1, 0, 0));
        JPanel timeCard = new JPanel(new GridLayout(2, 1, 0, 0));
        JPanel startCard = new JPanel(new GridLayout(2, 1, 0, 0));
        JPanel endCard = new JPanel(new GridLayout(2, 1, 0, 0));
        JPanel intervalCard = new JPanel(new GridLayout(2, 1, 0, 0));

        contentPane.setBackground(new Color(0, 190, 255));
        sbPanel.setBackground(new Color(0, 190, 255));
        optional.setBackground(new Color(0, 190, 255));
        parameter.setBackground(new Color(0, 190, 255));
        titleCard.setBackground(new Color(0, 190, 255));
        activeCard.setBackground(new Color(0, 190, 255));
        title.setBackground(new Color(0, 190, 255));
        active.setBackground(new Color(0, 190, 255));
        active1.setBackground(new Color(0, 190, 255));
        active2.setBackground(new Color(0, 190, 255));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));

        parameter.setLayout(new BoxLayout(parameter, BoxLayout.Y_AXIS));
        submit.setFocusable(false);
        back.setFocusable(false);
        title.setFocusable(false);
        active.setFocusable(false);
        active1.setFocusable(false);
        active2.setFocusable(false);
        time.setFocusable(false);
        start.setFocusable(false);
        end.setFocusable(false);
        interval.setFocusable(false);

        ButtonGroup buttons = new ButtonGroup();
        buttons.add(title);
        buttons.add(active);
        ButtonGroup actives = new ButtonGroup();
        actives.add(active1);
        actives.add(active2);

        sbPanel.add(submit);
        sbPanel.add(back);
        titleCard.add(new JLabel("New title"));
        titleCard.add(titleF);
        activeCard.add(active1);
        activeCard.add(active2);
        cards.add(titleCard, "title");
        cards.add(activeCard, "active");
        parameter.add(title);
        parameter.add(active);

        ButtonEventListener eventListener = new ButtonEventListener();
        submit.addActionListener(eventListener);
        back.addActionListener(eventListener);
        title.addActionListener(eventListener);
        active.addActionListener(eventListener);

        if (controller.getTaskList().getTask(taskNum).isRepeated()) {
            startCard.setBackground(new Color(0, 190, 255));
            endCard.setBackground(new Color(0, 190, 255));
            intervalCard.setBackground(new Color(0, 190, 255));
            start.setBackground(new Color(0, 190, 255));
            end.setBackground(new Color(0, 190, 255));
            interval.setBackground(new Color(0, 190, 255));
            time.setSelected(false);

            startCard.add(new JLabel("New start time"));
            startCard.add(startF);
            endCard.add(new JLabel("New end time"));
            endCard.add(endF);
            intervalCard.add(new JLabel("New interval"));
            intervalCard.add(intervalF);

            parameter.add(start);
            parameter.add(end);
            parameter.add(interval);
            buttons.add(start);
            buttons.add(end);
            buttons.add(interval);

            cards.add(startCard, "start");
            cards.add(endCard, "end");
            cards.add(intervalCard, "interval");

            start.addActionListener(eventListener);
            end.addActionListener(eventListener);
            interval.addActionListener(eventListener);
        } else {
            timeCard.setBackground(new Color(0, 190, 255));
            time.setBackground(new Color(0, 190, 255));

            start.setSelected(false);
            end.setSelected(false);
            interval.setSelected(false);

            timeCard.add(new JLabel("New time"));
            timeCard.add(timeF);
            parameter.add(time);
            buttons.add(time);

            cards.add(timeCard, "time");

            time.addActionListener(eventListener);
        }

        title.setSelected(true);
        active1.setSelected(true);

        GridBagConstraints o = new GridBagConstraints();
        o.gridx = 0;
        o.gridy = 0;
        o.insets = new Insets(10, 10, 0, 50);
        optional.add(parameter, o);
        o.gridx = 1;
        o.gridy = 0;
        o.insets = new Insets(10, 20, 0, 10);
        optional.add(cards, o);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        contentPane.add(label, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 10, 30, 10);
        contentPane.add(optional, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 5, 5);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        contentPane.add(sbPanel, c);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(submit)) {
                if (title.isSelected()) {
                    if (controller.checkTitle(titleF.getText())) {
                        controller.editTitle(titleF.getText(), taskNum);
                        dispose();
                        controller.launchOperation(UserOperations.TASK_OUTPUT);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Incorrect title!");
                    }
                } else if (active.isSelected()) {
                    controller.editActive(active1.isSelected(), taskNum);
                    dispose();
                    controller.launchOperation(UserOperations.TASK_OUTPUT);
                } else if (time.isSelected()) {
                    if (controller.checkTime(timeF.getText())) {
                        controller.editTime(timeF.getText(), taskNum);
                        dispose();
                        controller.launchOperation(UserOperations.TASK_OUTPUT);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Incorrect time!");
                    }
                } else if (start.isSelected()) {
                    if (controller.checkTime(startF.getText(), taskNum)) {
                        controller.editStartTime(startF.getText(), taskNum);
                        dispose();
                        controller.launchOperation(UserOperations.TASK_OUTPUT);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Incorrect start time!");
                    }
                } else if (end.isSelected()) {
                    if (controller.checkTime(taskNum, endF.getText())) {
                        controller.editEndTime(endF.getText(), taskNum);
                        dispose();
                        controller.launchOperation(UserOperations.TASK_OUTPUT);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Incorrect end time!");
                    }
                } else if (interval.isSelected()) {
                    if (controller.checkInterval(intervalF.getText())) {
                        controller.editInterval(intervalF.getText(), taskNum);
                        dispose();
                        controller.launchOperation(UserOperations.TASK_OUTPUT);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Incorrect interval!");
                    }
                }
            } else if (e.getSource().equals(back)) {
                dispose();
                controller.launchOperation(UserOperations.EDIT_TASK);
            } else if (e.getSource().equals(title)) {
                CardLayout layout = (CardLayout)(cards.getLayout());
                layout.show(cards, "title");
            } else if (e.getSource().equals(active)) {
                CardLayout layout = (CardLayout)(cards.getLayout());
                layout.show(cards, "active");
            } else if (e.getSource().equals(time)) {
                CardLayout layout = (CardLayout)(cards.getLayout());
                layout.show(cards, "time");
            } else if (e.getSource().equals(start)) {
                CardLayout layout = (CardLayout)(cards.getLayout());
                layout.show(cards, "start");
            } else if (e.getSource().equals(end)) {
                CardLayout layout = (CardLayout)(cards.getLayout());
                layout.show(cards, "end");
            } else if (e.getSource().equals(interval)) {
                CardLayout layout = (CardLayout)(cards.getLayout());
                layout.show(cards, "interval");
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
