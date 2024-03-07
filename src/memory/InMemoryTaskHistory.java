package memory;

import interfaces.TaskHistory;
import task.Task;
import util.Node;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class InMemoryTaskHistory implements TaskHistory {

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;
    private final HashMap<Integer, Node<Task>> tasksHistory = new HashMap<>();

    @Override
    public void addTaskInHistory(Task task) {
        if (tasksHistory.contains(task)) {
            tasksHistory.remove(task);
        }
        tasksHistory.addFirst(task);
    }

    @Override
    public Set<Task> getHistory() {
        return tasksHistory;
    }

    @Override
    public void removeTaskFromHistory(int id) {
        tasksHistory.remove(id);
    }

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);

        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }
}