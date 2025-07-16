package structure;

public class MyQueue {
    private static class Node {
        String data;
        Node next;
        Node(String data) { this.data = data; }
    }

    private Node front, rear;

    public void enqueue(String data) {
        Node newNode = new Node(data);
        if (rear != null) rear.next = newNode;
        rear = newNode;
        if (front == null) front = newNode;
    }

    public String dequeue() {
        if (front == null) return null;
        String data = front.data;
        front = front.next;
        if (front == null) rear = null;
        return data;
    }

    public boolean isEmpty() {
        return front == null;
    }
}