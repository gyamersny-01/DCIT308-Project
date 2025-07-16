package service;

import model.BankAccount;
import structure.MyMap;

import java.io.*;
import java.util.Scanner;

public class BankAccountService {
    public static void viewAllAccountIDs() {
        System.out.println("Account IDs:");
        accounts.forEach((id, acc) -> System.out.println("- " + id));
    }
    private static final String FILE_PATH = "data/accounts.txt";
    private static MyMap<String, BankAccount> accounts = new MyMap<>();

    public static void loadAccountsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String id = parts[0];
                    String bank = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    BankAccount account = new BankAccount(id, bank, balance);
                    if (parts.length == 4) {
                        String[] codes = parts[3].split(",");
                        for (String code : codes) {
                            account.addExpenditureCode(code);
                        }
                    }
                    accounts.put(id, account);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load accounts: " + e.getMessage());
        }
    }

    public static void addBankAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter bank name: ");
        String bank = scanner.nextLine().trim();
        System.out.print("Enter initial balance: ");
        double balance = Double.parseDouble(scanner.nextLine().trim());

        BankAccount account = new BankAccount(id, bank, balance);
        accounts.put(id, account);
        appendToFile(account);
        System.out.println("Account added successfully.");
    }

    public static void viewAllAccounts() {
        System.out.println("--- All Bank Accounts ---");
        accounts.forEach((id, acc) -> {
            System.out.println("Account ID: " + acc.getAccountId());
            System.out.println("Bank Name: " + acc.getBankName());
            System.out.println("Balance: GHS " + acc.getBalance());
            System.out.println("Linked Expenditures:");
            acc.getExpenditureCodes().printAll();
            System.out.println("-----------------------------------");
        });
    }

    public static BankAccount getAccount(String id) {
        return accounts.get(id);
    }

    public static void updateAccount(BankAccount account) {
        accounts.put(account.getAccountId(), account);
        saveAllToFile();
    }

    private static void appendToFile(BankAccount acc) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(acc.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving account: " + e.getMessage());
        }
    }

    private static void saveAllToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            accounts.forEach((id, acc) -> {
                try {
                    writer.write(acc.toString());
                    writer.newLine();
                } catch (IOException e) {
                    System.out.println("Error writing account: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.out.println("Error saving all accounts: " + e.getMessage());
        }
    }
}