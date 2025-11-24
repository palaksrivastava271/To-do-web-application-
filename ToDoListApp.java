import java.util.ArrayList;
import java.util.Scanner;

public class ToDoListApp {

    // Simple Task class to hold our task details
    static class Task {
        String description;
        boolean isCompleted;

        public Task(String description) {
            this.description = description;
            this.isCompleted = false;
        }

        public void markCompleted() {
            this.isCompleted = true;
        }

        @Override
        public String toString() {
            // Returns [X] if completed, [ ] if not
            return (isCompleted ? "[X] " : "[ ] ") + description;
        }
    }

    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to your To-Do List!");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    removeTask();
                    break;
                case "3":
                    viewTasks();
                    break;
                case "4":
                    markTaskCompleted();
                    break;
                case "5":
                    running = false;
                    System.out.println("Goodbye! Have a productive day.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println(); // Empty line for spacing
        }
    }

    private static void printMenu() {
        System.out.println("-------------------------");
        System.out.println("1. Add a new task");
        System.out.println("2. Remove a task");
        System.out.println("3. View all tasks");
        System.out.println("4. Mark task as completed");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addTask() {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        if (!description.trim().isEmpty()) {
            tasks.add(new Task(description));
            System.out.println("Task added successfully!");
        } else {
            System.out.println("Task description cannot be empty.");
        }
    }

    private static void removeTask() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to remove.");
            return;
        }
        viewTasks();
        System.out.print("Enter the number of the task to remove: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < tasks.size()) {
                Task removed = tasks.remove(index);
                System.out.println("Removed task: " + removed.description);
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Your to-do list is empty.");
        } else {
            System.out.println("Your Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    private static void markTaskCompleted() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks to mark as completed.");
            return;
        }
        viewTasks();
        System.out.print("Enter the number of the task to mark as completed: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < tasks.size()) {
                tasks.get(index).markCompleted();
                System.out.println("Great job! Task marked as completed.");
            } else {
                System.out.println("Invalid task number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}
