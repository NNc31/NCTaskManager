package ua.edu.sumdu.j2se.nefodov.tasks;

import java.util.stream.Stream;

/**
 * abstract class for lists of tasks
 * contains abstract methods and size
 */
public abstract class AbstractTaskList implements Iterable, Cloneable {
    protected int size = 0;

    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract Task getTask(int index) throws IndexOutOfBoundsException;
    public abstract Stream<Task> getStream();
    public abstract ListTypes.types getType();

    public final AbstractTaskList incoming(int from, int to) throws IllegalArgumentException {
        if (from < 0) {
            throw new IllegalArgumentException();
        }

        AbstractTaskList inTime = TaskListFactory.createTaskList(getType());

        Stream<Task> stream = getStream();
        stream.forEach(task -> {
            if (task.nextTimeAfter(from) <= to && task.nextTimeAfter(from) != -1) inTime.add(task);
        });
        return inTime;
    }

    public int size(){
        return size;
    }
}
