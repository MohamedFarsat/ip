package buddy;

import java.nio.file.Path;

import buddy.exception.BuddyException;
import buddy.storage.Storage;
import buddy.task.Deadline;
import buddy.task.Event;
import buddy.task.Task;
import buddy.task.TaskList;
import buddy.task.Todo;
import buddy.ui.Ui;

/**
 * Entry point for Buddy, a simple chatbot used for the iP.
 */
public class Buddy {
    private static final Path SAVE_FILE_PATH = Path.of("data", "buddy.txt");

    /**
     * Starts Buddy and handles commands entered by the user.
     *
     * @param args command line arguments supplied by the runtime
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(SAVE_FILE_PATH);
        TaskList tasks = new TaskList();
        for (Task task : storage.load()) {
            tasks.add(task);
        }

        ui.showGreeting();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            ui.showLine();
            if (input.equals("bye")) {
                ui.showGoodbye();
                ui.showLine();
                break;
            }
            try {
                handleCommand(input, tasks, storage, ui);
            } catch (BuddyException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
    }

    /**
     * Works out which command the user entered and carries it out.
     *
     * @param input full line typed by the user
     * @param tasks task list to read from or update
     * @param storage storage used to persist the task list after it changes
     * @param ui used to show the result of the command to the user
     * @throws BuddyException if the command is unknown or its arguments are invalid
     */
    private static void handleCommand(String input, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String description = parts.length > 1 ? parts[1].trim() : "";

        switch (command) {
        case "list":
            ui.showTaskList(tasks.getAll());
            break;
        case "mark":
            markTask(description, tasks, storage, ui);
            break;
        case "unmark":
            unmarkTask(description, tasks, storage, ui);
            break;
        case "todo":
            addTask(new Todo(requireDescription(description, "A todo", "todo")), tasks, storage, ui);
            break;
        case "deadline":
            addTask(createDeadline(description), tasks, storage, ui);
            break;
        case "event":
            addTask(createEvent(description), tasks, storage, ui);
            break;
        case "delete":
            deleteTask(description, tasks, storage, ui);
            break;
        default:
            throw new BuddyException(
                    "I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
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

    private static void addTask(Task task, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        tasks.add(task);
        storage.save(tasks.getAll());
        ui.showTaskAdded(task, tasks.size());
    }

    private static void markTask(String description, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        Task task = tasks.getTask(getTaskNumber(description, "mark"));
        task.markAsDone();
        storage.save(tasks.getAll());
        ui.showTaskMarked(task);
    }

    private static void unmarkTask(String description, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        Task task = tasks.getTask(getTaskNumber(description, "unmark"));
        task.markAsNotDone();
        storage.save(tasks.getAll());
        ui.showTaskUnmarked(task);
    }

    private static void deleteTask(String description, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        Task removedTask = tasks.remove(getTaskNumber(description, "delete"));
        storage.save(tasks.getAll());
        ui.showTaskRemoved(removedTask, tasks.size());
    }

    /**
     * Parses the task number that should follow a mark, unmark, or delete command.
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
