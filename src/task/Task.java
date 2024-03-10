package task;

import memory.InMemoryTaskManager;
import util.TaskStatus;
import java.util.Objects;

public class Task {

    private final Integer taskId = InMemoryTaskManager.taskIdGenerator();
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

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Task otherTask = (Task) obj;
        return  Objects.equals(taskId, otherTask.taskId);
    }

    @Override
    public int hashCode() {
        int hash = 17;

        hash += taskId.hashCode();
        if (taskName != null) {
            hash += taskName.hashCode();
        }
        hash *= 31;
        if (taskDescription != null) {
            hash += taskDescription.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Task{taskID='" + taskId + "',\n"
                + "taskName='" + taskName + "',\n"
                + "taskDescription='" + taskDescription + "',\n"
                + "taskStatus='" + taskStatus + "'}";
    }
}
