package ua.edu.sumdu.j2se.nefodov.tasks.model;

import javax.swing.*;
import java.util.Set;
import java.util.TimerTask;

public class Notification extends TimerTask {
    private Set<Task> notifySet;

    public Notification(Set<Task> notifySet) {
        this.notifySet = notifySet;
    }

    public String createMessage() {
        String message = "It`s time to:\n";
        for (Task task : notifySet) {
            message += task.getTitle() + '\n';
        }
        return message;
    }

    @Override
    public void run() {
        JOptionPane.showMessageDialog(new JPanel(),
                createMessage(),
                "Task manager notification",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
