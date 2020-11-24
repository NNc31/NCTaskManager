package ua.edu.sumdu.j2se.nefodov.tasks;

/**
 * class for creating linked list of tasks
 * contains pointer to head and tail of linked list
 */
public class LinkedTaskList extends AbstractTaskList {
    private Node head;
    private Node tail;

    /**
     * inner class for node of list
     * contains task and pointer for next and previous element
     */
    private class Node {
        private Task task;
        private Node next;

        /**
         * constructor that assigns task to node
         * invoked in function of adding new tasks
         * @param task is new task to assign to node
         */
        Node(Task task) {
            this.task = task;
            next = null;
        }
    }

    /**
     * method to add new tasks
     * @param task is new task to add
     */
    @Override
    public void add(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /**
     * method to remove tasks
     * @param task is task to remove
     * @return true if task was removed, otherwise - false
     */
    @Override
    public boolean remove(Task task) {
        if (head == null) return false;

        if (head.task == task) {
            head = head.next;
            size--;
            return true;
        }

        Node node = head;
        while (node != tail) {
            if (node.next.task == task) {
                if (node.next == tail) tail = node;
                node.next = node.next.next;
                size--;
                return true;
            }
            node = node.next;
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
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return head.task;
        }
        if (index == size - 1) {
            return tail.task;
        }

        Node node = head.next;
        int i = 1;
        while (node != null) {
            if (i == index) {
                return node.task;
            }
            node = node.next;
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * method to get a list of tasks that executed
     * @param from is time from which tasks are included
     * @param to is time after which tasks are not included
     * @return linked list of tasks
     * @throws IllegalArgumentException if from is less than 0
     */
    @Override
    public LinkedTaskList incoming(int from, int to) throws IllegalArgumentException {
        if (from < 0) {
            throw new IllegalArgumentException();
        }
        LinkedTaskList inTime = new LinkedTaskList();
        int nextTime;
        Node node = head;
        while (node != null) {
            nextTime = node.task.nextTimeAfter(from);
            if (nextTime > from && nextTime <= to) {
                inTime.add(node.task);
            }
            node = node.next;
        }
        return inTime;
    }
}
