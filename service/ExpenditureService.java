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
        String[] fields = {"Code", "Amount", "Date (YYYY-MM-DD)", "Phase", "Category", "Account ID"};
        String[] values = new String[6];

        int i = 0;
        while (i < fields.length) {
            System.out.print("Enter " + fields[i] + " (or type 'back' to edit previous): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                if (i > 0) {
                    i--;
                    continue;
                } else {
                    System.out.println("Already at the first field.");
                    continue;
                }
            }

            switch (i) {
                case 0: // Code
                case 3: // Phase
                case 4: // Category
                case 5: // Account ID
                    if (!input.isEmpty()) {
                        values[i] = input;
                        i++;
                    } else {
                        System.out.println(fields[i] + " cannot be empty.");
                    }
                    break;
                case 1: // Amount
                    try {
                        Double.parseDouble(input);
                        values[i] = input;
                        i++;
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid amount. Please enter a numeric value.\n");
                    }
                    break;
                case 2: // Date
                    if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        values[i] = input;
                        i++;
                    } else {
                        System.out.println("Invalid date format. Use YYYY-MM-DD.");
                    }
                    break;
            }
        }

        String code = values[0];
        double amount = Double.parseDouble(values[1]);
        String date = values[2];
        String phase = values[3];
        String category = values[4];
        String accountId = values[5];

        Expenditure expenditure = new Expenditure(code, amount, date, phase, category, accountId);
        expenditureMap.put(code, expenditure);
        appendToFile(expenditure);
        System.out.println("Expenditure added successfully.\n");
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
