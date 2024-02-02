package task;

import util.TaskManager;
import util.TaskStatus;

import java.util.Objects;

public class Task {

    private final int taskId = TaskManager.taskIdGenerator();
    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;

    public Task(String taskName, String taskDescription, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "Task{taskID='" + taskId + "',\n"
                + "taskName='" + taskName + "',\n"
                + "taskDescription='" + taskDescription + "',\n"
                + "taskStatus='" + taskStatus + "'}";
    }
}
