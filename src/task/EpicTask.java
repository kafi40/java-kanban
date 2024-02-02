package task;

import util.TaskStatus;

import java.util.ArrayList;

public class EpicTask extends Task {

    private ArrayList<SubTask> subTasksId;

    public EpicTask(String taskName, String taskDescription) {
        super(taskName, taskDescription, TaskStatus.NEW);
    }
}
