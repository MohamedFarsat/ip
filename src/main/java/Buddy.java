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
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("added: " + input);
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
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
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
