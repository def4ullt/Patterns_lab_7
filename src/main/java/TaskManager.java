import com.google.gson.*;

import javax.inject.Inject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    private final Gson gson;


    @Inject
    public TaskManager(Gson gson) {
        this.gson = gson;
    }

    public void createTask(Scanner sc) {

        System.out.println("Please enter the name of the task you would like to add(press ENTER to exit): ");
        String name = sc.nextLine();
        if(name == null || name.equals("")) {
            return;
        }
        else {

            System.out.println("Please enter the description of the task you would like to add: ");
            String text = sc.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            boolean isNotParsable = true;
            LocalDateTime endTime = null;
            while(isNotParsable)
            {

                System.out.println("Please enter end time (format: dd-MM-yyyy HH:mm):");
                String endTimeString = sc.nextLine().replace('.', ':');
                try {
                    endTime = LocalDateTime.parse(endTimeString, formatter);
                    isNotParsable = false;
                }
                catch(DateTimeParseException e) {
                    System.out.println("Please enter a valid end time (っ °Д °;)っ");
                    isNotParsable = true;
                }
            }
                   Task task = new Task(nextId++, name, text, endTime);
                   System.out.println("Task created: ");
                   tasks.add(task);
               }
            }


    public void saveTasks() {
        JsonArray jsonArray = new JsonArray();
        for (Task task : tasks) {
            jsonArray.add(task.getJsonObject());
        }
        try(FileWriter fileWriter = new FileWriter("tasks.json")) {
            gson.toJson(jsonArray, fileWriter);
        }
        catch(IOException e)
        {
            System.out.println("Error writing tasks.json:" + e.getMessage());
        }
    }

    public void loadTasks() {
        try(FileReader fileReader = new FileReader("tasks.json")) {
            File file = new File("tasks.json");
            if (!file.exists() || file.length() == 0)
            {
                System.out.println("No tasks found, maybe file is empty. 🤷‍♂️");
                return;
            }

            FileReader reader = new FileReader("tasks.json");
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray)
            {
                JsonObject taskObject = element.getAsJsonObject();
                int id = taskObject.get("id").getAsInt();
                if(id >= nextId)
                    nextId = id + 1;
                String name = taskObject.get("name").getAsString();
                String text = taskObject.get("text").getAsString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime endTime = LocalDateTime.parse(taskObject.get("endTime").getAsString(), formatter);


                Task task = new Task(id, name, text, endTime);
                tasks.add(task);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading tasks:" + e.getMessage());
        }
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    public Task getTask(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    // Методи сортування
    public List<Task> sortByName() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getName))
                .collect(Collectors.toList());
    }

    public List<Task> sortByEndTime() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getEndTime))
                .collect(Collectors.toList());
    }

    // Метод відображення
    public void displayTasks(List<Task> tasks) {
        tasks.forEach(System.out::println);
    }

    // Методи редагування
    public void updateTaskName(int id, String newName) {
        Task task = getTask(id);
        if (task != null) {
            task = new Task(task.getId(), newName, task.getText(), task.getEndTime());
            tasks.set(tasks.indexOf(getTask(id)), task);
        }
    }

    public void updateTaskText(int id, String newText) {
        Task task = getTask(id);
        if (task != null) {
            task = new Task(task.getId(), task.getName(), newText, task.getEndTime());
            tasks.set(tasks.indexOf(getTask(id)), task);
        }
    }

    public void updateTaskEndTime(int id, LocalDateTime newEndTime) {
        Task task = getTask(id);
        if (task != null) {
            task = new Task(task.getId(), task.getName(), task.getText(), newEndTime);
            tasks.set(tasks.indexOf(getTask(id)), task);
        }
    }

    public List<Task> searchTasks(String query) {
        return tasks.stream()
                .filter(task -> task.getName().toLowerCase().contains(query.toLowerCase()) ||
                        task.getText().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Task readTask(int id) {
        return getTask(id);
    }
}