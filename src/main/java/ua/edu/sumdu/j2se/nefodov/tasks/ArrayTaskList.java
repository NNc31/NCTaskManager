package ua.edu.sumdu.j2se.nefodov.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

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

    @Override
    public Stream<Task> getStream(){
        ArrayList<Task> arrayList = new ArrayList<Task>(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(array[i]);
        }
        return arrayList.stream();
    }

    @Override
    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Task next() {
                return array[index++];
            }

            @Override
            public void remove() {
                if (index == 0) {
                    throw new IllegalStateException();
                } else {
                    ArrayTaskList.this.remove(array[--index]);
                }
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTaskList arrayTaskList = (ArrayTaskList) o;
        int amount = 0;
        for (int i = 0; i < size; i++) {
            if(array[i].equals(arrayTaskList.array[i])) amount++;
        }
        if (amount == size) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for(int i = 0; i < size; i++) {
            result += array[i].hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < size; i++){
            str += array[i].toString() + "\n";
        }
        return str;
    }

    @Override
    public ArrayTaskList clone() {
        ArrayTaskList arrayTaskList = new ArrayTaskList();
        for (int i = 0; i < size; i++) {
            arrayTaskList.add(array[i]);
        }
        return arrayTaskList;
    }

    @Override
    public ListTypes.types getType(){
        return ListTypes.types.ARRAY;
    }
}
