package ua.edu.sumdu.j2se.nefodov.tasks;

/**
 * class for creating list of task of wished type
 */
public class TaskListFactory {

    /**
     * static method for creating list of tasks
     *
     * @param type is type of the list - array list or linked list
     * @return list of needed type
     */
    public static AbstractTaskList createTaskList(ListTypes.types type) {
        switch (type) {
            case ARRAY:
                ArrayTaskList arrayTaskList = new ArrayTaskList();
                return arrayTaskList;
            case LINKED:
                LinkedTaskList linkedTaskList = new LinkedTaskList();
                return linkedTaskList;
            default:
                ArrayTaskList taskList = new ArrayTaskList();
                return taskList;
        }
    }
}
