package ua.edu.sumdu.j2se.nefodov.tasks.controller;

import ua.edu.sumdu.j2se.nefodov.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.nefodov.tasks.model.LinkedTaskList;
import ua.edu.sumdu.j2se.nefodov.tasks.model.Task;
import ua.edu.sumdu.j2se.nefodov.tasks.model.Tasks;
import ua.edu.sumdu.j2se.nefodov.tasks.view.UserView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.SortedMap;

import static ua.edu.sumdu.j2se.nefodov.tasks.model.TaskIO.readBinary;
import static ua.edu.sumdu.j2se.nefodov.tasks.model.TaskIO.writeBinary;

public class UserController {

    private AbstractTaskList taskList = new LinkedTaskList();
    private UserView view = null;

    public File loadFile() {
        File readFrom = new File("FileToLoad.bin");
        if (readFrom.exists() && readFrom.canRead() && readFrom.length() != 0) {
            File file = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(readFrom), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                file = new File(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        } else {
            throw new IllegalStateException();
        }
    }

    public void setFile(File file) {
        File readFrom = new File("FileToLoad.bin");
        try {
            readFrom.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fos = new FileOutputStream(readFrom)) {
            fos.write(file.getAbsolutePath().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadList(File file) {
        try {
            file.createNewFile();
            if (file.length() != 0) {
                readBinary(taskList, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveList(File file) {
        try {
            if (file.exists() && file.delete() && file.createNewFile()) {
                writeBinary(taskList, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchOperation(UserOperations op) {
        switch (op) {
            case MAIN_MENU:
                view.mainMenu();
                break;
            case ADD_MENU:
                view.addMenu();
                break;
            case EDIT_TASK:
                view.editTask();
                break;
            case DELETE_TASK:
                view.deleteTask();
                break;
            case INFO_MENU:
                view.infoMessage();
                break;
            case TASK_OUTPUT:
                view.taskOutput();
                break;
            case CALENDAR_OUTPUT:
                view.calendarOutput();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void launchOperation(UserOperations op, int taskNum) {
        switch (op) {
            case EDIT_MENU:
                view.editMenu(taskNum);
                break;
            case DELETE_MENU:
                view.deleteConfirm(taskNum);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean checkTask(String title, String startStr, String endStr, String intervalStr) {
        return checkTitle(title) && checkTime(startStr, endStr) && checkInterval(intervalStr);
    }

    public boolean checkTask(String title, String timeStr) {
        return checkTitle(title) && checkTime(timeStr);
    }

    public boolean checkFile(File file) {
        return file != null && file.exists() && file.length() != 0 && file.canRead();
    }

    public boolean checkTaskNum(String str) {
        int taskNum;
        try {
            taskNum = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            taskNum = 0;
        }
        return taskNum > 0 && taskNum <= taskList.size();
    }

    public boolean checkTitle(String title) {
        return !title.trim().isEmpty();
    }

    public boolean checkTime(String timeStr) {
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
        return checkTime(startStr) && checkTime(endStr) && !parseTime(startStr).isAfter(parseTime(endStr));
    }

    public boolean checkTime(String startStr, int taskNum) {
        return checkTime(startStr) && !parseTime(startStr).isAfter(taskList.getTask(taskNum).getEndTime());
    }

    public boolean checkTime(int taskNum, String endStr) {
        return checkTime(endStr) && !parseTime(endStr).isBefore(taskList.getTask(taskNum).getStartTime());
    }

    public boolean checkInterval(String intervalStr) {
        int interval;
        try {
            interval = Integer.parseInt(intervalStr);
        } catch (NumberFormatException e1) {
            interval = 0;
        }
        return interval > 0;
    }

    public LocalDateTime parseTime(String timeStr) {
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
        Task task = new Task(title, parseTime(timeStr));
        task.setActive(active);
        taskList.add(task);
    }

    public void addTask(String title, boolean active, String startStr, String endStr, String intervalStr) {
        Task task = new Task(title, parseTime(startStr), parseTime(endStr), Integer.parseInt(intervalStr));
        task.setActive(active);
        taskList.add(task);
    }

    public void editTitle(String title, int taskNum) {
        taskList.getTask(taskNum).setTitle(title);
    }

    public void editActive(boolean active, int taskNum) {
        taskList.getTask(taskNum).setActive(active);
    }

    public void editTime(String time, int taskNum) {
        taskList.getTask(taskNum).setTime(parseTime(time));
    }

    public void editStartTime(String startTime, int taskNum) {
        Task task = taskList.getTask(taskNum);
        task.setTime(parseTime(startTime), task.getEndTime(), task.getRepeatInterval());
    }

    public void editEndTime(String endTime, int taskNum) {
        Task task = taskList.getTask(taskNum);
        task.setTime(task.getStartTime(), parseTime(endTime), task.getRepeatInterval());
    }

    public void editInterval(String interval, int taskNum) {
        Task task = taskList.getTask(taskNum);
        task.setTime(task.getStartTime(), task.getEndTime(), Integer.parseInt(interval));
    }

    public AbstractTaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(AbstractTaskList taskList) {
        this.taskList = taskList;
    }

    public UserView getView() {
        return view;
    }

    public void setView(UserView view) {
        this.view = view;
    }

    public String getCalendar(String startStr, String endStr) {
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
}
