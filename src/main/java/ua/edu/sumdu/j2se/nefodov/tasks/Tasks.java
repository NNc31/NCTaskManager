package ua.edu.sumdu.j2se.nefodov.tasks;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.TreeMap;


public class Tasks {
    public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) throws IllegalArgumentException {
        if (start.isAfter(end)) {
           throw new IllegalArgumentException();
        }

        Iterator<Task> it = tasks.iterator();
        AbstractTaskList inTime = TaskListFactory.createTaskList(ListTypes.types.ARRAY);

        while (it.hasNext()) {
            Task task = it.next();
            if (task.nextTimeAfter(start) != null && !task.nextTimeAfter(start).isAfter(end)) {
                inTime.add(task);
            }
        }
        return inTime;
    }

    public static SortedMap<LocalDateTime, Set<Task>> calendar(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
        SortedMap<LocalDateTime, Set<Task>> calendar = new TreeMap<LocalDateTime, Set<Task>>();
        Iterable<Task> inTime = incoming(tasks, start, end);
        Iterator<Task> it = inTime.iterator();

        while (it.hasNext()) {
            Task task = it.next();
            LocalDateTime temp = task.nextTimeAfter(start);
            if (task.isRepeated()) {
                while (!temp.isAfter(end)) {
                    if (!(calendar.containsKey(temp))) {
                        calendar.put(temp, new HashSet<Task>());
                    }
                    calendar.get(temp).add(task);
                    temp = temp.plusSeconds(task.getRepeatInterval());
                }
            } else {
                if (!(calendar.containsKey(temp))) {
                    calendar.put(temp, new HashSet<Task>());
                }
                calendar.get(temp).add(task);
            }
        }

        return calendar;
    }
}
