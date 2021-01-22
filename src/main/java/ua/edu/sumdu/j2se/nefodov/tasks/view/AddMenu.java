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

public class AddMenu extends JFrame implements ActionListener, WindowStateListener {
    private Controller controller;
    private JPanel contentPane = new JPanel();
    private JRadioButton yes = new JRadioButton("Yes");
    private JRadioButton no = new JRadioButton("No");
    private JButton submit = new JButton("Submit");
    private JButton back = new JButton("Back");
    private JPanel cards = new JPanel(new CardLayout());
    private JTextField titleF1 = new JTextField(12);
    private JTextField titleF2 = new JTextField(12);
    private JCheckBox active1 = new JCheckBox();
    private JCheckBox active2 = new JCheckBox();
    private JTextField intervalF = new JTextField(12);
    private JTextField startF = new JTextField(12);
    private JTextField endF = new JTextField(12);
    private JTextField timeF = new JTextField(12);
    private TrayIcon trayIcon;
    private MenuItem trayExit = new MenuItem("Exit");

    public static final Logger LOGGER = LogManager.getLogger(AddMenu.class);

    public AddMenu(Controller controller) {
        this.controller = controller;
        setTitle("Task manager");
        setIconImage(new ImageIcon("icon.jpg").getImage());
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel sbButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel rPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        JPanel sbPanel = new JPanel(new BorderLayout());
        JPanel flowT1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel flowA1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel flowS = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel flowE = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel flowI = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel flowT2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel flowA2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel flowT = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel grid1 = new JPanel(new GridLayout(5, 1, 0, 5));
        JPanel grid2 = new JPanel(new GridLayout(4, 1, 0, 5));
        JPanel card1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel card2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel label = new JLabel(" Enter info about task");

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(rPanel);
        contentPane.add(cards);

        contentPane.setBackground(new Color(0, 190, 255));
        sbButtons.setBackground(new Color(0, 190, 255));
        sbPanel.setBackground(new Color(0, 190, 255));
        rPanel.setBackground(new Color(0, 190, 255));
        yes.setBackground(new Color(0, 190, 255));
        no.setBackground(new Color(0, 190, 255));
        flowT1.setBackground(new Color(0, 190, 255));
        flowT2.setBackground(new Color(0, 190, 255));
        flowA1.setBackground(new Color(0, 190, 255));
        flowA2.setBackground(new Color(0, 190, 255));
        flowS.setBackground(new Color(0, 190, 255));
        flowE.setBackground(new Color(0, 190, 255));
        flowI.setBackground(new Color(0, 190, 255));
        flowT.setBackground(new Color(0, 190, 255));
        active1.setBackground(new Color(0, 190, 255));
        active2.setBackground(new Color(0, 190, 255));
        grid1.setBackground(new Color(0, 190, 255));
        card1.setBackground(new Color(0, 190, 255));
        grid2.setBackground(new Color(0, 190, 255));
        card2.setBackground(new Color(0, 190, 255));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        flowT1.add(new JLabel("Title"));
        flowT1.add(titleF1);
        flowA1.add(new JLabel("Active"));
        flowA1.add(active1);
        flowS.add(new JLabel("Start time"));
        flowS.add(startF);
        flowE.add(new JLabel("End time"));
        flowE.add(endF);
        flowI.add(new JLabel("Interval"));
        flowI.add(intervalF);
        flowT2.add(new JLabel("Title"));
        flowT2.add(titleF2);
        flowA2.add(new JLabel("Active"));
        flowA2.add(active2);
        flowT.add(new JLabel("Time"));
        flowT.add(timeF);
        grid1.add(flowT1);
        grid1.add(flowA1);
        grid1.add(flowS);
        grid1.add(flowE);
        grid1.add(flowI);
        grid2.add(flowT2);
        grid2.add(flowA2);
        grid2.add(flowT);

        card1.add(grid1);
        card2.add(grid2);
        cards.add(card1, "Repeated");
        cards.add(card2, "Not repeated");

        sbButtons.add(submit);
        sbButtons.add(back);
        submit.setFocusable(false);
        back.setFocusable(false);
        sbPanel.add(sbButtons, BorderLayout.SOUTH);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(yes);
        buttonGroup.add(no);
        rPanel.setLayout(new GridLayout(5, 1, 0, 5));
        rPanel.add(label);
        rPanel.add(new JLabel(" Task is repeated"));
        rPanel.add(yes);
        rPanel.add(no);
        rPanel.add(sbPanel);
        yes.setSelected(true);
        yes.setFocusable(false);
        no.setFocusable(false);

        submit.addActionListener(this);
        back.addActionListener(this);
        yes.addActionListener(this);
        no.addActionListener(this);

        if (SystemTray.isSupported()) {
            addWindowStateListener(this);
            PopupMenu pm = new PopupMenu();
            pm.add(trayExit);

            trayIcon = new TrayIcon(new ImageIcon("icon.jpg").getImage(), getTitle(), pm);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(this);
        }
        LOGGER.debug("Add menu created");
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        LOGGER.debug("Action received");
        if (e.getSource().equals(submit)) {
            LOGGER.info("Submit pressed");
            submit.setEnabled(false);
            if (yes.isSelected()) {
                if (controller.checkTask(titleF1.getText(),
                        startF.getText(), endF.getText(), intervalF.getText())) {
                    controller.addTask(titleF1.getText(), active1.isSelected(),
                            startF.getText(), endF.getText(), intervalF.getText());
                    dispose();
                    controller.launchOperation(Operations.TASK_OUTPUT);
                } else {
                    LOGGER.info("Incorrect input");
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Incorrect input!");
                    submit.setEnabled(true);
                }
            } else {
                if (controller.checkTask(titleF2.getText(), timeF.getText())) {
                    controller.addTask(titleF2.getText(), active2.isSelected(), timeF.getText());
                    dispose();
                    controller.launchOperation(Operations.TASK_OUTPUT);
                } else {
                    LOGGER.info("Incorrect input");
                    JOptionPane.showMessageDialog(new JPanel(),
                            "Incorrect input!");
                    submit.setEnabled(true);
                }
            }
        } else if (e.getSource().equals(back)) {
            LOGGER.info("Back pressed");
            dispose();
            controller.launchOperation(Operations.MAIN_MENU);
        } else if (e.getSource().equals(yes)) {
            LOGGER.debug("Yes pressed");
            CardLayout layout = (CardLayout)(cards.getLayout());
            layout.show(cards, "Repeated");
        } else if (e.getSource().equals(no)) {
            LOGGER.debug("No pressed");
            CardLayout layout = (CardLayout)(cards.getLayout());
            layout.show(cards, "Not repeated");
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
