# Buddy User Guide

Buddy is a simple chatbot that stores tasks and tracks whether they are done.

## Starting Buddy

Run `src/main/java/buddy/Buddy.java` from IntelliJ IDEA.

Expected output:

```text
Hello! I'm Buddy
What can I do for you?
```

## Adding Todos

Use `todo` followed by a task description to add a todo task.

Example: `todo read book`

Expected output:

```text
Got it. I've added this task:
  [T][ ] read book
```

## Adding Deadlines

Use `deadline` followed by a task description and `/by` to add a deadline.

Example: `deadline return book /by Sunday`

## Adding Events

Use `event` followed by a task description, `/from`, and `/to` to add an event.

Example: `event project meeting /from Mon 2pm /to 4pm`

## Listing Tasks

Use `list` to show all tasks.

Expected output:

```text
1. [T][ ] read book
2. [D][X] return book (by: Sunday)
```

## Marking Tasks

Use `mark` followed by the task number to mark a task as done.

Example: `mark 2`

## Unmarking Tasks

Use `unmark` followed by the task number to mark a task as not done.

Example: `unmark 2`

## Deleting Tasks

Use `delete` followed by the task number to remove a task from the list.

Example: `delete 3`

Expected output:

```text
Noted. I've removed this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 task in the list.
```

## Finding Tasks

Use `find` followed by a keyword to show tasks whose description contains
that keyword. The search is case-insensitive.

Example: `find book`

Expected output:

```text
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Sunday)
```

## Saving Data

Buddy automatically saves the task list to `./data/buddy.txt` whenever it
changes, and loads it back in the next time Buddy starts. There is no need
to save manually.

## Exiting

Use `bye` to exit Buddy.
