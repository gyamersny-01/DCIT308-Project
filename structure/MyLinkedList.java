package structure;

public class MyLinkedList {
    private class Node {
        String data;
        Node next;

        Node(String data) {
            this.data = data;
        }
    }

    private Node head;

    public void add(String data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    public boolean contains(String data) {
        Node current = head;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }

    public void printAll() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
