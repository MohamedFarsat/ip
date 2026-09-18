package buddy;

import java.nio.file.Path;

import buddy.exception.BuddyException;
import buddy.parser.Parser;
import buddy.storage.Storage;
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
        String command = Parser.getCommandWord(input);
        String arguments = Parser.getArguments(input);

        switch (command) {
        case "list":
            ui.showTaskList(tasks.getAll());
            break;
        case "mark":
            markTask(Parser.parseTaskNumber(arguments, "mark"), tasks, storage, ui);
            break;
        case "unmark":
            unmarkTask(Parser.parseTaskNumber(arguments, "unmark"), tasks, storage, ui);
            break;
        case "todo":
            addTask(new Todo(Parser.parseDescription(arguments, "A todo", "todo")), tasks, storage, ui);
            break;
        case "deadline":
            addTask(Parser.parseDeadline(arguments), tasks, storage, ui);
            break;
        case "event":
            addTask(Parser.parseEvent(arguments), tasks, storage, ui);
            break;
        case "delete":
            deleteTask(Parser.parseTaskNumber(arguments, "delete"), tasks, storage, ui);
            break;
        default:
            throw new BuddyException(
                    "I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    private static void addTask(Task task, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        tasks.add(task);
        storage.save(tasks.getAll());
        ui.showTaskAdded(task, tasks.size());
    }

    private static void markTask(int taskNumber, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        Task task = tasks.getTask(taskNumber);
        task.markAsDone();
        storage.save(tasks.getAll());
        ui.showTaskMarked(task);
    }

    private static void unmarkTask(int taskNumber, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        Task task = tasks.getTask(taskNumber);
        task.markAsNotDone();
        storage.save(tasks.getAll());
        ui.showTaskUnmarked(task);
    }

    private static void deleteTask(int taskNumber, TaskList tasks, Storage storage, Ui ui) throws BuddyException {
        Task removedTask = tasks.remove(taskNumber);
        storage.save(tasks.getAll());
        ui.showTaskRemoved(removedTask, tasks.size());
    }
}
