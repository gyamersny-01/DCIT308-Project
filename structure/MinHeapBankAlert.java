package structure;

import model.BankAccount;

public class MinHeapBankAlert {
    private BankAccount[] heap;
    private int size;

    public MinHeapBankAlert(int capacity) {
        heap = new BankAccount[capacity];
        size = 0;
    }

    public void insert(BankAccount account) {
        if (size >= heap.length) return;
        heap[size] = account;
        int current = size;
        while (current > 0 && heap[current].getBalance() < heap[(current - 1) / 2].getBalance()) {
            BankAccount temp = heap[current];
            heap[current] = heap[(current - 1) / 2];
            heap[(current - 1) / 2] = temp;
            current = (current - 1) / 2;
        }
        size++;
    }

    public void printLowBalanceAccounts(double threshold) {
        System.out.println("--- Low Balance Accounts (Below GHS " + threshold + ") ---");
        for (int i = 0; i < size; i++) {
            if (heap[i].getBalance() < threshold) {
                System.out.println("ID: " + heap[i].getId() + ", Balance: GHS " + heap[i].getBalance());
            }
        }
    }
} 