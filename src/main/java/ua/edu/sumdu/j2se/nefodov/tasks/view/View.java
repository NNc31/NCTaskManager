package ua.edu.sumdu.j2se.nefodov.tasks.view;

import ua.edu.sumdu.j2se.nefodov.tasks.controller.Operations;
import ua.edu.sumdu.j2se.nefodov.tasks.controller.Controller;

import javax.swing.*;

public class View {
    private Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void mainMenu() {
        MainMenu mainMenu = new MainMenu(controller);
        mainMenu.pack();
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
    }

    public void addMenu() {
        AddMenu addMenu = new AddMenu(controller);
        addMenu.pack();
        addMenu.setLocationRelativeTo(null);
        addMenu.setVisible(true);
    }

    public void editTask() {
        taskSelection(Operations.EDIT_MENU);
    }

    public void editMenu(int n) {
        EditMenu editMenu = new EditMenu(controller, n);
        editMenu.pack();
        editMenu.setLocationRelativeTo(null);
        editMenu.setVisible(true);
    }

    public void deleteTask() {
        taskSelection(Operations.DELETE_MENU);
    }

    public void deleteConfirm(int taskNum) {
        String taskName = controller.getTaskList().getTask(taskNum).getTitle();
        int response = JOptionPane.showConfirmDialog(new JPanel(),
                "Task " + taskName +
                " will be deleted. Confirm operation",
                "Task manager",
                JOptionPane.OK_CANCEL_OPTION);

        switch (response) {
            case JOptionPane.OK_OPTION:
                controller.deleteTask(taskNum);
                controller.launchOperation(Operations.TASK_OUTPUT);
                break;
            case JOptionPane.CANCEL_OPTION:
                controller.launchOperation(Operations.DELETE_TASK);
                break;
            default:
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
                controller.launchOperation(Operations.TASK_OUTPUT);
                break;
            case JOptionPane.NO_OPTION:
                controller.launchOperation(Operations.CALENDAR_OUTPUT);
                break;
            default:
                controller.launchOperation(Operations.MAIN_MENU);
        }
    }

    public void taskOutput() {
        TaskOutput taskOutput = new TaskOutput(controller);
        taskOutput.pack();
        taskOutput.setLocationRelativeTo(null);
        taskOutput.setVisible(true);
    }

    public void taskSelection(Operations operation) {
        TaskSelection taskSelection = new TaskSelection(controller, operation);
        taskSelection.pack();
        taskSelection.setLocationRelativeTo(null);
        taskSelection.setVisible(true);
    }

    public void calendarOutput() {
        CalendarOutput calendarOutput = new CalendarOutput(controller);
        calendarOutput.pack();
        calendarOutput.setLocationRelativeTo(null);
        calendarOutput.setVisible(true);
    }
}
