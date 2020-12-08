package ua.edu.sumdu.j2se.nefodov.tasks;

import java.util.Objects;

/**
 * class for creating new tasks
 * have title and can be active or not active
 * done once or repeated with interval
 */

public class Task implements Cloneable {
    private String title;
    private boolean active;
    private int time;
    private int start;
    private int end;
    private int interval;

    /**
     * constructor for one-time tasks
     * @param title is name of the task
     * @param time is time when the task must be done
     * @throws IllegalArgumentException if time is less than 0
     */
    public Task(String title, int time) throws IllegalArgumentException {
        if (time < 0) {
            throw new IllegalArgumentException();
        } else {
            this.title = title;
            active = false;
            this.time = time;
            start = time;
            end = time;
            interval = 0;
        }
    }

    /**
     * constructor for repeated tasks
     * @param title is name of the task
     * @param start time of the start of repetition
     * @param end time of the end of repetition
     * @param interval time-interval between repetition
     * @throws IllegalArgumentException if start/end/interval is less than 0
     */
    public Task(String title, int start, int end, int interval) throws IllegalArgumentException {
        if (start < 0 || end < 0 || interval < 0) {
            throw new IllegalArgumentException();
        } else {
            this.title = title;
            active = false;
            time = start;
            this.start = start;
            this.end = end;
            this.interval = interval;
        }
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
        this.start = time;
        this.end = time;
    }

    public int getStartTime() {
        return start;
    }
    public int getEndTime() {
        return end;
    }
    public int getRepeatInterval() {
        if(start >= end) interval = 0;
        return interval;
    }
    public void setTime(int start, int end, int interval) {
        this.start = start;
        time = start;
        this.end = end;
        this.interval = interval;
    }
    public boolean isRepeated() {
        if (interval != 0 && start < end) return true;
        else return false;
    }

    /**
     * method of next time implementation
     * @param current time relative to which next time of the task is returned
     * @return -1 if there is no next time or  nextTime
     * @throws IllegalArgumentException if current is less than 0
     */
    public int nextTimeAfter(int current) throws IllegalArgumentException {
        if(current < 0) {
            throw new IllegalArgumentException();
        }
        if (active) {
            if(isRepeated()) {
                if(current < start) {
                    return start;
                }
                int nextRepetition = (current - start) / interval + 1;
                int nextTime = start + nextRepetition * interval;
                if(nextTime < end) return nextTime;
                else return -1;
            } else {
                if(current < time) return time;
                else return -1;
            }
        } else {
            return -1;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return active == task.active &&
                time == task.time &&
                start == task.start &&
                end == task.end &&
                interval == task.interval &&
                title.equals(task.title);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + title.hashCode();
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + time;
        result = 31 * result + start;
        result = 31 * result + end;
        result = 31 * result + interval;
        return result;
    }

    @Override
    public String toString() {
        if (interval != 0) {
            return "Task {" +
                    "title = '" + title + '\'' +
                    ", active = " + active +
                    ", start = " + start +
                    ", end = " + end +
                    ", interval = " + interval +
                    '}';
        } else {
            return "Task {" +
                    "title = '" + title + '\'' +
                    ", active = " + active +
                    ", time =" + time +
                    '}';
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Task task = new Task(title, time);
        task.active = active;
        task.start = start;
        task.end = end;
        task.interval = interval;
        return task;
    }
}
