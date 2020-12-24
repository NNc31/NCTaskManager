package ua.edu.sumdu.j2se.nefodov.tasks;

import com.google.gson.Gson;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;

public class TaskIO {
    public static void write(AbstractTaskList tasks, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeInt(tasks.size());
        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            try {
                Task t = it.next();
                dos.writeInt(t.getTitle().length());
                dos.writeUTF(t.getTitle());
                dos.writeBoolean(t.isActive());
                dos.writeInt(t.getRepeatInterval());
                if(t.isRepeated()) {
                    dos.writeLong(t.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    dos.writeLong(t.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                } else {
                    dos.writeLong(t.getTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void read(AbstractTaskList tasks, InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int size = dis.readInt();
        while (size > 0) {
            int titleSize = dis.readInt();
            String title = dis.readUTF();
            boolean active = dis.readBoolean();
            int interval = dis.readInt();
            Task t;
            if (interval != 0) {
                LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(dis.readLong()), ZoneId.systemDefault());
                LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(dis.readLong()), ZoneId.systemDefault());
                t = new Task(title, start, end, interval);
            } else {
                LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(dis.readLong()), ZoneId.systemDefault());
                t = new Task(title, time);
            }
            t.setActive(active);
            tasks.add(t);
            size--;
        }
    }
    public static void writeBinary(AbstractTaskList tasks, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            write(tasks, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readBinary(AbstractTaskList tasks, File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            read(tasks, fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void write(AbstractTaskList tasks, Writer out) throws IOException {
        Gson gson = new Gson();
        ArrayTaskList tempList = new ArrayTaskList();
        tasks.getStream().forEach(tempList::add);
        gson.toJson(tempList, out);
        out.flush();
    }
    public static void read(AbstractTaskList tasks, Reader in) {
        Gson gson = new Gson();
        ArrayTaskList tempList = gson.fromJson(in, ArrayTaskList.class);
        tempList.getStream().forEach(tasks::add);
    }
    public static void writeText(AbstractTaskList tasks, File file) {
        Gson gson = new Gson();
        try (FileWriter fw = new FileWriter(file)) {
            write(tasks, fw);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readText(AbstractTaskList tasks, File file) {
        Gson gson = new Gson();
        try (FileReader fr = new FileReader(file)) {
            read(tasks, fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
