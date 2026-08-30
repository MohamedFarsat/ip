import java.util.Scanner;

/**
 * Entry point for Buddy, a simple chatbot used for the iP.
 */
public class Buddy {
    public static final String NAME = "Buddy";
    private static final String LINE = "---------------------------------------------------------------";

    /**
     * Starts Buddy and handles commands entered by the user.
     *
     * @param args command line arguments supplied by the runtime
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskList tasks = new TaskList();

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
                showTasks(tasks);
            } else if (input.startsWith("mark ")) {
                markTask(input, tasks);
            } else if (input.startsWith("unmark ")) {
                unmarkTask(input, tasks);
            } else if (!tasks.isFull()) {
                Task task = createTask(input);
                if (task == null) {
                    System.out.println("Please enter todo, deadline, event, list, mark, unmark, or bye.");
                } else {
                    tasks.add(task);
                    showAddedTask(task, tasks.size());
                }
            } else {
                System.out.println("Sorry, I cannot remember any more tasks.");
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

    private static void showTasks(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(i + ". " + tasks.getTask(i));
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

    private static void markTask(String input, TaskList tasks) {
        try {
            int taskNumber = getTaskNumber(input, "mark ".length());
            Task task = tasks.getTask(taskNumber);
            task.markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("  " + task);
        } catch (IllegalArgumentException e) {
            System.out.println("Please give me a valid task number to mark.");
        }
    }

    private static void unmarkTask(String input, TaskList tasks) {
        try {
            int taskNumber = getTaskNumber(input, "unmark ".length());
            Task task = tasks.getTask(taskNumber);
            task.markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + task);
        } catch (IllegalArgumentException e) {
            System.out.println("Please give me a valid task number to unmark.");
        }
    }

    private static int getTaskNumber(String input, int commandLength) {
        try {
            return Integer.parseInt(input.substring(commandLength));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Task number must be an integer.", e);
        }
    }
}
