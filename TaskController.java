package controller;

import model.Task;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskController {
    private List<Task> tasks = new ArrayList<>();
    private final String FILE_NAME = "tasks.dat";

    public TaskController() {
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            saveTasks();
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void completeTask(int index, boolean completed) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).setCompleted(completed);
            saveTasks();
        }
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (List<Task>) ois.readObject();
        } catch (FileNotFoundException e) {
            tasks = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            tasks = new ArrayList<>();
        }
    }
}
