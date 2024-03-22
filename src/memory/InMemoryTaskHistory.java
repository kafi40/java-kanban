package memory;

import interfaces.TaskHistory;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskHistory<T extends Task> implements TaskHistory<T> {
    private Node head;
    private Node tail;
    private final HashMap<Integer, Node> tasksLink = new HashMap<>();

    public Node linkLast(T data) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, data, null);

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        tail = newNode;
        return tail;
    }

    public void removeNode(Node node) {
        Node prev = node.getPrev();
        Node next = node.getNext();

        if (node == head) {
            head = next;
        }
        if (node == tail) {
            tail = prev;
        }
        if (prev != null) {
            prev.setNext(next);
        }
        if (next != null) {
            next.setPrev(prev);
        }
    }

    @Override
    public void addTaskInHistory(T element) {
        Integer id = element.getTaskId();
        Node node = tasksLink.get(id);

        if (node != null) {
            tasksLink.remove(id);
            removeNode(node);
        }
        tasksLink.put(id, linkLast(element));
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node next = head;
        while (next != null) {
            historyList.add(next.getData());
            next = next.getNext();
        }
        return historyList;
    }

    @Override
    public void removeTaskFromHistory(int id) {
        Node node = tasksLink.get(id);
        if (node != null) {
            tasksLink.remove(node);
            removeNode(node);
        }
    }

    public class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(Node prev, T data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public T getData() {
            return data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}