package service;

import model.Expenditure;
import model.BankAccount;
import structure.MyMap;
import structure.MinHeapBankAlert;

import java.io.*;
import java.util.Scanner;

public class ExpenditureService {
    private static final String FILE_PATH = "data/expenditures.txt";
    private static MyMap<String, Expenditure> expenditures = new MyMap<>();
    private static MinHeapBankAlert minHeap = new MinHeapBankAlert(50);


    public static void analyzeMaterialImpact() {
        String[] materials = {"cement", "bricks", "blocks", "sand", "iron rods", "wood", "tiles"};
        MyMap<String, Double> materialTotals = new MyMap<>();
        MyMap<String, Integer> materialCounts = new MyMap<>();

        expenditures.forEach((code, exp) -> {
            String category = exp.getCategory().toLowerCase();
            for (String mat : materials) {
                if (category.contains(mat)) {
                    Double currentTotal = materialTotals.get(mat);
                    Integer count = materialCounts.get(mat);
                    if (currentTotal == null) currentTotal = 0.0;
                    if (count == null) count = 0;
                    materialTotals.put(mat, currentTotal + exp.getAmount());
                    materialCounts.put(mat, count + 1);
                }
            }
        });

        System.out.println("--- Building Material Price Impact ---");
        final double[] total = {0};
        materialTotals.forEach((mat, amt) -> {
            int count = materialCounts.get(mat);
            double avg = amt / count;
            System.out.println(capitalize(mat) + " - Total: GHS " + amt + ", Avg: GHS " + String.format("%.2f", avg));
            total[0] += amt;
        });

        System.out.println("Total material-related expenditure: GHS " + total[0]);
        double costPerHouse = 50000; // assumed base cost
        double housesAffordable = 1000000 / (costPerHouse + total[0] / 10); // simulate effect
        System.out.println("Estimated number of affordable houses with adjusted material cost: " + (int) housesAffordable);
    }

    private static String capitalize(String word) {
        return word.substring(0,1).toUpperCase() + word.substring(1);
    }
    public static void forecastProfitability() {
        MyMap<String, Double> monthlyTotals = new MyMap<>();
        expenditures.forEach((code, exp) -> {
            String[] parts = exp.getDate().split("-");
            if (parts.length >= 2) {
                String yearMonth = parts[0] + "-" + parts[1];
                Double current = monthlyTotals.get(yearMonth);
                if (current == null) current = 0.0;
                monthlyTotals.put(yearMonth, current + exp.getAmount());
            }
        });

        // Count number of months
        final int[] count = {0};
        monthlyTotals.forEach((m, v) -> count[0]++);
        String[] months = new String[count[0]];

        // Collect months
        final int[] index = {0};
        monthlyTotals.forEach((m, v) -> months[index[0]++] = m);

        // Sort months
        for (int j = 0; j < months.length - 1; j++) {
            for (int k = j + 1; k < months.length; k++) {
                if (months[j].compareTo(months[k]) > 0) {
                    String temp = months[j];
                    months[j] = months[k];
                    months[k] = temp;
                }
            }
        }

        System.out.println("--- Profit Forecast ---");
        double prev = -1;
        for (String month : months) {
            double total = monthlyTotals.get(month);
            System.out.println(month + " - GHS " + total);
            if (prev != -1) {
                if (total > prev) {
                    System.out.println("↑ Costs increased compared to last month");
                } else if (total < prev) {
                    System.out.println("↓ Costs decreased compared to last month");
                } else {
                    System.out.println("→ Costs remained the same");
                }
            }
            prev = total;
        }
    }


    public static void trackMonthlyBurnRate() {
        MyMap<String, Double> monthlyTotals = new MyMap<>();
        expenditures.forEach((code, exp) -> {
            String[] parts = exp.getDate().split("-");
            if (parts.length >= 2) {
                String yearMonth = parts[0] + "-" + parts[1];
                Double currentTotal = monthlyTotals.get(yearMonth);
                if (currentTotal == null) currentTotal = 0.0;
                monthlyTotals.put(yearMonth, currentTotal + exp.getAmount());
            }
        });

        System.out.println("--- Monthly Burn Rate ---");
        monthlyTotals.forEach((month, total) -> {
            System.out.println(month + " - GHS " + total);
        });
    }
    public static void loadExpendituresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    Expenditure exp = new Expenditure(parts[0], Double.parseDouble(parts[1]), parts[2], parts[3], parts[4], parts[5], parts[6]);
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

        BankAccountService.viewAllAccountIDs();
        System.out.print("Enter account ID: ");
        String accountId = scanner.nextLine().trim();

        System.out.print("Enter receipt description or ID: ");
        String receiptNote = scanner.nextLine().trim();

        Expenditure expenditure = new Expenditure(code, amount, date, phase, category, accountId, receiptNote);
        expenditures.put(code, expenditure);
        appendToFile(expenditure);
        ReceiptService.uploadReceipt(code, receiptNote);

        BankAccount account = BankAccountService.getAccount(accountId);
        if (account != null) {
            account.addExpenditureCode(code);
            account.deduct(amount);
            BankAccountService.updateAccount(account);
            minHeap.insert(account);
        }

        System.out.println("Expenditure added successfully.");
    }
    public static Expenditure getExpenditureByCode(String code) {
    return expenditures.get(code);
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
            printExpenditure(exp);
        });
    }

    public static void printExpenditure(Expenditure exp) {
        System.out.println("Code: " + exp.getCode());
        System.out.println("Amount: GHS " + exp.getAmount());
        System.out.println("Date: " + exp.getDate());
        System.out.println("Phase: " + exp.getPhase());
        System.out.println("Category: " + exp.getCategory());
        System.out.println("Bank Account: " + exp.getAccountId());
        System.out.println("Receipt: " + exp.getReceiptNote());
        System.out.println("-----------------------------------");
    }

    public static void searchMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Search Expenditures ---");
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

        System.out.println("\nExpenditures from " + start + " to " + end + ":");
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

        System.out.println("\nExpenditures in category: " + category);
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

        System.out.println("\nExpenditures between GHS " + min + " and GHS " + max + ":");
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

        System.out.println("\nExpenditures using account: " + accId);
        expenditures.forEach((code, exp) -> {
            if (exp.getAccountId().equalsIgnoreCase(accId)) {
                printExpenditure(exp);
            }
        });
    }

    public static void sortMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Sort Expenditures ---");
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
        System.out.println("\n--- Expenditures Sorted by Category ---");
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
        System.out.println("\n--- Expenditures Sorted by Date ---");
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

    public static void reviewReceiptMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Receipt Review ---");
            System.out.println("1. Review Next Receipt");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    ReceiptService.reviewNextReceipt();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void alertLowBalances() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter threshold (e.g. 1000): ");
        double threshold = Double.parseDouble(scanner.nextLine().trim());
        minHeap.printLowBalanceAccounts(threshold);
    }
}