package buddy.task;

import java.util.ArrayList;
import java.util.List;

import buddy.exception.BuddyException;

/**
 * Stores and manages the tasks remembered by Buddy.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;

    private final Task[] tasks;
    private int size;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new Task[MAX_TASKS];
        size = 0;
    }

    /**
     * Adds a task to the list if there is space.
     *
     * @param task task to add
     */
    public void add(Task task) {
        if (isFull()) {
            throw new IllegalStateException("Task list is full.");
        }
        tasks[size] = task;
        size++;
    }

    /**
     * Returns the task at the given one-based task number.
     *
     * @param taskNumber one-based task number
     * @return matching task
     * @throws BuddyException if there is no task with that number
     */
    public Task getTask(int taskNumber) throws BuddyException {
        if (taskNumber < 1 || taskNumber > size) {
            throw new BuddyException("There is no task number " + taskNumber
                    + ". You have " + size + " task(s).");
        }
        return tasks[taskNumber - 1];
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the task list has reached its maximum size.
     *
     * @return true if no more tasks can be added
     */
    public boolean isFull() {
        return size == MAX_TASKS;
    }

    /**
     * Returns a snapshot of all tasks currently in the list, in order. Used
     * when saving the list to disk.
     *
     * @return a new list containing all tasks
     */
    public List<Task> getAll() {
        List<Task> all = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            all.add(tasks[i]);
        }
        return all;
    }
}
