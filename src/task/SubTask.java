package task;

import util.TaskStatus;

public class SubTask extends Task {

    private final int mainEpicTaskId = 0;

    public SubTask(String taskName, String taskDescription, TaskStatus taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }
}
