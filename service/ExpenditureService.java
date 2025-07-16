package service;

import model.Expenditure;
import model.BankAccount;
import structure.MyLinkedList;
import structure.MyMap;

import java.io.*;
import java.util.Scanner;

public class ExpenditureService {

    public static void sortMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--- Sort Expenditures ---");
            System.out.println("1. Sort by Category (Alphabetical)");
            System.out.println("2. Sort by Date (Chronological)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1": sortByCategory(); break;
                case "2": sortByDate(); break;
                case "0": return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void sortByCategory() {
        Expenditure[] list = collectExpenditures();
        for (int i = 0; i < list.length - 1; i++) {
            for (int j = i + 1; j < list.length; j++) {
                if (list[i].getCategory().compareToIgnoreCase(list[j].getCategory()) > 0) {
                    Expenditure temp = list[i];
                    list[i] = list[j];
                    list[j] = temp;
                }
            }
        }
        System.out.println("--- Expenditures Sorted by Category ---");
        for (Expenditure exp : list) {
            printExpenditure(exp);
        }
    }

    private static void sortByDate() {
        Expenditure[] list = collectExpenditures();
        for (int i = 0; i < list.length - 1; i++) {
            for (int j = i + 1; j < list.length; j++) {
                if (list[i].getDate().compareTo(list[j].getDate()) > 0) {
                    Expenditure temp = list[i];
                    list[i] = list[j];
                    list[j] = temp;
                }
            }
        }
        System.out.println("--- Expenditures Sorted by Date ---");
        for (Expenditure exp : list) {
            printExpenditure(exp);
        }
    }

    private static Expenditure[] collectExpenditures() {
        final Expenditure[] temp = new Expenditure[100];
        final int[] index = {0};
        expenditures.forEach((code, exp) -> {
            temp[index[0]] = exp;
            index[0]++;
        });
        Expenditure[] result = new Expenditure[index[0]];
        for (int i = 0; i < index[0]; i++) {
            result[i] = temp[i];
        }
        return result;
    }

    public static void searchMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--- Search Expenditures ---");
            System.out.println("1. By Time Range");
            System.out.println("2. By Category");
            System.out.println("3. By Cost Range");
            System.out.println("4. By Bank Account Used");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1": searchByDateRange(); break;
                case "2": searchByCategory(); break;
                case "3": searchByCostRange(); break;
                case "4": searchByAccount(); break;
                case "0": return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void searchByDateRange() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter start date (yyyy-mm-dd): ");
        String start = scanner.nextLine().trim();
        System.out.print("Enter end date (yyyy-mm-dd): ");
        String end = scanner.nextLine().trim();

        System.out.println("--- Expenditures from " + start + " to " + end + ":");
        expenditures.forEach((code, exp) -> {
            if (exp.getDate().compareTo(start) >= 0 && exp.getDate().compareTo(end) <= 0) {
                printExpenditure(exp);
            }
        });
    }

    private static void searchByCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();

        System.out.println("Expenditures in category: " + category);
        expenditures.forEach((code, exp) -> {
            if (exp.getCategory().equalsIgnoreCase(category)) {
                printExpenditure(exp);
            }
        });
    }

    private static void searchByCostRange() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter minimum amount: ");
        double min = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Enter maximum amount: ");
        double max = Double.parseDouble(scanner.nextLine().trim());

        System.out.println("Expenditures between GHS " + min + " and GHS " + max + ":");
        expenditures.forEach((code, exp) -> {
            if (exp.getAmount() >= min && exp.getAmount() <= max) {
                printExpenditure(exp);
            }
        });
    }

    private static void searchByAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter bank account ID: ");
        String accId = scanner.nextLine().trim();

        System.out.println("Expenditures using account: " + accId);
        expenditures.forEach((code, exp) -> {
            if (exp.getAccountId().equalsIgnoreCase(accId)) {
                printExpenditure(exp);
            }
        });
    }

    private static void printExpenditure(Expenditure exp) {
        System.out.println("Code: " + exp.getCode());
        System.out.println("Amount: GHS " + exp.getAmount());
        System.out.println("Date: " + exp.getDate());
        System.out.println("Phase: " + exp.getPhase());
        System.out.println("Category: " + exp.getCategory());
        System.out.println("Bank Account: " + exp.getAccountId());
        System.out.println("-----------------------------------");
    }
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
        System.out.println("--- All Expenditures ---");
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