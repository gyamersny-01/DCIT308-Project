import service.ExpenditureService;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ExpenditureService.loadExpendituresFromFile();

        while (true) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    ExpenditureService.addExpenditure();
                    break;
                case "2":
                    ExpenditureService.viewAllExpenditures();
                    break;
                case "8":
                    System.out.println("Exiting system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n====== Nkwa Expenditure Management System ======");
        System.out.println("1. Add Expenditure");
        System.out.println("2. View All Expenditures");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }
}
