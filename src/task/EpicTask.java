package task;

import enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

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
    public LocalDateTime getStartTime() {
        Optional<SubTask> optionalStartTime = subTasks.stream()
                .filter(subTask -> subTask.getStartTime() != null)
                .min(Comparator.comparing(Task::getStartTime));
        this.setStartTime(optionalStartTime.map(Task::getStartTime).orElse(null));
        return optionalStartTime.map(Task::getStartTime).orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
         subTasks.stream()
                 .filter(subTask -> subTask.getStartTime() != null)
                 .max(Comparator.comparing(Task::getEndTime))
                 .ifPresent(subTask -> this.endTime = subTask.getEndTime());
        return endTime;
    }
}
