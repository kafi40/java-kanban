package task;

import enums.TaskStatus;

public class SubTask extends Task {

    private int epicTaskId;

    public SubTask(String taskName, String taskDescription, TaskStatus taskStatus, int epicTaskId) {
        super(taskName, taskDescription, taskStatus);
        this.epicTaskId = epicTaskId;
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }
}
