package ua.edu.sumdu.j2se.nefodov.tasks;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * abstract class for lists of tasks
 * contains abstract methods and size
 */
public abstract class AbstractTaskList implements Iterable, Cloneable, Serializable {
    protected int size = 0;

    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract Task getTask(int index) throws IndexOutOfBoundsException;
    public abstract Stream<Task> getStream();
    public abstract ListTypes.types getType();

    public int size(){
        return size;
    }
}
