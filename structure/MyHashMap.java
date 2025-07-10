package structure;

import model.Expenditure;

public class MyHashMap {
    private static final int SIZE = 100;
    private Node[] table = new Node[SIZE];

    private class Node {
        String key;
        Expenditure value;
        Node next;

        Node(String key, Expenditure value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % SIZE;
    }

    public void put(String key, Expenditure value) {
        int idx = hash(key);
        Node head = table[idx];
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }
        Node newNode = new Node(key, value);
        newNode.next = table[idx];
        table[idx] = newNode;
    }

    public Expenditure get(String key) {
        int idx = hash(key);
        Node head = table[idx];
        while (head != null) {
            if (head.key.equals(key)) return head.value;
            head = head.next;
        }
        return null;
    }

    public void displayAll() {
        for (Node head : table) {
            while (head != null) {
                System.out.println(head.value);
                head = head.next;
            }
        }
    }
}