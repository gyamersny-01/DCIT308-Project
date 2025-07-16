package structure;

public class MyMap<K, V> {
    private static final int SIZE = 100;
    private Entry<K, V>[] table;

    public MyMap() {
        table = new Entry[SIZE];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % SIZE;
    }

    public void put(K key, V value) {
        int index = hash(key);
        table[index] = new Entry<>(key, value);
    }

    public V get(K key) {
        int index = hash(key);
        Entry<K, V> entry = table[index];
        if (entry != null && entry.key.equals(key)) {
            return entry.value;
        }
        return null;
    }

    public void forEach(EntryConsumer<K, V> action) {
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                action.accept(entry.key, entry.value);
            }
        }
    }

    public static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public interface EntryConsumer<K, V> {
        void accept(K key, V value);
    }
}
