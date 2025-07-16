import service.ExpenditureService;
import service.CategoryService;
import service.BankAccountService;
import structure.MyLinkedList;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenditureService.loadExpendituresFromFile();
        CategoryService.loadCategoriesFromFile();
        BankAccountService.loadAccountsFromFile();

        while (true) {
            System.out.println("\n--- Nkwa Expenditure Management System ---");
            System.out.println("1. Add Expenditure");
            System.out.println("2. View All Expenditures");
            System.out.println("3. Add Category");
            System.out.println("4. View All Categories");
            System.out.println("5. Search Category");
            System.out.println("6. Add Bank Account");
            System.out.println("7. View Bank Accounts");
            System.out.println("8. Search Expenditures");
            System.out.println("9. Sort Expenditures");
            System.out.println("10. Review Receipts");
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
                case "6":
                    BankAccountService.addBankAccount();
                    break;
                case "7":
                    BankAccountService.viewAllAccounts();
                    break;
                case "8":
                    ExpenditureService.searchMenu();
                    break;
                case "9":
                    ExpenditureService.sortMenu();
                    break;
                case "10":
                    ExpenditureService.reviewReceiptMenu();
                    break;
                case "0":
                    System.out.println("\nExiting... Goodbye!\n");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
