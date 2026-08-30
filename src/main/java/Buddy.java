import java.util.Scanner;

/**
 * Entry point for Buddy, a simple chatbot used for the iP.
 */
public class Buddy {
    public static final String NAME = "Buddy";
    private static final int MAX_TASKS = 100;
    private static final String LINE = "---------------------------------------------------------------";

    /**
     * Starts Buddy and handles commands entered by the user.
     *
     * @param args command line arguments supplied by the runtime
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        showGreeting();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println(LINE);
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }
            if (input.equals("list")) {
                showTasks(tasks, taskCount);
            } else if (input.startsWith("mark ")) {
                markTask(input, tasks, taskCount);
            } else if (input.startsWith("unmark ")) {
                unmarkTask(input, tasks, taskCount);
            } else if (taskCount < MAX_TASKS) {
                Task task = createTask(input);
                if (task == null) {
                    System.out.println("Please enter todo, deadline, event, list, mark, unmark, or bye.");
                } else {
                    tasks[taskCount] = task;
                    taskCount++;
                    showAddedTask(task, taskCount);
                }
            } else {
                System.out.println("Sorry, I can only remember " + MAX_TASKS + " tasks.");
            }
            System.out.println(LINE);
        }
    }

    private static void showGreeting() {
        String banner = " ____            _     _       \n"
                + "| __ ) _   _  __| | __| |_   _ \n"
                + "|  _ \\| | | |/ _` |/ _` | | | |\n"
                + "| |_) | |_| | (_| | (_| | |_| |\n"
                + "|____/ \\__,_|\\__,_|\\__,_|\\__, |\n"
                + "                          |___/ \n";

        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("Hello! I'm " + NAME);
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void showTasks(Task[] tasks, int taskCount) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }

    private static Task createTask(String input) {
        if (input.startsWith("todo ")) {
            return new Todo(input.substring("todo ".length()));
        }
        if (input.startsWith("deadline ")) {
            return createDeadline(input);
        }
        if (input.startsWith("event ")) {
            return createEvent(input);
        }
        return new Todo(input);
    }

    private static Task createDeadline(String input) {
        String content = input.substring("deadline ".length());
        int byIndex = content.indexOf(" /by ");
        if (byIndex == -1) {
            return null;
        }

        String description = content.substring(0, byIndex);
        String by = content.substring(byIndex + " /by ".length());
        if (description.isBlank() || by.isBlank()) {
            return null;
        }
        return new Deadline(description, by);
    }

    private static Task createEvent(String input) {
        String content = input.substring("event ".length());
        int fromIndex = content.indexOf(" /from ");
        int toIndex = content.indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            return null;
        }

        String description = content.substring(0, fromIndex);
        String from = content.substring(fromIndex + " /from ".length(), toIndex);
        String to = content.substring(toIndex + " /to ".length());
        if (description.isBlank() || from.isBlank() || to.isBlank()) {
            return null;
        }
        return new Event(description, from, to);
    }

    private static void showAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        String taskLabel = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskLabel + " in the list.");
    }

    private static void markTask(String input, Task[] tasks, int taskCount) {
        try {
            int taskNumber = getTaskNumber(input, "mark ".length(), taskCount);
            tasks[taskNumber - 1].markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + tasks[taskNumber - 1]);
        } catch (IllegalArgumentException e) {
            System.out.println("Please give me a valid task number to mark.");
        }
    }

    private static void unmarkTask(String input, Task[] tasks, int taskCount) {
        try {
            int taskNumber = getTaskNumber(input, "unmark ".length(), taskCount);
            tasks[taskNumber - 1].markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks[taskNumber - 1]);
        } catch (IllegalArgumentException e) {
            System.out.println("Please give me a valid task number to unmark.");
        }
    }

    private static int getTaskNumber(String input, int commandLength, int taskCount) {
        try {
            int taskNumber = Integer.parseInt(input.substring(commandLength));
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new IllegalArgumentException("Task number is out of range.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Task number must be an integer.", e);
        }
    }
}
