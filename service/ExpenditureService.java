package service;

import model.Expenditure;
import model.BankAccount;
import structure.MyLinkedList;
import structure.MyMap;

import java.io.*;
import java.util.Scanner;

public class ExpenditureService {
    private static final String FILE_PATH = "data/expenditures.txt";
    private static MyMap<String, Expenditure> expenditures = new MyMap<>();

    public static void loadExpendituresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    Expenditure exp = new Expenditure(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3], parts[4], parts[5]);
                    expenditures.put(parts[0], exp);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load expenditures: " + e.getMessage());
        }
    }

    public static void addExpenditure() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter code: ");
        String code = scanner.nextLine().trim();

        double amount = 0;
        while (true) {
            try {
                System.out.print("Enter amount: ");
                amount = Double.parseDouble(scanner.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }

        System.out.print("Enter date (yyyy-mm-dd): ");
        String date = scanner.nextLine().trim();

        System.out.print("Enter phase (e.g., construction, marketing, sales): ");
        String phase = scanner.nextLine().trim();

        String category = CategoryService.selectCategoryFromList();

        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine().trim();

        Expenditure expenditure = new Expenditure(code, amount, date, phase, category, accountId);
        expenditures.put(code, expenditure);
        appendToFile(expenditure);

        // Update bank account link
        BankAccount account = BankAccountService.getAccount(accountId);
        if (account != null) {
            account.addExpenditureCode(code);
            BankAccountService.updateAccount(account);
        }

        System.out.println("Expenditure added successfully.");
    }

    private static void appendToFile(Expenditure exp) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(exp.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving expenditure: " + e.getMessage());
        }
    }

    public static void viewAllExpenditures() {
        System.out.println("\n--- All Expenditures ---");
        expenditures.forEach((code, exp) -> {
            System.out.println("Code: " + exp.getCode());
            System.out.println("Amount: GHS " + exp.getAmount());
            System.out.println("Date: " + exp.getDate());
            System.out.println("Phase: " + exp.getPhase());
            System.out.println("Category: " + exp.getCategory());
            System.out.println("Bank Account: " + exp.getAccountId());
            System.out.println("-----------------------------------\n");
        });
    }
}