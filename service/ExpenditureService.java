package service;

import model.Expenditure;
import structure.MyHashMap;

import java.io.*;
import java.util.Scanner;

public class ExpenditureService {
    private static final String FILE_PATH = "data/expenditures.txt";
    private static MyHashMap expenditureMap = new MyHashMap();

    public static void addExpenditure() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Code: ");
        String code = scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter Phase: ");
        String phase = scanner.nextLine();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        System.out.print("Enter Account ID: ");
        String accountId = scanner.nextLine();

        Expenditure expenditure = new Expenditure(code, amount, date, phase, category, accountId);
        expenditureMap.put(code, expenditure);
        appendToFile(expenditure);
        System.out.println("Expenditure added successfully.");
    }

    private static void appendToFile(Expenditure expenditure) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(expenditure.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void viewAllExpenditures() {
        System.out.println("\n--- All Expenditures ---");
        expenditureMap.displayAll();
    }

    public static void loadExpendituresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    Expenditure exp = new Expenditure(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3], parts[4], parts[5]);
                    expenditureMap.put(parts[0], exp);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing expenditures to load.");
        }
    }
}
