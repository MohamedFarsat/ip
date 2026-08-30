import java.util.Scanner;

/**
 * Entry point for Buddy, a simple chatbot used for the iP.
 */
public class Buddy {
    public static final String NAME = "Buddy";
    private static final int MAX_TASKS = 100;
    private static final String LINE = "---------------------------------------------------------------";

    /**
     * Greets the user and exits.
     *
     * @param args command line arguments supplied by the runtime
     */
    public static void main(String[] args) {
        String banner = " ____            _     _       \n"
                + "| __ ) _   _  __| | __| |_   _ \n"
                + "|  _ \\| | | |/ _` |/ _` | | | |\n"
                + "| |_) | |_| | (_| | (_| | |_| |\n"
                + "|____/ \\__,_|\\__,_|\\__,_|\\__, |\n"
                + "                          |___/ \n";
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("Hello! I'm " + NAME);
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println(LINE);
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }
            if (input.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(5));
                    tasks[taskNumber - 1].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskNumber - 1]);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
                    System.out.println("Please give me a valid task number to mark.");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(7));
                    tasks[taskNumber - 1].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskNumber - 1]);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
                    System.out.println("Please give me a valid task number to unmark.");
                }
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
}
