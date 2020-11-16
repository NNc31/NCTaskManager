package ua.edu.sumdu.j2se.nefodov.tasks;

/**
 * class for creating arrays of tasks
 * contains an array of tasks and size of the array
 */
public class ArrayTaskList {
    private int size = 0;
    private Task array[];

    /**
     * method of adding new tasks
     * @param task is task to add
     */
    public void add(Task task){
        Task[] newArray = new Task[size + 1];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        newArray[size] = task;
        array = newArray;
        size++;
    }

    /**
     * method to remove tasks
     * @param task is task to remove
     * @return true if task was removed, otherwise - false
     */
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

    public int size(){
        return size;
    }

    /**
     * method to get task
     * @param index is position from zero of needed task
     * @return needed task or null if there is no such task
     */
    public Task getTask(int index) {
        if(index >= size || index < 0){
            return null;
        }
        return array[index];
    }

    /**
     * method to get an array list of tasks that executed
     * @param from is time from which tasks are included
     * @param to is time after which tasks are not included
     * @return array list of tasks
     */
    public ArrayTaskList incoming(int from, int to){
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