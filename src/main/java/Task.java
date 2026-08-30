/**
 * Represents a task tracked by Buddy.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description text that describes the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return X if done, otherwise a blank space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the icon that identifies this task type.
     *
     * @return task type icon
     */
    public String getTypeIcon() {
        return "T";
    }

    /**
     * Returns the text that describes this task.
     *
     * @return task description
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Returns the display text for this task.
     *
     * @return task status and description
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
