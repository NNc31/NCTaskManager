package ua.edu.sumdu.j2se.nefodov.tasks;

import java.util.Iterator;

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

    @Override
    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            private Node nextNode = head;
            private Node prevNode = null;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public Task next() {
                prevNode = nextNode;
                nextNode = nextNode.next;
                return prevNode.task;
            }

            @Override
            public void remove() {
                if (prevNode == null) {
                   throw new IllegalStateException();
                } else {
                    LinkedTaskList.this.remove(prevNode.task);
                    prevNode = null;
                }
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedTaskList linkedTaskList = (LinkedTaskList) o;
        int amount = 0;
        for (int i = 0; i < size; i++) {
            if (getTask(i).equals(linkedTaskList.getTask(i))) amount++;
        }
        if (amount == size) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for(int i = 0; i < size; i++) {
            result += getTask(i).hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < size; i++){
            str += getTask(i).toString() + "\n";
        }
        return str;
    }

    @Override
    public LinkedTaskList clone() {
        LinkedTaskList linkedTaskList = new LinkedTaskList();
        for (int i = 0; i < size; i++) {
            linkedTaskList.add(getTask(i));
        }
        return linkedTaskList;
    }
}
