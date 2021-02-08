package ua.edu.sumdu.j2se.nefodov.tasks.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Controller;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Operations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainMenu extends JFrame implements ActionListener, WindowStateListener {
    private Controller controller;
    private JPanel contentPane = new JPanel(new GridBagLayout());
    private JButton add = new JButton("Add task");
    private JButton edit = new JButton("Edit task");
    private JButton delete = new JButton("Delete task");
    private JButton info = new JButton("Show info");
    private JMenu file = new JMenu("File");
    private JMenuItem save = new JMenuItem("Save");
    private JMenuItem load = new JMenuItem("Load");
    private JMenuItem exit = new JMenuItem("Exit");
    private TrayIcon trayIcon;
    private MenuItem trayExit = new MenuItem("Exit");

    public static final Logger LOGGER = LogManager.getLogger(MainMenu.class);

    public MainMenu(Controller controller) {
        this.controller = controller;
        setTitle("Task manager");
        setIconImage(new ImageIcon("icon.jpg").getImage());
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

        add.addActionListener(this);
        edit.addActionListener(this);
        delete.addActionListener(this);
        info.addActionListener(this);
        save.addActionListener(this);
        load.addActionListener(this);
        exit.addActionListener(this);

        if (SystemTray.isSupported()) {
            addWindowStateListener(this);
            PopupMenu pm = new PopupMenu();
            pm.add(trayExit);

            trayIcon = new TrayIcon(new ImageIcon("icon.jpg").getImage(), getTitle(), pm);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(this);
        }
        LOGGER.debug("Main menu created");
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        LOGGER.debug("Action received");
        if (e.getSource().equals(add)) {
            LOGGER.info("Add pressed");
            dispose();
            controller.launchOperation(Operations.ADD_MENU);
        } else if (e.getSource().equals(edit)) {
            LOGGER.info("Edit pressed");
            dispose();
            controller.launchOperation(Operations.EDIT_TASK);
        } else if (e.getSource().equals(delete)) {
            LOGGER.info("Delete pressed");
            dispose();
            controller.launchOperation(Operations.DELETE_TASK);
        } else if (e.getSource().equals(info)) {
            LOGGER.info("Info pressed");
            dispose();
            controller.launchOperation(Operations.INFO_MENU);
        } else if (e.getSource().equals(save)) {
            LOGGER.info("Save pressed");
            controller.saveList(controller.getFile());
        } else if (e.getSource().equals(exit)) {
            LOGGER.info("Exit pressed");
            System.exit(0);
        } else if (e.getSource().equals(load)) {
            LOGGER.info("Load pressed");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(
                    new File("D:\\Studies\\Courses\\Netcracker\\NCTaskManager"));
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                LOGGER.info("File " + file.getAbsolutePath() + " selected");
                if (controller.checkFile(file)) {
                    controller.setFile(file);
                    controller.clearList();
                    controller.loadList(file);
                } else {
                    LOGGER.info("Incorrect file");
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Incorrect file!");
                }
            }
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