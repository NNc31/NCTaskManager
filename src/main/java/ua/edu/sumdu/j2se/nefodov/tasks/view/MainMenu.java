package ua.edu.sumdu.j2se.nefodov.tasks.view;

import ua.edu.sumdu.j2se.nefodov.tasks.controller.UserController;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.UserOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class MainMenu extends JFrame {
    private UserController controller;
    private JPanel contentPane = new JPanel(new GridBagLayout());
    private JButton add = new JButton("Add task");
    private JButton edit = new JButton("Edit task");
    private JButton delete = new JButton("Delete task");
    private JButton info = new JButton("Show info");
    private JMenu file = new JMenu("File");
    private JMenuItem save = new JMenuItem("Save");
    private JMenuItem load = new JMenuItem("Load");
    private JMenuItem exit = new JMenuItem("Exit");

    public MainMenu(UserController controller) {
        this.controller = controller;
        setTitle("Task manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);

        JPanel buttons = new JPanel();
        JMenuBar menuBar = new JMenuBar();
        JLabel label = new JLabel("Select operation");

        contentPane.setBackground(new Color(0, 190, 255));
        buttons.setBackground(new Color(0, 190, 255));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));

        add.setFocusable(false);
        edit.setFocusable(false);
        delete.setFocusable(false);
        info.setFocusable(false);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        contentPane.add(menuBar, c);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        contentPane.add(label, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(20, 30, 20, 30);
        contentPane.add(buttons, c);

        file.add(save);
        file.add(load);
        file.add(exit);
        menuBar.add(file);
        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(info);

        ButtonEventListener eventListener = new ButtonEventListener();
        add.addActionListener(eventListener);
        edit.addActionListener(eventListener);
        delete.addActionListener(eventListener);
        info.addActionListener(eventListener);
        save.addActionListener(eventListener);
        load.addActionListener(eventListener);
        exit.addActionListener(eventListener);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            if (e.getSource().equals(add)) {
                dispose();
                controller.launchOperation(UserOperations.ADD_MENU);
            } else if (e.getSource().equals(edit)) {
                dispose();
                controller.launchOperation(UserOperations.EDIT_TASK);
            } else if (e.getSource().equals(delete)) {
                dispose();
                controller.launchOperation(UserOperations.DELETE_TASK);
            } else if (e.getSource().equals(info)) {
                dispose();
                controller.launchOperation(UserOperations.INFO_MENU);
            } else if (e.getSource().equals(save)) {
                controller.saveList(controller.loadFile());
            } else if (e.getSource().equals(exit)) {
                System.exit(0);
            } else if (e.getSource().equals(load)) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(
                        new File("D:\\Studies\\Courses\\Netcracker\\NCTaskManager"));
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    if (controller.checkFile(file)) {
                        controller.setFile(file);
                        controller.loadList(file);
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(),
                                "Incorrect file!");
                    }
                }
            } else {
                throw new IllegalStateException();
            }
        }
    }
}