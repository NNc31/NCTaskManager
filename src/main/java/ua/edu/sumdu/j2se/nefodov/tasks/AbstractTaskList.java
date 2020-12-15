package ua.edu.sumdu.j2se.nefodov.tasks;

/**
 * abstract class for lists of tasks
 * contains abstract methods and size
 */
public abstract class AbstractTaskList implements Iterable, Cloneable {
    protected int size = 0;

    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract Task getTask(int index) throws IndexOutOfBoundsException;
    public abstract AbstractTaskList incoming(int from, int to);

    public int size(){
        return size;
    }
}
