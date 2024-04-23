package memory;

import enums.TaskStatus;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import task.EpicTask;
import task.SubTask;
import task.Task;
import util.Managers;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final TreeSet<Task> taskTreeSet;
    private boolean isSorted = false;
    public static int taskIdCounter = 0;

    public InMemoryTaskManager() {
        taskTreeSet = new TreeSet<>((o1, o2) -> {
            if (o1.getStartTime().equals(o2.getStartTime())) {
                return -1;
            } else {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
    }

    public static int taskIdGenerator() {
        taskIdCounter = taskIdCounter + 1;
        return taskIdCounter;
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public List<SubTask> getEpicSubTasks(int epicId) {
        Optional<EpicTask> optionalEpicTask = Optional.ofNullable(epicTasks.get(epicId));
        return optionalEpicTask.map(epicTask -> epicTask.getSubTasks().stream().toList()).orElse(null);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
        isSorted = false;
    }

    @Override
    public void deleteEpicTasks() {
        epicTasks.values().forEach(epicTask
                -> epicTask.getSubTasks().forEach(subTask
                -> deleteSubTask(subTask.getTaskId())));
        epicTasks.clear();
        isSorted = false;
    }

    @Override
    public void deleteSubTasks() {
        epicTasks.values().forEach(epicTask -> getSubTasks().clear());
        subTasks.clear();
        isSorted = false;
    }

    @Override
    public Task getTask(int id) {
        try {
            System.out.println(tasks.get(id));
        } catch (Exception e) {
            System.out.println(e);
        }
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public EpicTask getEpicTask(int id) {
        if (epicTasks.get(id) != null) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subTasks.get(id) != null) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
        isSorted = false;
    }

    @Override
    public void addEpicTask(EpicTask epicTask) {
        epicTasks.put(epicTask.getTaskId(), epicTask);
        isSorted = false;
    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getEpicTaskId());
        epicTask.addSubTask(subTask);
        setEpicTaskStatus(epicTasks.get(subTask.getEpicTaskId()));
        setEpicTaskDateTime(epicTasks.get(subTask.getEpicTaskId()));
        isSorted = false;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
        isSorted = false;
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTasks.put(epicTask.getTaskId(), epicTask);
        subTasks.values().forEach(subTask -> {
            if (subTask.getEpicTaskId() == epicTask.getTaskId()) {
                epicTask.addSubTask(subTask);
            }
        });
        setEpicTaskDateTime(epicTask);
        isSorted = false;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        EpicTask epicTask = epicTasks.get(subTask.getEpicTaskId());
        epicTask.getSubTasks().remove(subTask);
        subTasks.put(subTask.getTaskId(), subTask);
        epicTask.addSubTask(subTask);
        setEpicTaskStatus(epicTasks.get(subTask.getEpicTaskId()));
        setEpicTaskDateTime(epicTasks.get(subTask.getEpicTaskId()));
        isSorted = false;
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        isSorted = false;
    }

    @Override
    public void deleteEpicTask(int id) {
        subTasks.values().forEach(subTask -> {
            if (subTask.getEpicTaskId() == id) {
                subTasks.remove(subTask.getTaskId());
            }
        });
        epicTasks.remove(id);
        isSorted = false;
    }

    @Override
    public void deleteSubTask(int id) {
        EpicTask epicTask = epicTasks.get(subTasks.get(id).getEpicTaskId());
        epicTask.getSubTasks().remove(subTasks.get(id));
        subTasks.remove(id);
        setEpicTaskDateTime(epicTask);
        isSorted = false;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        if (!isSorted) {
            taskTreeSet.clear();
            List<Task> taskList = new ArrayList<>(tasks.values());
            taskList.addAll(subTasks.values());

            Set<Task> taskSet = taskList.stream()
                    .filter(task -> task.getStartTime() != null)
                    .collect(Collectors.toSet());
            taskTreeSet.addAll(taskSet);
            isSorted = true;
        }
        return taskTreeSet;
    }

    public void setEpicTaskStatus(EpicTask epicTask) {
        boolean isNewTaskFlag = false;
        boolean isDoneTaskFlag = false;

        if (epicTask.getSubTasks().isEmpty()) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else {
            for (SubTask st : epicTask.getSubTasks()) {
                if (st.getTaskStatus().equals(TaskStatus.NEW)) {
                    isNewTaskFlag = true;
                } else {
                    isNewTaskFlag = false;
                    break;
                }
            }
            for (SubTask st : epicTask.getSubTasks()) {
                if (st.getTaskStatus().equals(TaskStatus.DONE)) {
                    isDoneTaskFlag = true;
                } else {
                    isDoneTaskFlag = false;
                    break;
                }
            }
            if (isNewTaskFlag) {
                epicTask.setTaskStatus(TaskStatus.NEW);
            } else if (isDoneTaskFlag) {
                epicTask.setTaskStatus(TaskStatus.DONE);
            } else {
                epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public void setEpicTaskDateTime(EpicTask epicTask) {
        Optional<SubTask> optionalStartTime = epicTask.getSubTasks().stream()
                .filter(subTask -> subTask.getStartTime() != null)
                .min(Comparator.comparing(Task::getStartTime));
        epicTask.setStartTime(optionalStartTime.map(Task::getStartTime).orElse(null));

        epicTask.getSubTasks().stream()
                .filter(subTask -> subTask.getStartTime() != null)
                .max(Comparator.comparing(Task::getEndTime))
                .ifPresent(subTask -> epicTask.setEndTime(subTask.getEndTime()));
        if (epicTask.getStartTime() != null && epicTask.getEndTime() != null) {
            epicTask.setDuration(Duration.between(epicTask.getStartTime(), epicTask.getEndTime()));
        } else {
            epicTask.setDuration(Duration.ofMinutes(0));
        }
    }

    public boolean isTimeIntersection(Task task) {
            Duration duration;
            LocalDateTime startTime;
            LocalDateTime endTime;
            Optional<LocalDateTime> optionalStartTime = Optional.ofNullable(task.getStartTime());
            Optional<Duration> optionalDuration = Optional.ofNullable(task.getDuration());
            if (optionalStartTime.isEmpty()) {
                return false;
            }
            if (optionalDuration.isPresent()) {
                startTime = optionalStartTime.get();
                duration = optionalDuration.get();
                endTime = startTime.plus(duration);
                for (Task t : tasks.values()) {
                    if (task.getTaskId().equals(t.getTaskId()) || t.getStartTime() == null) {
                        continue;
                    }
                    if ((!startTime.isBefore(t.getStartTime()) && !startTime.isAfter(t.getEndTime()))
                        || (endTime.isBefore(t.getEndTime()) && endTime.isAfter(t.getStartTime()))) {
                        return true;
                    }
                }

                for (SubTask t : subTasks.values()) {
                    if (task.getTaskId().equals(t.getTaskId()) || t.getStartTime() == null) {
                        continue;
                    }
                    if ((!startTime.isBefore(t.getStartTime()) && !startTime.isAfter(t.getEndTime()))
                        || (endTime.isBefore(t.getEndTime()) && endTime.isAfter(t.getStartTime()))) {
                        return true;
                    }
                }
            }
            return false;
    }
}
