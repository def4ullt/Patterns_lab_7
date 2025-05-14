import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        AppInjector appInjector = DaggerAppInjector.create();
        TaskManager taskManager = appInjector.getTaskManager();
        taskManager.loadTasks();
        boolean active = true;
        while (active) {
            System.out.println("Enter command:");

            String line = sc.nextLine().trim();
            String[] parts = line.split(" ", 2);
            String command = parts[0];

            switch (command) {
                case "exit": {
                    System.out.println("Whathever ¯\\_(ツ)_/¯");
                    Thread.sleep(1000);
                    System.out.println("Goodbye! ヾ(•ω•`)o");
                    taskManager.saveTasks();
                    active = false;
                    break;
                }
                case "help": {
                    System.out.println("help - shows this help message");
                    System.out.println("create - initialization of operation of creating new task");
                    System.out.println("delete <name> - deletes a task");
                    System.out.println("search <query> - searches tasks by name or text");
                    System.out.println("list - lists all tasks");
                    System.out.println("sort_name — show all tasks sorted by name");
                    System.out.println("sort_time — show all tasks sorted by date");
                    System.out.println("edit <name> - edits a task");
                    System.out.println("exit - exits the program");
                    break;
                }
                case "create": {
                    taskManager.createTask(sc);
                    break;
                }
                case "delete": {
                    if (parts.length > 1) {
                        String taskName = parts[1];
                        Task taskToDelete = taskManager.getAllTasks().stream()
                                .filter(task -> task.getName().equals(taskName))
                                .findFirst()
                                .orElse(null);

                        if (taskToDelete != null) {
                            taskManager.deleteTask(taskToDelete.getId());
                            System.out.println("Task deleted: " + taskToDelete);
                        } else {
                            System.out.println("Task not found.");
                        }
                    }
                    break;
                }
                case "search": {
                    if (parts.length > 1) {
                        String query = parts[1];
                        List<Task> results = taskManager.searchTasks(query);
                        if (!results.isEmpty()) {
                            taskManager.displayTasks(results);
                        } else {
                            System.out.println("No tasks found.");
                        }
                    } else {
                        System.out.println("Invalid search command. Use search <query>");
                    }
                    break;
                }
                case "sort_name":
                    taskManager.displayTasks(taskManager.sortByName());
                    break;
                case "sort_time":
                    taskManager.displayTasks(taskManager.sortByEndTime());
                    break;
                case "list":
                    taskManager.displayTasks(taskManager.getAllTasks());
                    break;
                case "edit": {
                    if (parts.length > 1) {
                        String taskName = parts[1];
                        Task taskToEdit = taskManager.getAllTasks().stream()
                                .filter(task -> task.getName().equals(taskName))
                                .findFirst()
                                .orElse(null);

                        if (taskToEdit != null) {
                            System.out.println("Enter new name (or press Enter to skip):");
                            String newName = sc.nextLine().trim();
                            if (!newName.isEmpty()) {
                                taskManager.updateTaskName(taskToEdit.getId(), newName);
                            }

                            System.out.println("Enter new text (or press Enter to skip):");
                            String newText = sc.nextLine().trim();
                            if (!newText.isEmpty()) {
                                taskManager.updateTaskText(taskToEdit.getId(), newText);
                            }

                            System.out.println("Enter new end time (HH.mm dd.MM.yyyy, or press Enter to skip):");
                            String newEndTimeStr = sc.nextLine().trim();
                            if (!newEndTimeStr.isEmpty()) {
                                try {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm dd.MM.yyyy");
                                    LocalDateTime newEndTime = LocalDateTime.parse(newEndTimeStr, formatter);
                                    taskManager.updateTaskEndTime(taskToEdit.getId(), newEndTime);
                                } catch (DateTimeParseException e) {
                                    System.out.println("Invalid date/time format.");
                                }
                            }
                            System.out.println("Task updated: " + taskManager.readTask(taskToEdit.getId()));
                        } else {
                            System.out.println("Task not found.");
                        }
                    } else {
                        System.out.println("Invalid edit command. Use edit <task_name>");
                    }
                    break;
                }
                default: {
                    System.out.println("Invalid command");
                    break;
                }
            }
        }
    }
}
