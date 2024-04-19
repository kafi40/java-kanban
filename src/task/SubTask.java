package task;

import enums.TaskStatus;

public class SubTask extends Task {

    private final EpicTask epicTask;

    public SubTask(String taskName, String taskDescription, TaskStatus taskStatus, EpicTask epicTask) {
        super(taskName, taskDescription, taskStatus);
        this.epicTask = epicTask;
        epicTask.addSubTask(this);
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }
}
