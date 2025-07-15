import service.ExpenditureService;
import service.CategoryService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenditureService.loadExpendituresFromFile();
        CategoryService.loadCategoriesFromFile();

        while (true) {
            System.out.println("\n--- Nkwa Expenditure Management System ---");
            System.out.println("1. Add Expenditure");
            System.out.println("2. View All Expenditures");
            System.out.println("3. Add Category");
            System.out.println("4. View All Categories");
            System.out.println("5. Search Category");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1":
                    ExpenditureService.addExpenditure();
                    break;
                case "2":
                    ExpenditureService.viewAllExpenditures();
                    break;
                case "3":
                    CategoryService.addCategory();
                    break;
                case "4":
                    CategoryService.viewAllCategories();
                    break;
                case "5":
                    CategoryService.searchCategory();
                    break;
                case "0":
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}