# Buddy User Guide

Buddy is a simple chatbot that stores tasks and tracks whether they are done.

## Starting Buddy

Run `src/main/java/Buddy.java` from IntelliJ IDEA.

Expected output:

```text
Hello! I'm Buddy
What can I do for you?
```

## Adding Tasks

Type a task description to add it to Buddy's task list.

Example: `read book`

Expected output:

```text
added: read book
```

## Listing Tasks

Use `list` to show all tasks.

Expected output:

```text
1. [ ] read book
2. [X] return book
```

## Marking Tasks

Use `mark` followed by the task number to mark a task as done.

Example: `mark 2`

## Unmarking Tasks

Use `unmark` followed by the task number to mark a task as not done.

Example: `unmark 2`

## Exiting

Use `bye` to exit Buddy.
