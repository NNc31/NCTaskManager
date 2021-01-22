package ua.edu.sumdu.j2se.nefodov.tasks.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Controller;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Operations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class TaskSelection extends JFrame implements ActionListener, WindowStateListener {
    private Controller controller;
    private Operations nextOperation;
    private JPanel contentPane = new JPanel();
    private JTextField task = new JTextField(2);
    private JButton submit = new JButton("Submit");
    private JButton back = new JButton("Back");
    private TrayIcon trayIcon;
    private MenuItem trayExit = new MenuItem("Exit");

    public static final Logger LOGGER = LogManager.getLogger(TaskSelection.class);

    public TaskSelection(Controller controller, Operations operation) {
        this.controller = controller;
        nextOperation = operation;
        setTitle("Task manager");
        setIconImage(new ImageIcon("icon.jpg").getImage());
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Current task list. Select task by number");
        JTextArea tasks = new JTextArea(15, 27);
        JScrollPane scroll = new JScrollPane(tasks);
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel taskIn = new JPanel();

        contentPane.setBackground(new Color(0, 190, 255));
        flow.setBackground(new Color(0, 190, 255));
        taskIn.setBackground(new Color(0, 190, 255));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

        submit.setFocusable(false);
        back.setFocusable(false);
        tasks.setLineWrap(true);
        tasks.setEditable(false);
        tasks.setText(controller.getTaskList().toString());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        flow.add(submit);
        flow.add(back);
        taskIn.add(new JLabel("Task #"));
        taskIn.add(task);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0, 0);
        contentPane.add(title, c);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(15, 50, 10, 50);
        contentPane.add(scroll, c);
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 50, 30, 0);
        contentPane.add(taskIn, c);
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(0, 0, 5, 5);
        contentPane.add(flow, c);

        submit.addActionListener(this);
        back.addActionListener(this);

        if (SystemTray.isSupported()) {
            addWindowStateListener(this);
            PopupMenu pm = new PopupMenu();
            pm.add(trayExit);

            trayIcon = new TrayIcon(new ImageIcon("icon.jpg").getImage(), getTitle(), pm);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(this);
        }
        LOGGER.debug("Task selection created");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOGGER.debug("Action received");
        if (e.getSource().equals(submit)) {
            LOGGER.info("Submit pressed");
            if (controller.checkTaskNum(task.getText())) {
                LOGGER.info("Number accepted");
                dispose();
                controller.launchOperation(nextOperation, Integer.parseInt(task.getText()) - 1);
            } else {
                LOGGER.info("Incorrect number");
                JOptionPane.showMessageDialog(new JPanel(),
                        "Incorrect number!");
            }
        } else if (e.getSource().equals(back)) {
            LOGGER.info("Back pressed");
            dispose();
            controller.launchOperation(Operations.MAIN_MENU);
        } else if (e.getSource().equals(trayExit)) {
            LOGGER.info("Closing program from tray");
            System.exit(0);
        } else if (e.getSource().equals(trayIcon)) {
            LOGGER.info("Expanding program from tray");
            setExtendedState(NORMAL);
            setVisible(true);
            SystemTray.getSystemTray().remove(trayIcon);
        } else {
            LOGGER.error("Unexpected action event source");
            throw new IllegalStateException();
        }
    }

    @Override
    public void windowStateChanged(WindowEvent we) {
        LOGGER.debug("Windows state changed");
        int state = we.getNewState();
        SystemTray tray = SystemTray.getSystemTray();
        if (state == ICONIFIED) {
            LOGGER.info("Program put in tray");
            try {
                tray.add(trayIcon);
                setVisible(false);
                dispose();
            } catch (AWTException e) {
                LOGGER.error("AWTException in tray");
                e.printStackTrace();
            }
        }
    }
}
