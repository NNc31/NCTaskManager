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

public class TaskOutput extends JFrame implements ActionListener, WindowStateListener {
    private Controller controller;
    private  JPanel contentPane = new JPanel(new GridBagLayout());
    private JButton ok = new JButton("OK");
    private JButton exit = new JButton("Exit");
    private TrayIcon trayIcon;
    private MenuItem trayExit = new MenuItem("Exit");

    public static final Logger LOGGER = LogManager.getLogger(TaskOutput.class);

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

        ok.addActionListener(this);
        exit.addActionListener(this);
        trayExit.addActionListener(this);

        if (SystemTray.isSupported()) {
            addWindowStateListener(this);
            PopupMenu pm = new PopupMenu();
            pm.add(trayExit);

            trayIcon = new TrayIcon(new ImageIcon("icon.jpg").getImage(), getTitle(), pm);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(this);
        }
        LOGGER.debug("Task output created");
    }

    public void actionPerformed(ActionEvent e) {
        LOGGER.debug("Action received");
        if (e.getSource().equals(ok)) {
            LOGGER.info("OK pressed");
            dispose();
            controller.launchOperation(Operations.MAIN_MENU);
        } else if (e.getSource().equals(exit)) {
            LOGGER.info("Closing program");
            System.exit(0);
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
