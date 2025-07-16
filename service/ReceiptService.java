package service;

import structure.MyQueue;

import java.io.*;

public class ReceiptService {
    private static final String FILE_PATH = "data/receipts.txt";
    private static MyQueue receiptQueue = new MyQueue();

    public static void uploadReceipt(String code, String note) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(code + ": " + note);
            writer.newLine();
            receiptQueue.enqueue(code);
            System.out.println("Receipt uploaded and added to review queue.");
        } catch (IOException e) {
            System.out.println("Error uploading receipt: " + e.getMessage());
        }
    }

    public static void reviewNextReceipt() {
        if (receiptQueue.isEmpty()) {
            System.out.println("No receipts to review.");
            return;
        }
        String code = receiptQueue.dequeue();
        System.out.println("Reviewing receipt for expenditure: " + code);
    }
}