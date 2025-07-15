package structure;

public class MyCategorySet {
    private static final int SIZE = 50;
    private MyLinkedList[] table;

    public MyCategorySet() {
        table = new MyLinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            table[i] = new MyLinkedList();
        }
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % SIZE;
    }

    public boolean add(String category) {
        int index = hash(category);
        if (!table[index].contains(category)) {
            table[index].add(category);
            return true;
        }
        return false;
    }

    public boolean contains(String category) {
        int index = hash(category);
        return table[index].contains(category);
    }

    public void displayAll() {
        for (MyLinkedList bucket : table) {
            bucket.printAll();
        }
    }
}