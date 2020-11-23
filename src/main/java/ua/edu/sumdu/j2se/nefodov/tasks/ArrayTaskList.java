package ua.edu.sumdu.j2se.nefodov.tasks;

/**
 * class for creating arrays of tasks.
 * contains an array of tasks and initialization size
 */
public class ArrayTaskList extends AbstractTaskList {
    private final int INIT_SIZE = 5;
    private Task array[] = new Task[INIT_SIZE];

    /**
     * method to add new tasks
     * increases size of arraylist in half
     * if bound of the array is reached
     * @param task is new task to add
     */
    @Override
    public void add(Task task){
        if(size == array.length){
            Task[] newArray = new Task[size + size / 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }
        array[size] = task;
        size++;
    }

    /**
     * method to remove tasks
     * @param task is task to remove
     * @return true if task was removed, otherwise - false
     */
    @Override
    public boolean remove(Task task){
        for(int i = 0; i < size; i++){
            if(array[i] == task){
                for(int j = i + 1; j < size; j++, i++){
                    array[i] = array[j];
                }
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * method to get task
     * @param index is position from zero of needed task
     * @return needed task by index
     * @throws IndexOutOfBoundsException if index of task is incorrect
     */
    @Override
    public Task getTask(int index) throws IndexOutOfBoundsException {
        if(index >= size || index < 0){
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    /**
     * method to get an array list of tasks that executed
     * @param from is time from which tasks are included
     * @param to is time after which tasks are not included
     * @return array list of tasks
     * @throws IllegalArgumentException if from is less than 0
     */
    @Override
    public ArrayTaskList incoming(int from, int to) throws IllegalArgumentException{
        if(from < 0) {
            throw new IllegalArgumentException();
        }
        ArrayTaskList inTime = new ArrayTaskList();
        int nextTime;
        for(int i = 0; i < size; i++){
            nextTime = array[i].nextTimeAfter(from);
            if(nextTime > from && nextTime <= to) {
                inTime.add(array[i]);
            }
        }
        return inTime;
    }
}
