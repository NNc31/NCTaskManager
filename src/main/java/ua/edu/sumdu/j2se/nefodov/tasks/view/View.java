package ua.edu.sumdu.j2se.nefodov.tasks.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Operations;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Controller;

import javax.swing.*;

public class View {
    private Controller controller;

    public static final Logger LOGGER = LogManager.getLogger(View.class);

    public View(Controller controller) {
        this.controller = controller;
        LOGGER.debug("View created");
    }

    public void mainMenu() {
        MainMenu mainMenu = new MainMenu(controller);
        mainMenu.pack();
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
        LOGGER.debug("Main menu launched");
    }

    public void addMenu() {
        AddMenu addMenu = new AddMenu(controller);
        addMenu.pack();
        addMenu.setLocationRelativeTo(null);
        addMenu.setVisible(true);
        LOGGER.debug("Add menu launched");
    }

    public void editMenu(int n) {
        EditMenu editMenu = new EditMenu(controller, n);
        editMenu.pack();
        editMenu.setLocationRelativeTo(null);
        editMenu.setVisible(true);
        LOGGER.debug("Edit menu launched");
    }

    public void taskOutput() {
        TaskOutput taskOutput = new TaskOutput(controller);
        taskOutput.pack();
        taskOutput.setLocationRelativeTo(null);
        taskOutput.setVisible(true);
        LOGGER.debug("Task output launched");
    }

    public void taskSelection(Operations operation) {
        TaskSelection taskSelection = new TaskSelection(controller, operation);
        taskSelection.pack();
        taskSelection.setLocationRelativeTo(null);
        taskSelection.setVisible(true);
        LOGGER.debug("Task selection launched");
    }

    public void calendarOutput() {
        CalendarOutput calendarOutput = new CalendarOutput(controller);
        calendarOutput.pack();
        calendarOutput.setLocationRelativeTo(null);
        calendarOutput.setVisible(true);
        LOGGER.debug("Calendar output launched");
    }

    public void editTask() {
        LOGGER.debug("Launching of the task selection for next editing");
        taskSelection(Operations.EDIT_MENU);
    }

    public void deleteTask() {
        LOGGER.debug("Launching of the task selection for next deleting");
        taskSelection(Operations.DELETE_MENU);
    }

    public void deleteConfirm(int taskNum) {
        LOGGER.debug("Launching of the deleting confirmation for task " +
                controller.getTaskList().getTask(taskNum).getTitle());
        String taskName = controller.getTaskList().getTask(taskNum).getTitle();
        int response = JOptionPane.showConfirmDialog(new JPanel(),
                "Task " + taskName +
                " will be deleted. Confirm operation",
                "Task manager",
                JOptionPane.OK_CANCEL_OPTION);

        switch (response) {
            case JOptionPane.OK_OPTION:
                LOGGER.info("User agreed");
                controller.deleteTask(taskNum);
                controller.launchOperation(Operations.TASK_OUTPUT);
                break;
            case JOptionPane.CANCEL_OPTION:
                LOGGER.info("User canceled");
                controller.launchOperation(Operations.DELETE_TASK);
                break;
            default:
                LOGGER.info("Default option");
                controller.launchOperation(Operations.MAIN_MENU);
        }
    }

    public void infoMessage() {
        Object [] answers = {"Full list", "Calendar", "Back"};
        int response = JOptionPane.showOptionDialog(new JPanel(),
                "Select type of information",
                "Task manager",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                answers,
                answers[2]);

        switch (response) {
            case JOptionPane.YES_OPTION:
                LOGGER.info("User chose full list output");
                controller.launchOperation(Operations.TASK_OUTPUT);
                break;
            case JOptionPane.NO_OPTION:
                LOGGER.info("User chose calendar output");
                controller.launchOperation(Operations.CALENDAR_OUTPUT);
                break;
            default:
                LOGGER.info("User chose default option");
                controller.launchOperation(Operations.MAIN_MENU);
        }
    }
}
