package task;

import enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTask extends Task {

    private ArrayList<SubTask> subTasks;
    private LocalDateTime endTime;

    public EpicTask(String taskName, String taskDescription) {
        super(taskName, taskDescription, TaskStatus.NEW);
        this.subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void removeSubTask() {
        subTasks.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        for (SubTask subTask : subTasks) {

        }
        return endTime;
    }
}
