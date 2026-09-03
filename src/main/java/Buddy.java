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
            try {
                handleCommand(input, tasks);
            } catch (BuddyException e) {
                System.out.println("OOPS!!! " + e.getMessage());
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

    /**
     * Works out which command the user entered and carries it out.
     *
     * @param input full line typed by the user
     * @param tasks task list to read from or update
     * @throws BuddyException if the command is unknown or its arguments are invalid
     */
    private static void handleCommand(String input, TaskList tasks) throws BuddyException {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String description = parts.length > 1 ? parts[1].trim() : "";

        switch (command) {
        case "list":
            showTasks(tasks);
            break;
        case "mark":
            markTask(description, tasks);
            break;
        case "unmark":
            unmarkTask(description, tasks);
            break;
        case "todo":
            addTask(new Todo(requireDescription(description, "A todo", "todo")), tasks);
            break;
        case "deadline":
            addTask(createDeadline(description), tasks);
            break;
        case "event":
            addTask(createEvent(description), tasks);
            break;
        default:
            throw new BuddyException(
                    "I don't recognise that command. Try todo, deadline, event, list, mark, unmark, or bye.");
        }
    }

    private static void showTasks(TaskList tasks) throws BuddyException {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(i + ". " + tasks.getTask(i));
        }
    }

    /**
     * Checks that a task description is present, since todo, deadline, and
     * event tasks are meaningless without one.
     *
     * @param description text supplied after the command word
     * @param taskLabel task type with its article, e.g. "A todo" or "An event", used in the error message
     * @param commandWord command word used in the example, e.g. "todo"
     * @return the description, unchanged
     * @throws BuddyException if the description is blank
     */
    private static String requireDescription(String description, String taskLabel, String commandWord)
            throws BuddyException {
        if (description.isBlank()) {
            throw new BuddyException(taskLabel + " needs a description, e.g. \"" + commandWord + " read book\".");
        }
        return description;
    }

    private static Task createDeadline(String description) throws BuddyException {
        int byIndex = description.indexOf(" /by ");
        if (byIndex == -1) {
            requireDescription(description, "A deadline", "deadline");
            throw new BuddyException(
                    "A deadline needs a due date after /by, e.g. \"deadline return book /by Sunday\".");
        }

        String taskDescription = description.substring(0, byIndex).trim();
        String by = description.substring(byIndex + " /by ".length()).trim();
        if (taskDescription.isEmpty()) {
            throw new BuddyException("A deadline needs a description before /by, e.g. \"deadline return book /by Sunday\".");
        }
        if (by.isEmpty()) {
            throw new BuddyException("A deadline needs a due date after /by, e.g. \"deadline return book /by Sunday\".");
        }
        return new Deadline(taskDescription, by);
    }

    private static Task createEvent(String description) throws BuddyException {
        int fromIndex = description.indexOf(" /from ");
        int toIndex = description.indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            requireDescription(description, "An event", "event");
            throw new BuddyException(
                    "An event needs a start and end time, e.g. \"event meeting /from Mon 2pm /to 4pm\".");
        }

        String taskDescription = description.substring(0, fromIndex).trim();
        String from = description.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = description.substring(toIndex + " /to ".length()).trim();
        if (taskDescription.isEmpty()) {
            throw new BuddyException(
                    "An event needs a description before /from, e.g. \"event meeting /from Mon 2pm /to 4pm\".");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BuddyException("An event needs both a start (/from) and an end (/to) time.");
        }
        return new Event(taskDescription, from, to);
    }

    private static void addTask(Task task, TaskList tasks) throws BuddyException {
        if (tasks.isFull()) {
            throw new BuddyException("Sorry, I cannot remember any more tasks.");
        }
        tasks.add(task);
        showAddedTask(task, tasks.size());
    }

    private static void showAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        String taskLabel = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskLabel + " in the list.");
    }

    private static void markTask(String description, TaskList tasks) throws BuddyException {
        Task task = tasks.getTask(getTaskNumber(description, "mark"));
        task.markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    private static void unmarkTask(String description, TaskList tasks) throws BuddyException {
        Task task = tasks.getTask(getTaskNumber(description, "unmark"));
        task.markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Parses the task number that should follow a mark or unmark command.
     *
     * @param description text supplied after the command word
     * @param commandName name of the command, used in the error message
     * @return the parsed task number
     * @throws BuddyException if no number was given or it is not a valid integer
     */
    private static int getTaskNumber(String description, String commandName) throws BuddyException {
        if (description.isBlank()) {
            throw new BuddyException("Tell me which task number to " + commandName + ", e.g. \"" + commandName + " 2\".");
        }
        try {
            return Integer.parseInt(description);
        } catch (NumberFormatException e) {
            throw new BuddyException("\"" + description + "\" is not a valid task number.");
        }
    }
}
