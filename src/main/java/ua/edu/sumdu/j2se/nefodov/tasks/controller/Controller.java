package ua.edu.sumdu.j2se.nefodov.tasks.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.edu.sumdu.j2se.nefodov.tasks.model.*;
import ua.edu.sumdu.j2se.nefodov.tasks.view.View;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Timer;

import static ua.edu.sumdu.j2se.nefodov.tasks.model.TaskIO.readBinary;
import static ua.edu.sumdu.j2se.nefodov.tasks.model.TaskIO.writeBinary;

public class Controller {

    private AbstractTaskList taskList = new LinkedTaskList();
    private View view = null;
    private LinkedList<Timer> timers = new LinkedList<>();

    public static final Logger LOGGER = LogManager.getLogger(Controller.class);

    public void launchOperation(Operations op) {
        switch (op) {
            case MAIN_MENU:
                LOGGER.info("Launch of the main menu");
                view.mainMenu();
                break;
            case ADD_MENU:
                LOGGER.info("Launch of the add menu");
                view.addMenu();
                break;
            case EDIT_TASK:
                LOGGER.info("Launch of the editing process");
                view.editTask();
                break;
            case DELETE_TASK:
                LOGGER.info("Launch of the deleting process");
                view.deleteTask();
                break;
            case INFO_MENU:
                LOGGER.info("Launch of the info message");
                view.infoMessage();
                break;
            case TASK_OUTPUT:
                LOGGER.info("Launch of the task output");
                view.taskOutput();
                break;
            case CALENDAR_OUTPUT:
                LOGGER.info("Launch of the calendar of tasks output");
                view.calendarOutput();
                break;
            default:
                LOGGER.error("Illegal argument received in launch process");
                throw new IllegalArgumentException();
        }
    }

    public void launchOperation(Operations op, int taskNum) {
        switch (op) {
            case EDIT_MENU:
                LOGGER.info("Launch of the edit menu");
                view.editMenu(taskNum);
                break;
            case DELETE_MENU:
                LOGGER.info("Launch of the delete menu");
                view.deleteConfirm(taskNum);
                break;
            default:
                LOGGER.error("Illegal argument received in launch process");
                throw new IllegalArgumentException();
        }
    }

    public boolean checkTask(String title, String startStr, String endStr, String intervalStr) {
        LOGGER.debug("Checking repeated task");
        return checkTitle(title) && checkTime(startStr, endStr) && checkInterval(intervalStr);
    }

    public boolean checkTask(String title, String timeStr) {
        LOGGER.debug("Checking one-time task");
        return checkTitle(title) && checkTime(timeStr);
    }

    public boolean checkFile(File file) {
        LOGGER.debug("Checking file");
        return file != null && file.canRead() && file.exists();
    }

    public boolean checkTaskNum(String str) {
        LOGGER.debug("Checking task number");
        int taskNum;
        try {
            taskNum = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            taskNum = 0;
        }
        return taskNum > 0 && taskNum <= taskList.size();
    }

    public boolean checkTitle(String title) {
        LOGGER.debug("Checking task title");
        return !title.trim().isEmpty();
    }

    public boolean checkTime(String timeStr) {
        LOGGER.debug("Checking time");
        LocalDateTime time;
        try {
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
            time = LocalDateTime.parse(timeStr, formatter1);
        } catch (DateTimeParseException e1) {
            try {
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
                time = LocalDateTime.parse(timeStr, formatter2);
            } catch (DateTimeParseException e2) {
                time = null;
            }
        }
        return time != null;
    }

    public boolean checkTime(String startStr, String endStr) {
        LOGGER.debug("Checking start and finish time");
        return checkTime(startStr) && checkTime(endStr) && !parseTime(startStr).isAfter(parseTime(endStr));
    }

    public boolean checkTime(String startStr, int taskNum) {
        LOGGER.debug("Checking start time");
        return checkTime(startStr) && !parseTime(startStr).isAfter(taskList.getTask(taskNum).getEndTime());
    }

    public boolean checkTime(int taskNum, String endStr) {
        LOGGER.debug("Checking end time");
        return checkTime(endStr) && !parseTime(endStr).isBefore(taskList.getTask(taskNum).getStartTime());
    }

    public boolean checkInterval(String intervalStr) {
        LOGGER.debug("Checking interval");
        int interval;
        try {
            interval = Integer.parseInt(intervalStr);
        } catch (NumberFormatException e1) {
            interval = 0;
        }
        return interval > 0;
    }

    public LocalDateTime parseTime(String timeStr) {
        LOGGER.debug("Parsing time " + timeStr);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        LocalDateTime time;
        try {
            time = LocalDateTime.parse(timeStr, formatter1);
        } catch (DateTimeParseException e) {
            time = LocalDateTime.parse(timeStr, formatter2);
        }
        return time;
    }

    public void addTask(String title, boolean active, String timeStr) {
        LOGGER.info("Adding new one-time task " + title);
        Task task = new Task(title, parseTime(timeStr));
        task.setActive(active);
        taskList.add(task);
        updateTimers();
    }

    public void addTask(String title, boolean active, String startStr, String endStr, String intervalStr) {
        LOGGER.info("Adding new repeated task " + title);
        Task task = new Task(title, parseTime(startStr), parseTime(endStr), Integer.parseInt(intervalStr));
        task.setActive(active);
        taskList.add(task);
        updateTimers();
    }

    public void editTitle(String title, int taskNum) {
        LOGGER.info("Editing title to " + title + " for task #" + taskNum);
        taskList.getTask(taskNum).setTitle(title);
        updateTimers();
    }

    public void editActive(boolean active, int taskNum) {
        LOGGER.info("Editing active to " + active + " for task #" + taskNum);
        taskList.getTask(taskNum).setActive(active);
        updateTimers();
    }

    public void editTime(String time, int taskNum) {
        LOGGER.info("Editing time to " + time + " for task #" + taskNum);
        taskList.getTask(taskNum).setTime(parseTime(time));
        updateTimers();
    }

    public void editStartTime(String startTime, int taskNum) {
        LOGGER.info("Editing start time to " + startTime + " for task #" + taskNum);
        Task task = taskList.getTask(taskNum);
        task.setTime(parseTime(startTime), task.getEndTime(), task.getRepeatInterval());
        updateTimers();
    }

    public void editEndTime(String endTime, int taskNum) {
        LOGGER.info("Editing end time to " + endTime + " for task #" + taskNum);
        Task task = taskList.getTask(taskNum);
        task.setTime(task.getStartTime(), parseTime(endTime), task.getRepeatInterval());
        updateTimers();
    }

    public void editInterval(String interval, int taskNum) {
        LOGGER.info("Editing interval to " + interval + " for task #" + taskNum);
        Task task = taskList.getTask(taskNum);
        task.setTime(task.getStartTime(), task.getEndTime(), Integer.parseInt(interval));
        updateTimers();
    }

    public void deleteTask(int taskNum) {
        LOGGER.info("Deleting of the task #" + taskNum);
        taskList.remove(taskList.getTask(taskNum));
        updateTimers();
    }

    public AbstractTaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(AbstractTaskList taskList) {
        this.taskList = taskList;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getCalendar(String startStr, String endStr) {
        LOGGER.info("Creating calendar info from " + startStr + " to " + endStr);
        SortedMap<LocalDateTime, Set<Task>> map = Tasks.calendar(taskList, parseTime(startStr), parseTime(endStr));
        StringBuilder builder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        for (LocalDateTime date : map.keySet()) {
            builder.append(date.format(formatter)).append("\n");
            for (Task task : map.get(date)) {
                builder.append(task.toString() + "\n");
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public File getFile() {
        LOGGER.debug("Returning file with tasks");
        File location = new File("ListLocation.txt");
        File file;
        try {
            location.createNewFile();
        } catch (IOException e) {
            LOGGER.error("IOException caught while creating file \"ListLocation.txt\"");
            e.printStackTrace();
        }

        if (location.length() == 0) {
            file = new File("tasks.bin");
        } else {
            StringBuilder builder = new StringBuilder();
            try (FileReader reader = new FileReader(location)) {
                int ch;
                while ((ch = reader.read()) != -1) {
                    builder.append((char) ch);
                }
            } catch (IOException e) {
                LOGGER.error("IOException caught while reading file location");
                e.printStackTrace();
            }
            file = new File(builder.toString());
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IOException caught while creating file " + file.getAbsolutePath());
        }

        return file;
    }

    public void setFile(File file) {
        LOGGER.info("Setting new file with task - " + file.getAbsolutePath());
        File location = new File("ListLocation.txt");
        try {
            location.createNewFile();
        } catch (IOException e) {
            LOGGER.error("IOException caught while creating file \"ListLocation.txt\"");
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(location)) {
            writer.write(file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("IOException caught while writing file name");
            e.printStackTrace();
        }
    }

    public void loadList(File file) {
        LOGGER.info("Loading the tasks");
        if (file.length() != 0) {
            readBinary(taskList, file);
        }
    }

    public void saveList(File file) {
        LOGGER.info("Saving the tasks");
        writeBinary(taskList, file);
    }

    public void clearList() {
        LOGGER.info("Clearing the task list");
        taskList = new LinkedTaskList();
        updateTimers();
    }

    public void setTimers() {
        LOGGER.info("Setting timers");
        clearTimers();
        SortedMap<LocalDateTime, Set<Task>> map = Tasks.calendar(taskList,
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        for (LocalDateTime date : map.keySet()) {
            Timer timer = new Timer();
            timer.schedule(new Notification(map.get(date)), Date.from
                    (date.atZone(ZoneId.systemDefault()).toInstant()));
            timers.add(timer);
        }
    }

    public void clearTimers() {
        LOGGER.debug("Timers clearing");
        for (Timer timer : timers) {
            timer.cancel();
        }
    }

    public void updateTimers() {
        LOGGER.debug("Timers updating");
        clearTimers();
        setTimers();
    }
}
