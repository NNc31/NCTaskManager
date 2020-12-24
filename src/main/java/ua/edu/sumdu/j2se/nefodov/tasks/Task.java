package ua.edu.sumdu.j2se.nefodov.tasks;

import java.time.LocalDateTime;

/**
 * class for creating new tasks
 * have title and can be active or not active
 * done once or repeated with interval
 */

public class Task implements Cloneable {
    private String title;
    private boolean active;
    private LocalDateTime time;
    private LocalDateTime start;
    private LocalDateTime end;
    private int interval;

    public Task(String title, LocalDateTime time) throws IllegalArgumentException {
        if (time == null) {
            throw new IllegalArgumentException();
        } else {
            this.title = title;
            active = false;
            this.time = cloneTime(time);
            start = cloneTime(time);
            end = cloneTime(time);
            interval = 0;
        }
    }

    public Task(String title, LocalDateTime start, LocalDateTime end, int interval) throws IllegalArgumentException {
        if (start == null || end == null || interval < 0) {
            throw new IllegalArgumentException();
        } else {
            this.title = title;
            active = false;
            time = cloneTime(start);
            this.start = cloneTime(start);
            this.end = cloneTime(end);
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = cloneTime(time);
        this.start = cloneTime(time);
        this.end = cloneTime(time);
        interval = 0;
    }

    public LocalDateTime getStartTime() {
        return start;
    }

    public LocalDateTime getEndTime() {
        return end;
    }

    public int getRepeatInterval() {
        return interval;
    }

    public void setTime(LocalDateTime start, LocalDateTime end, int interval) {
        this.start = cloneTime(start);
        time = cloneTime(start);
        this.end = cloneTime(end);
        this.interval = interval;
    }

    public boolean isRepeated() {
        if (interval != 0) return true;
        else return false;
    }

    public LocalDateTime nextTimeAfter(LocalDateTime current) throws IllegalArgumentException {
        if(current == null) {
            throw new IllegalArgumentException();
        }
        if (active) {
            if(isRepeated()) {
                LocalDateTime nextRepetition = cloneTime(start);
                while(nextRepetition.isBefore(current) || nextRepetition.equals(current)) {
                    nextRepetition = nextRepetition.plusSeconds(interval);
                }
                if(nextRepetition.isAfter(end)) return null;
                else return nextRepetition;
            } else {
                if(current.isBefore(time)) return time;
                else return null;
            }
        } else {
            return null;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return active == task.active &&
                time.equals(task.time) &&
                start.equals(task.start) &&
                end.equals(task.end) &&
                interval == task.interval &&
                title.equals(task.title);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + title.hashCode();
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + time.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
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
        task.start = cloneTime(start);
        task.end = cloneTime(end);
        task.interval = interval;
        return task;
    }

    public LocalDateTime cloneTime(LocalDateTime time) {
        return LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond(), time.getNano());
    }
}
